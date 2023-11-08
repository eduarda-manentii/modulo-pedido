package br.com.pedido.service.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.service.PedidoService;

@Service
public class PedidoServiceProxy implements PedidoService {
	
	@Autowired
	@Qualifier("pedidoServiceImpl")
	private PedidoService service;

	@Override
	public Pedido salvar(Pedido pedido) {
		return service.salvar(pedido);
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
