package br.com.pedido.service.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.pedido.entity.OpcaoDoPedido;
import br.com.pedido.service.OpcaoDoPedidoService;

@Service
public class OpcaoDoPedidoServiceProxy implements OpcaoDoPedidoService {
	
	@Autowired
	@Qualifier("opcaoDoPedidoServiceImpl")
	private OpcaoDoPedidoService service;

	@Override
	public OpcaoDoPedido salvar(OpcaoDoPedido opcao) {
		return service.salvar(opcao);
	}

}
