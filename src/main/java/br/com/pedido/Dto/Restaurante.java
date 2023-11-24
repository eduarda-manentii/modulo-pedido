package br.com.pedido.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Restaurante {

	@NotNull(message = "O id é obrigatório")
	@Positive(message = "O id do restaurante deve ser positivo")
	private Integer id;
	
	@Size(max = 45, message = "O nome do endereco não deve conter mais de 45 caracteries")
	@NotBlank(message = "O nome é obrigatório")
	private String nome;
	
	@Size(max = 8, min = 8, message = "O cep deve conter 8 digitos")
	private Integer cep;
	
}
