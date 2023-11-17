package br.com.pedido.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import br.com.pedido.Dto.NovoPedido;
import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
public interface PedidoService {

	public Pedido salvar(
			@NotNull(message = "O pedido é obrigatório")
			NovoPedido novoPedido);
	
	public void atualizarStatusPor(
			@NotNull(message = "O id é obrigatório")
			@Positive(message = "O id deve ser positivo")
			Integer id, 
			@NotNull(message = "O status para a atualização é obrigatorio")
			Status status);
	
	public Page<Pedido> listarPor(
			Optional<Integer> idRestaurante,
			@NotNull(message = "O status é obrigatorio")
			Status status,
			Optional<Retirada> retirada,
			Optional<Integer> resumo,
			Pageable paginacao);
	
	public Pedido buscarPor(
			@NotNull(message = "O id para busca é obrigatório")
			@Positive(message = "O id para busca deve positivo")
			Integer id);
	
}