package br.com.pedido.Dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Frete {


	@DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser positivo")
	@Digits(integer = 9, fraction = 2, message = "O valor de possuir o formato 'NNNNNNNNN.NN'")
	@NotNull(message = "O valor do frete é obrigatório")
	private BigDecimal valor;
	
}
