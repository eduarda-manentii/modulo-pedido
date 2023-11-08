package br.com.pedido.service;

import org.springframework.validation.annotation.Validated;

import br.com.pedido.entity.OpcaoDoPedido;
import jakarta.validation.constraints.NotNull;

@Validated
public interface OpcaoDoPedidoService {

	public OpcaoDoPedido salvar(@NotNull(message = "A opção do pedido é obrigatório") OpcaoDoPedido opcao);

}
