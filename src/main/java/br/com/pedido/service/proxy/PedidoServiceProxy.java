package br.com.pedido.service.proxy;

import java.math.BigDecimal;
import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pedido.Dto.Cliente;
import br.com.pedido.Dto.Cupom;
import br.com.pedido.Dto.Endereco;
import br.com.pedido.Dto.NovaOpcaoDoPedido;
import br.com.pedido.Dto.NovoPedido;
import br.com.pedido.Dto.Restaurante;
import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.service.PedidoService;

@Service
public class PedidoServiceProxy implements PedidoService {

	@Autowired
	@Qualifier("pedidoServiceImpl")
	private PedidoService service;

	@Autowired
	private ProducerTemplate fromCupom;

	@Autowired
	private ProducerTemplate fromOpcao;

	@Autowired
	private ProducerTemplate fromRestaurante;
	
	@Autowired
	private ProducerTemplate fromCliente;

	@Autowired
	private ProducerTemplate fromEndereco;
	
	@Override
	public Pedido salvar(NovoPedido novoPedido) {
		Integer idDoCardapio = novoPedido.getIdDoCardapio();

		for (NovaOpcaoDoPedido nop : novoPedido.getOpcoes()) {
			Integer idDaOpcao = nop.getIdDaOpcao();
			JSONObject request = new JSONObject();
			request.put("idDaOpcao", idDaOpcao);
			request.put("idDoCardapio", idDoCardapio);
			JSONObject opcaoDoCardapioEncontrada = fromOpcao.requestBody("direct:receberOpcao", request,
					JSONObject.class);
			nop.setValorItem(opcaoDoCardapioEncontrada.getBigDecimal("preco"));
			nop.setNome(opcaoDoCardapioEncontrada.getString("nome"));
			BigDecimal qtde = new BigDecimal(nop.getQtdeItens());
			BigDecimal subtotal = nop.getValorItem().multiply(qtde);
			nop.setSubtotal(subtotal);
		}
		
		//TODO: implemntar a recuperação do valor do cupom
		Integer idDoCupom = novoPedido.getIdCupom();
		JSONObject requestCupom = new JSONObject();
		requestCupom.put("idDoCupom", idDoCupom);
		JSONObject cupomEncontrado = fromCupom.requestBody("direct:receberCupom", requestCupom, JSONObject.class);
		Cupom cupom = new Cupom();
		cupom.setId(cupomEncontrado.getInt("id"));
		cupom.setValor(cupomEncontrado.getBigDecimal("percentualDeDesconto"));
		cupom.setStatus(cupomEncontrado.getString("status"));
		
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
	public Pedido buscarPor(Integer id) {

		Pedido pedido = service.buscarPor(id);
		
		//RECUPERA O RESTAURANTE NO JSON
		JSONObject requestBodyRestaurante = new JSONObject();
		requestBodyRestaurante.put("idRestaurante", pedido.getIdRestaurante());
		JSONObject restauranteJson = fromRestaurante.requestBody("direct:receberRestaurante", requestBodyRestaurante,
				JSONObject.class);
		Restaurante restaurante = new Restaurante();
		restaurante.setId(restauranteJson.getInt("id"));
		restaurante.setNome(restauranteJson.getString("nome"));
		pedido.setRestaurante(restaurante);

		//RECUPERA O CLIENTE NO JSON
		JSONObject requestBodyCliente = new JSONObject();
		requestBodyCliente.put("idCliente", pedido.getIdCliente());
		JSONObject clienteJson = fromCliente.requestBody("direct:receberCliente", requestBodyCliente,
				JSONObject.class);
		Cliente cliente = new Cliente();
		cliente.setId(clienteJson.getInt("id"));
		cliente.setNome(clienteJson.getString("nome"));
		pedido.setCliente(cliente);
		
		//RECUPERA O CUPOM NO JSON
		JSONObject requestBodyCupom = new JSONObject();
		requestBodyCupom.put("idDoCupom", pedido.getIdCupom());
		JSONObject cupomJson = fromCupom.requestBody("direct:receberCupom", requestBodyCupom,
				JSONObject.class);
		Cupom cupom = new Cupom();
		cupom.setId(cupomJson.getInt("id"));
		cupom.setCodigo(cupomJson.getString("codigo"));
		cupom.setStatus(cupomJson.getString("status"));
		cupom.setValor(cupomJson.getBigDecimal("percentualDeDesconto"));
		pedido.setCupom(cupom);
		
		//RECUPERA O ENDERECO NO JSON
		
		JSONObject requestBodyEndereco = new JSONObject();
		requestBodyEndereco.put("idEndereco", pedido.getIdEndereco());
		JSONObject enderecoJson = fromEndereco.requestBody("direct:receberEndereco", requestBodyEndereco,
				JSONObject.class);
		Endereco endereco = new Endereco();
		endereco.setId(enderecoJson.getInt("id"));
		endereco.setNome(enderecoJson.getString("nome"));
		endereco.setCep(enderecoJson.getString("cep"));
		endereco.setRua(enderecoJson.getString("rua"));
		endereco.setBairro(enderecoJson.getString("bairro"));
		endereco.setCidade(enderecoJson.getString("cidade"));
		endereco.setEstado(enderecoJson.getString("estado"));
		endereco.setNumero(enderecoJson.getString("numeroDaCasa"));
		endereco.setComplemento(enderecoJson.getString("complemento"));
		
		pedido.setEndereco(endereco);
		
		return pedido;

	}

}
