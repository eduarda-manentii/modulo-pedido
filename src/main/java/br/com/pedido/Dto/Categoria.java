package br.com.pedido.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class Categoria {

	@NotNull(message = "O id é obrigatório")
	@Positive(message = "O id da categoria deve ser positivo")
	private Integer id;
	
	@Size(max = 100, message = "O nome da categoria não deve conter mais de 100 caracteries")
	@NotBlank(message = "O nome é obrigatório")
	private String nome;
	
}
