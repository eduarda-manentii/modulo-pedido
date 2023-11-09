package br.com.pedido.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedido.Dto.NovoPedido;
import br.com.pedido.entity.Pedido;
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
	
}
