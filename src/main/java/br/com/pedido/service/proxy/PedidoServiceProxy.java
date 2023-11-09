package br.com.pedido.service.proxy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
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

@Service
public class PedidoServiceProxy implements PedidoService {
	
	@Autowired
	@Qualifier("pedidoServiceImpl")
	private PedidoService service;
	
	@Value("${https://cardapios-mktplace-api-production.up.railway.app}")
	private String enderecoOpcao = "https://cardapios-mktplace-api-production.up.railway.app";

	@Autowired
	private ProducerTemplate fromOpcao;

	@Override
	public Pedido salvar(NovoPedido novoPedido) {
	    List<NovaOpcaoDoPedido> novasOpcoes = new ArrayList<>();

	    for (int i = 0; i < novoPedido.getOpcoes().size(); i++) {
	        JSONObject opcao = this.fromOpcao.requestBody("direct:receberOpcao", null, JSONObject.class);
	        NovaOpcaoDoPedido opcaoDoPedido = new NovaOpcaoDoPedido();
	        System.out.println(opcao.getString("nome"));
	        //opcaoDoPedido.setValorItem(opcao.getBigDecimal(""));
	        novasOpcoes.add(opcaoDoPedido);
	    }

	    novoPedido.getOpcoes().addAll(novasOpcoes);
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
		return service.buscarPor(id);
	}

}
