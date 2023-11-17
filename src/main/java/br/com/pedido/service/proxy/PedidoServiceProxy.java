package br.com.pedido.service.proxy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import br.com.pedido.Dto.Usuario;
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
	
	private Map<Integer, Cliente> cacheClientes = new HashMap<>();
	private Map<Integer, Restaurante> cacheRestaurantes = new HashMap<>();
	private Map<Integer, Usuario> cacheUsuarios = new HashMap<>();
	private Map<Integer, Cupom> cacheCupons = new HashMap<>();
	private Map<Integer, Endereco> cacheEnderecos = new HashMap<>();

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
			nop.setPromocao(opcaoDoCardapioEncontrada.getString("recomendado"));
			BigDecimal qtde = new BigDecimal(nop.getQtdeItens());
			BigDecimal subtotal = nop.getValorItem().multiply(qtde);
			nop.setSubtotal(subtotal);
		}
		Integer idDoCupom = novoPedido.getIdCupom();
		JSONObject requestCupom = new JSONObject();
		requestCupom.put("idDoCupom", idDoCupom);
		JSONObject cupomEncontrado = fromCupom.requestBody("direct:receberCupom", requestCupom, JSONObject.class);
		Cupom cupom = new Cupom();
		cupom.setId(cupomEncontrado.getInt("id"));
		cupom.setValor(cupomEncontrado.getBigDecimal("percentualDeDesconto"));
		cupom.setStatus(cupomEncontrado.getString("status"));
		novoPedido.setCupom(cupom);
		return service.salvar(novoPedido);
	}

	@Override
	public void atualizarStatusPor(Integer id, Status status) {
		this.service.atualizarStatusPor(id, status);
	}

	@Override
	public Page<Pedido> listarPor(Optional<Integer> idRestaurante, Status status,
	        Optional<Retirada> retirada, Optional<Integer> resumo, Pageable paginacao) {
	    Page<Pedido> pagina = service.listarPor(idRestaurante, status, retirada, resumo, paginacao);
	    if (resumo.isEmpty() || resumo.get() == 0) {
	        for (Pedido pedido : pagina.getContent()) {
	            carregarInformacoesExtras(pedido);
	        }
	    }
	    return pagina;
	}

	@Override
	public Pedido buscarPor(Integer id) {
	    Pedido pedido = service.buscarPor(id);
	    carregarInformacoesExtras(pedido);
	    return pedido;
	}

	private void carregarInformacoesExtras(Pedido pedido) {
	    pedido.setRestaurante(obterRestaurante(pedido.getIdRestaurante()));
	    pedido.setCliente(obterCliente(pedido.getIdCliente()));
	    pedido.setCupom(obterCupom(pedido.getIdCupom()));
	    pedido.setEndereco(obterEndereco(pedido.getIdEndereco()));
	    pedido.setUsuario(obterUsuario(pedido.getIdCliente()));
	}

	private Restaurante buscarRestaurantePor(Integer id) {
		JSONObject requestBodyRestaurante = new JSONObject();
		requestBodyRestaurante.put("idRestaurante", id);
		JSONObject restauranteJson = fromRestaurante.requestBody("direct:receberRestaurante", requestBodyRestaurante,
				JSONObject.class);
		Restaurante restaurante = new Restaurante();
		restaurante.setId(restauranteJson.getInt("id"));
		restaurante.setNome(restauranteJson.getString("nome"));

		return restaurante;
	}

	private Cliente buscarClientePor(Integer id) {
		JSONObject requestBodyCliente = new JSONObject();
		requestBodyCliente.put("idCliente", id);
		JSONObject clienteJson = fromCliente.requestBody("direct:receberCliente", requestBodyCliente, JSONObject.class);
		Cliente cliente = new Cliente();
		cliente.setId(clienteJson.getInt("id"));
		cliente.setNome(clienteJson.getString("nome"));

		return cliente;
	}

	private Usuario buscarUsuarioPor(Integer id) {
		JSONObject requestBodyCliente = new JSONObject();
		requestBodyCliente.put("idCliente", id);
		JSONObject clienteJson = fromCliente.requestBody("direct:receberCliente", requestBodyCliente, JSONObject.class);

		Usuario usuario = new Usuario();
		JSONObject usuarioJson = clienteJson.getJSONObject("usuario");
		usuario.setEmail(usuarioJson.getString("email"));

		return usuario;
	}

	private Cupom buscarCupomPor(Integer id) {
		JSONObject requestBodyCupom = new JSONObject();
		requestBodyCupom.put("idDoCupom", id);
		JSONObject cupomJson = fromCupom.requestBody("direct:receberCupom", requestBodyCupom, JSONObject.class);
		Cupom cupom = new Cupom();
		cupom.setId(cupomJson.getInt("id"));
		cupom.setCodigo(cupomJson.getString("codigo"));
		cupom.setStatus(cupomJson.getString("status"));
		cupom.setValor(cupomJson.getBigDecimal("percentualDeDesconto"));

		return cupom;
	}

	private Endereco buscarEnderecoPor(Integer id) {
		JSONObject requestBodyEndereco = new JSONObject();
		requestBodyEndereco.put("idEndereco", id);
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

		return endereco;
	}
	
	private Cliente obterCliente(Integer id) {
		return cacheClientes.computeIfAbsent(id, this::buscarClientePor);
	}
	
	private Restaurante obterRestaurante(Integer id) {
		return cacheRestaurantes.computeIfAbsent(id, this::buscarRestaurantePor);
	}
	
	private Usuario obterUsuario(Integer id) {
		return cacheUsuarios.computeIfAbsent(id, this::buscarUsuarioPor);
	}
	
	private Cupom obterCupom(Integer id) {
		return cacheCupons.computeIfAbsent(id, this::buscarCupomPor);
	}
	
	private Endereco obterEndereco(Integer id) {
		return cacheEnderecos.computeIfAbsent(id, this::buscarEnderecoPor);
	}
}
