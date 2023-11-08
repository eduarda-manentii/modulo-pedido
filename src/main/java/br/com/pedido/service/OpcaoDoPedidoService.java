package br.com.pedido.service;

import org.springframework.validation.annotation.Validated;

import br.com.pedido.entity.OpcaoDoPedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
public interface OpcaoDoPedidoService {
	
	OpcaoDoPedido buscarOpcaoPor(
			@NotNull(message = "O id do pedido é obrigatório")
			@Positive(message = "O id do pedido deve ser positivo")
			Integer idDoPedido, 
			@NotNull(message = "O id da opção é obrigatório")
			@Positive(message = "O id da opção deve ser positivo")
			Integer idDaOpcao);

    Long contarPor(
    		@NotNull(message = "O id do pedido é obrigatório")
			@Positive(message = "O id do pedido deve ser positivo")
    		Integer idDoPedido, 
    		@NotNull(message = "O id da opção é obrigatório")
			@Positive(message = "O id da opção deve ser positivo")
    		Integer idDaOpcao);

}
