package br.com.pedido.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.pedido.Dto.NovaOpcaoDoPedido;
import br.com.pedido.Dto.NovoPedido;
import br.com.pedido.entity.OpcaoDoPedido;
import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.composite.OpcaoDoPedidoId;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.repository.PedidosRepository;
import br.com.pedido.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {
	
	@Autowired
	private PedidosRepository repository;
	
	@Override
	public Pedido salvar(NovoPedido novoPedido) {
		Pedido pedido = new Pedido();
	    pedido.setRetirada(novoPedido.getRetirada());
	    pedido.setPagamento(novoPedido.getPagamento());
	    pedido.setStatus(novoPedido.getStatus());
	    pedido.setIdCliente(novoPedido.getIdCliente());
	    pedido.setIdCupom(novoPedido.getIdCupom());
	    pedido.setIdEndereco(novoPedido.getIdEndereco());
	    pedido.setIdRestaurante(novoPedido.getIdRestaurante());

	    BigDecimal desconto = novoPedido.getValorDesconto();
	    pedido.setValorDesconto(desconto);
	    
	    BigDecimal frete = novoPedido.getValorFrete();
	    pedido.setValorFrete(frete);
	    
	    BigDecimal valorItens = novoPedido.getValorItens();
	    pedido.setValorItens(valorItens);
	    
	    BigDecimal subtotal = valorItens.add(frete).subtract(desconto);
	    pedido.setValorTotal(subtotal);
	    
	    pedido.setData(novoPedido.getData());
	    
	    Pedido pedidoSalvo = repository.save(pedido);
	    this.validarDuplicidadeEm(novoPedido.getOpcoes());
	    for (NovaOpcaoDoPedido novaOpcao : novoPedido.getOpcoes()) {
	    	OpcaoDoPedidoId id = new OpcaoDoPedidoId();
			id.setIdDaOpcao(novaOpcao.getIdDaOpcao());
			id.setIdDoPedido(pedidoSalvo.getId());	
			OpcaoDoPedido opcaoDoPedido = new OpcaoDoPedido();
			opcaoDoPedido.setId(id);
			opcaoDoPedido.setPedido(pedidoSalvo);
			opcaoDoPedido.setQtdeItens(novaOpcao.getQtdeItens());
			BigDecimal valor = new BigDecimal(10);
			opcaoDoPedido.setValorItem(valor);
			BigDecimal subTotal = valor.multiply(new BigDecimal(novaOpcao.getQtdeItens()));
			opcaoDoPedido.setSubtotal(subTotal);
			
	        pedidoSalvo.getOpcoes().add(opcaoDoPedido);
	        this.repository.saveAndFlush(pedidoSalvo);
	    }
	    System.out.println(pedidoSalvo);
	    return repository.buscarPor(pedidoSalvo.getId());
	}

	@Override
	public void atualizarStatusPor(Integer id, Status status) {
		Pedido pedidoEncontrado = repository.buscarPor(id);
		Preconditions.checkNotNull(pedidoEncontrado, 
				"Não existe pedido para o id informado");
		Preconditions.checkArgument(pedidoEncontrado.getStatus() != status, 
				"O status já está salvo para a categoria");
		this.repository.atualizarStatusPor(id, status);
	}

	@Override
	public Page<Pedido> listarPor(Integer idRestaurante, Status status, Retirada retirada, Pageable paginacao) {
		return repository.listarPor(idRestaurante, status, retirada, paginacao);
	}

	@Override
	public Pedido buscarPor(Integer id) {
		Pedido pedidoEncontrado = repository.buscarPor(id);
		Preconditions.checkNotNull(pedidoEncontrado, 
				"Não existe pedido para o id informado");
		return pedidoEncontrado;
	}

	@Override
	public List<Pedido> listarPedidosPor(Status status) {
		return repository.listarPedidosPor(status);
	}
	
	private void validarDuplicidadeEm(List<NovaOpcaoDoPedido> opcoesDoPedido) {
		for (NovaOpcaoDoPedido novaOpcao : opcoesDoPedido) {
			int qtdeDeOcorrencias = 0;
			for (NovaOpcaoDoPedido outraOpcao : opcoesDoPedido) {
				if (novaOpcao.getIdDaOpcao().equals(outraOpcao.getIdDaOpcao())) {
					qtdeDeOcorrencias++;
				}
			}
			Preconditions.checkArgument(qtdeDeOcorrencias == 1, "A opção '" + novaOpcao.getIdDaOpcao() + "' está duplicado no pedido.");
		}
	}

}