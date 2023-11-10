package br.com.pedido.service.proxy;

import java.math.BigDecimal;
import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.util.json.JsonObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pedido.Dto.NovaOpcaoDoPedido;
import br.com.pedido.Dto.NovoPedido;
import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.service.PedidoService;
import br.com.pedido.Dto.Restaurante;

@Service
public class PedidoServiceProxy implements PedidoService {
	
	@Autowired
	@Qualifier("pedidoServiceImpl")
	private PedidoService service;
	
	@Value("${opcao.url}")
	private String enderecoOpcao = "opcao.url";

	@Autowired
	private ProducerTemplate fromOpcao;
	
	@Autowired
	private ProducerTemplate fromRestaurante;

	@Override
	public Pedido salvar(NovoPedido novoPedido) {
	    Integer idDoCardapio = novoPedido.getIdDoCardapio();
	    
	    for (NovaOpcaoDoPedido nop : novoPedido.getOpcoes()) {
	        Integer idDaOpcao = nop.getIdDaOpcao();
	        JSONObject request = new JSONObject();
	        request.put("idDaOpcao", idDaOpcao);
	        request.put("idDoCardapio", idDoCardapio);
	        JSONObject opcaoDoCardapioEncontrada = fromOpcao.requestBody(
	        		"direct:receberOpcao", request, JSONObject.class);
	        nop.setValorItem(opcaoDoCardapioEncontrada.getBigDecimal("preco"));
	        BigDecimal qtde = new BigDecimal(nop.getQtdeItens());
	        BigDecimal subtotal = nop.getValorItem().multiply(qtde);
	        nop.setSubtotal(subtotal);
	    }
	    return service.salvar(novoPedido);
	}

	@Override
	public void atualizarStatusPor(Integer id, Status status) {
		this.service.atualizarStatusPor(id, status);		
	}

	@Override
	public List<Pedido> listarPedidosPor(Status status) {
		return service.listarPedidosPor(status);
	}

	@Override
	public Page<Pedido> listarPor(Integer idRestaurante, Status status, Retirada retirada, Pageable paginacao) {
		return service.listarPor(idRestaurante, status, retirada, paginacao);
	}

	@Override
	public Pedido buscarPor (Integer id) {
		
		Pedido pedido = service.buscarPor(id);
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("idRestaurante", pedido.getIdRestaurante());
		
		JSONObject restauranteJson = fromRestaurante.requestBody(
				"direct:receberRestaurante", requestBody, JSONObject.class);
		
		Restaurante restaurante = new Restaurante();
		restaurante.setId(restauranteJson.getInt("id"));
		restaurante.setNome(restauranteJson.getString("nome"));
		System.out.println(restaurante.getNome());
		return pedido;
		
	}
	
	
	
	

}
