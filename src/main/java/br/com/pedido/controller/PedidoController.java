package br.com.pedido.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedido.Dto.NovoPedido;
import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.service.PedidoService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	@Qualifier("pedidoServiceProxy")
	private PedidoService service;
	
	
	@Transactional
	@PatchMapping("/id/{id}/status/{status}")
	public ResponseEntity<?> atualizarStatusPor(
			@PathVariable("id")
			Integer id, 
			@PathVariable("status")
			Status status){
		this.service.atualizarStatusPor(id, status);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> inserir(
			@RequestBody 
			NovoPedido novoPedido) {
		Pedido pedidoSalvo = service.salvar(novoPedido);		
		return ResponseEntity.created(URI.create(
				"/pedido/id/" + pedidoSalvo.getId())).build();
	}
	
	@GetMapping
	public ResponseEntity<?> listarPor(
			@RequestParam(name = "id-restaurante")
			Integer idDoRestaurante, 
			@RequestParam(name = "status")
			Status status, 
			@RequestParam(name = "retirada")
			Retirada retirada, 
			@RequestParam("pagina")
			Optional<Integer> pagina) {
		Pageable paginacao = null;
		if (pagina.isPresent()) {
			paginacao = PageRequest.of(pagina.get(), 15);
		} else {
			paginacao = PageRequest.of(0, 15);
		}
		Page<Pedido> page = service.listarPor(idDoRestaurante, status, retirada, paginacao);
		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("paginacaoAtual", page.getNumber());
		pageMap.put("totalDeItens", page.getTotalElements());
		pageMap.put("totalDePaginas", page.getTotalPages());
		List<Map<String, Object>> listagem = new ArrayList<Map<String, Object>>();
		for (Pedido pedido : page.getContent()) {
			//listagem.add(converter(pedido));
		}
		pageMap.put("listagem", listagem);
		return ResponseEntity.ok(pageMap);
	}
	
	
}
