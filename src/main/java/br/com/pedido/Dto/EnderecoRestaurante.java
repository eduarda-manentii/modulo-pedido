package br.com.pedido.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnderecoRestaurante {
	
	@NotBlank(message = "A cidade é obrigatória")
	private String cidade;
	
	@NotBlank(message = "O logradouro é obrigatório")
	private String logradouro;
	
	@NotBlank(message = "O bairro é obrigatório")
	private String bairro;
	
	@NotBlank(message = "O cep é obrigatório")
	private Integer cep;
}
