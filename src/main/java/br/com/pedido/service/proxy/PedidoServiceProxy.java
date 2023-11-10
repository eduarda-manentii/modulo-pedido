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
		cupom.setValor(cupomEncontrado.getBigDecimal("valor"));
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

		JSONObject requestBodyRestaurante = new JSONObject();
		requestBodyRestaurante.put("idRestaurante", pedido.getIdRestaurante());
		JSONObject restauranteJson = fromRestaurante.requestBody("direct:receberRestaurante", requestBodyRestaurante,
				JSONObject.class);
		Restaurante restaurante = new Restaurante();
		restaurante.setId(restauranteJson.getInt("id"));
		restaurante.setNome(restauranteJson.getString("nome"));
		pedido.setRestaurante(restaurante);
		
		//TOD: esperar os endpoints dos amigos
		Cliente cliente = new Cliente();
		cliente.setId(1);
		cliente.setNome("Eduarda Manenti");
		pedido.setCliente(cliente);
		
		Cupom cupom = new Cupom();
		cupom.setId(1);
		cupom.setCodigo("PRIMEIROCOMPRA20");
		cupom.setStatus("ACEITO_PELO_RESTAURANTE");
		BigDecimal valor = new BigDecimal(20);
		cupom.setValor(valor);
		pedido.setCupom(cupom);
		
		Endereco endereco = new Endereco();
		endereco.setId(1);
		endereco.setCep("88703-658");
		endereco.setBairro("Sertão dos Correias");
		endereco.setCidade("Tubarão");
		endereco.setEstado("Santa Catarina");
		endereco.setNumero("1194");
		endereco.setComplemento("Casa");
		endereco.setNome("Casa");
		pedido.setEndereco(endereco);
		
		return pedido;

	}

}
