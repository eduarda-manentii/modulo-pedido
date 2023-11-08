package br.com.pedido.service.impl;

import org.springframework.validation.annotation.Validated;

import br.com.pedido.entity.OpcaoDoPedido;
import br.com.pedido.service.OpcaoDoPedidoService;

@Validated
public class OpcaoDoPedidoServiceImpl implements OpcaoDoPedidoService {

	@Override
	public OpcaoDoPedido buscarOpcaoPor(Integer idDoPedido, Integer idDaOpcao) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long contarPor(Integer idDoPedido, Integer idDaOpcao) {
		// TODO Auto-generated method stub
		return null;
	}

}
