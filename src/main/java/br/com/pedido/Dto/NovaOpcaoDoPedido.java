package br.com.pedido.Dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class NovaOpcaoDoPedido {

	@NotNull(message = "O id da opção do pedido é obrigatório")
	@Positive(message = "O id da opção deve ser positivo")
	private Integer idDaOpcao;

	@DecimalMin(message = "O preço não pode ser inferior a R$0.01", value = "0.0", inclusive = false)
	@Digits(message = "O preço deve possuir o formato 'NNNNNNNNN.NN'", integer = 9, fraction = 2)
	private BigDecimal valorItem;

	@DecimalMin(message = "O subtotal não pode ser inferior a R$0.01", value = "0.0", inclusive = false)
	@Digits(message = "O subtotal deve possuir o formato 'NNNNNNNNN.NN'", integer = 9, fraction = 2)
	private BigDecimal subtotal;

	@Positive(message = "A quantidade de itens deve ser positiva.")
	@NotNull(message = "A quantidade de item não pode ser nula.")
	private Integer qtdeItens;

	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@NotBlank(message = "O nome é promocao")
	private String promocao;

}
