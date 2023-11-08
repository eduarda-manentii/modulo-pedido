package br.com.pedido.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.pedido.entity.OpcaoDoPedido;
import br.com.pedido.entity.Pedido;
import br.com.pedido.repository.OpcoesDoPedidoRepository;
import br.com.pedido.service.OpcaoDoPedidoService;
import jakarta.validation.constraints.NotNull;

@Service
public class OpcaoDoPedidoServiceImpl implements OpcaoDoPedidoService {
	
	@Autowired
	OpcoesDoPedidoRepository repository;

	@Override
	public OpcaoDoPedido salvar(OpcaoDoPedido opcao) {
		Preconditions.checkNotNull(opcao, "A opção é obrigatória");
		OpcaoDoPedido opcaoSalva = repository.save(opcao);
		return opcaoSalva;
	}

}
