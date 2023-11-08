package br.com.pedido.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.repository.PedidosRepository;
import br.com.pedido.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {
	
	@Autowired
	private PedidosRepository repository;
	
	@Override
	public Pedido salvar(Pedido pedido) {
		Preconditions.checkNotNull(pedido, "O pedido é obrigatório");
		Pedido pedidoSalvo = repository.save(pedido);
		return pedidoSalvo;
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

}