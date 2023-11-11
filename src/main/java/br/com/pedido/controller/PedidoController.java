package br.com.pedido.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.util.json.JsonObject;
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
import br.com.pedido.Dto.Restaurante;
import br.com.pedido.entity.OpcaoDoPedido;
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

	@Autowired
	private ProducerTemplate fromRestaurante;

	public Restaurante buscarRestaurante(Integer idRestaurante) {

		Restaurante restaurante = new Restaurante();
		JsonObject request = new JsonObject();
		request.put("idRestaurante", idRestaurante);
		JsonObject restauranteEncontrado = fromRestaurante.requestBody("direct:receberRestaurante", request,
				JsonObject.class);

		restaurante.setNome(restauranteEncontrado.getString("nome"));
		restaurante.setId(restauranteEncontrado.getInteger("id"));

		return restaurante;
	}

	private Map<String, Object> converter(Pedido pedido) {
		Map<String, Object> pedidoMap = new HashMap<String, Object>();
		pedidoMap.put("id_pedido", pedido.getId());
		pedidoMap.put("data_pedido", pedido.getData());
		pedidoMap.put("pagamento", pedido.getPagamento());
		pedidoMap.put("opcoes", pedido.getOpcoes());
		pedidoMap.put("tipo de entrega", pedido.getRetirada());
		pedidoMap.put("status", pedido.getStatus());
		pedidoMap.put("valor total", pedido.getValorTotal());
		pedidoMap.put("valor do frete", pedido.getValorFrete());
		pedidoMap.put("valor dos itens", pedido.getValorItens());
		pedidoMap.put("desconto", pedido.getValorDesconto());
		
		Map<String, Object> restauranteMap = new HashMap<String, Object>();
		restauranteMap.put("id_restaurante", pedido.getRestaurante().getId());
		restauranteMap.put("nome", pedido.getRestaurante().getNome());
		pedidoMap.put("restaurante", restauranteMap);

		Map<String, Object> clienteMap = new HashMap<String, Object>();
		clienteMap.put("id_cliente", pedido.getCliente().getId());
		clienteMap.put("nome", pedido.getCliente().getNome());
		pedidoMap.put("cliente", clienteMap);
		
		Map<String, Object> enderecoMap = new HashMap<String, Object>();
		enderecoMap.put("id_endereco", pedido.getEndereco().getId());
		enderecoMap.put("CEP", pedido.getEndereco().getCep());
		enderecoMap.put("estado", pedido.getEndereco().getEstado());
		enderecoMap.put("cidade", pedido.getEndereco().getCidade());
		enderecoMap.put("bairro", pedido.getEndereco().getBairro());
		enderecoMap.put("numero", pedido.getEndereco().getNumero());
		enderecoMap.put("complemento", pedido.getEndereco().getComplemento());
		pedidoMap.put("endereco", enderecoMap);
		
		Map<String, Object> cupomMap = new HashMap<String, Object>();
		cupomMap.put("id_cupom", pedido.getCupom().getId());
		cupomMap.put("codigo", pedido.getCupom().getCodigo());
		cupomMap.put("valor", pedido.getCupom().getValor() + "%");
		pedidoMap.put("cupom", cupomMap);
		
		List<Map<String, Object>> opcoesMap = new ArrayList<Map<String, Object>>();
		for (OpcaoDoPedido opcaoDoPedido : pedido.getOpcoes()) {
				Map<String, Object> opcaoMap = new HashMap<String, Object>();
				opcaoMap.put("id_opcao", opcaoDoPedido.getId());
				opcaoMap.put("qtde_itens", opcaoDoPedido.getQtdeItens());
				opcaoMap.put("valor", opcaoDoPedido.getValorItem());
				opcaoMap.put("subtotal", opcaoDoPedido.getSubtotal());
			}
			
			pedidoMap.put("opcoes", opcoesMap);
		return pedidoMap;
	}

	@Transactional
	@PatchMapping("/id/{id}/status/{status}")
	public ResponseEntity<?> atualizarStatusPor(@PathVariable("id") Integer id, @PathVariable("status") Status status) {
		this.service.atualizarStatusPor(id, status);
		return ResponseEntity.ok().build();
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> inserir(@RequestBody NovoPedido novoPedido) {
		Pedido pedidoSalvo = service.salvar(novoPedido);
		return ResponseEntity.created(URI.create("/pedido/id/" + pedidoSalvo.getId())).build();
	}

	@GetMapping
	public ResponseEntity<?> listarPor(@RequestParam(name = "id-restaurante") Integer idDoRestaurante,
			@RequestParam(name = "status") Status status, @RequestParam(name = "retirada") Retirada retirada,
			@RequestParam("pagina") Optional<Integer> pagina) {
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
			listagem.add(converter(pedido));
		}
		pageMap.put("listagem", listagem);
		return ResponseEntity.ok(pageMap);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<?> buscarPor(@PathVariable("id") Integer id) {
		Pedido pedidoEncontrado = service.buscarPor(id);
		return ResponseEntity.ok(converter(pedidoEncontrado));

	}

}
