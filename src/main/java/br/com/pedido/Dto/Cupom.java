package br.com.pedido.Dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Cupom {

	@NotNull(message = "O id é obrigatório")
	@Positive(message = "O id do cupom deve ser positivo")
	private Integer id;
	
	@Size(max = 20, message = "A codigo do cupom não deve conter mais de 20 caracteries")	
	private String codigo;
	
	private String status;
	
	@DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser positivo")
	@Digits(integer = 9, fraction = 2, message = "O valor de possuir o formato 'NNNNNNNNN.NN'")	
	private BigDecimal valor;
	
}
