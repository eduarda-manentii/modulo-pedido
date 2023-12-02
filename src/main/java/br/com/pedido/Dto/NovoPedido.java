package br.com.pedido.Dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.pedido.entity.enums.Pagamento;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NovoPedido {
	
	@Enumerated(value = EnumType.STRING)
	@NotNull(message = "O status do pedido não deve ser nulo")
	private Status status;
	
	@NotNull(message = "O tipo de entrega do pedido é obrigatória.")
	@Enumerated(value = EnumType.STRING)
	private Retirada retirada;
	
	@Column(name = "pagamento")
	@NotNull(message = "O pagamento é obrigatório.")
	private Pagamento pagamento;
	
	@DecimalMin(value = "0.0", inclusive = false, message = "O valor total não pode ser inferior a 0.0")
	@Digits(integer = 9, fraction = 2, message = "O valor total deve possuir o formato 'NNNNNNNNN.NN'")
	private BigDecimal valorTotal;
	
	@DecimalMin(value = "0.0", inclusive = true, message = "O valor de desconto não pode ser inferior a 0.0")
	@Digits(integer = 9, fraction = 2, message = "O valor de desconto deve possuir o formato 'NNNNNNNNN.NN'")
	private BigDecimal valorDesconto;
	
	@DecimalMin(value = "0.0", inclusive = false, message = "O valor dos itens não pode ser inferior a 0.0")
	@Digits(integer = 9, fraction = 2, message = "O valor de desconto deve possuir o formato 'NNNNNNNNN.NN'")
	private BigDecimal valorItens;
	
	@DecimalMin(value = "0.0", inclusive = true, message = "O valor do frete não pode ser inferior a 0.0")
	@Digits(integer = 9, fraction = 1, message = "O valor de frete deve possuir o formato 'NNNNNNNNN.N'")
	private BigDecimal valorFrete;
	
	@NotNull(message = "O cliente é obrigatório.")
	private Integer idCliente;
		
	private Integer idCupom;
	
	@Transient
	private Cupom cupom;
	
	@NotNull(message = "O endereço é obrigatório.")
	private Integer idEndereco;
	
	@Transient
	private Endereco endereco;
	
	@NotNull(message = "O restaurante é obrigatório.")
	private Integer idRestaurante;
	
	@Transient
	private Restaurante restaurante;
	
	@NotNull(message = "O id do cardápio é obrigatório.")
	private Integer idDoCardapio;	
	
	@Size(min = 1, message = "O pedido deve possuir ao menos uma opção")
	private List<NovaOpcaoDoPedido> opcoes;
	
	public NovoPedido() {
		this.opcoes = new ArrayList<>();
		this.status = Status.REALIZADO;
		
	}

}
