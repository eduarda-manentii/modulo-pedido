package br.com.pedido.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.pedido.Dto.NovoPedido;
import br.com.pedido.entity.OpcaoDoPedido;
import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.repository.OpcoesDoPedidoRepository;
import br.com.pedido.repository.PedidosRepository;
import br.com.pedido.service.PedidoService;
import jakarta.validation.constraints.NotNull;

@Service
public class PedidoServiceImpl implements PedidoService {
	
	@Autowired
	private PedidosRepository repository;
	
	@Autowired
	private OpcoesDoPedidoRepository opcaoRepository;
	
	public Pedido salvar(NovoPedido novoPedido) {
		Pedido pedido = new Pedido();
	    pedido.setRetirada(pedido.getRetirada());
	    pedido.setPagamento(pedido.getPagamento());
	    pedido.setStatus(pedido.getStatus());
	    pedido.setIdCliente(pedido.getCliente().getId());
	    pedido.setIdCupom(pedido.getCupom().getId());
	    pedido.setIdEndereco(pedido.getEnderco().getId());
	    pedido.setIdRestaurante(pedido.getEnderco().getId());
	    pedido.setValorDesconto(pedido.getValorDesconto());
	    pedido.setValorFrete(pedido.getValorFrete());
	    pedido.setValorItens(pedido.getValorItens());
	    pedido.setValorTotal(pedido.getValorTotal());
	    Pedido pedidoSalvo = repository.save(pedido);

	    for (OpcaoDoPedido opcao : pedido.getOpcoes()) {
	        opcao.setPedido(pedidoSalvo);
	        opcaoRepository.save(opcao);
	    }
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

	@Override
	public Pedido salvar(@NotNull(message = "O pedido é obrigatório") Pedido pedido) {
		// TODO Auto-generated method stub
		return null;
	}

}