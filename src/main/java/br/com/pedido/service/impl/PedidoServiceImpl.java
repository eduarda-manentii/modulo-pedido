package br.com.pedido.service.impl;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {
	
	@Override
	public Pedido salvar(Pedido pedido) {
		Preconditions.checkNotNull(pedido, "O pedido é obrigatório");
		return null;
	}

	@Override
	public void atualizarStatusPor(Integer id, Status status) {
		// TODO Auto-generated method stub

	}

	@Override
	public Page<Pedido> listarPor(Integer idRestaurante, Status status, Retirada retirada, Pageable paginacao) {
		return null;
	}

	@Override
	public Pedido buscarPor(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pedido> listarPedidosPor(Status status) {
		// TODO Auto-generated method stub
		return null;
	}

}
