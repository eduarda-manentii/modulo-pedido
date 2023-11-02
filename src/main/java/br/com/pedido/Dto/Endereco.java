package br.com.pedido.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Endereco {

	@NotNull(message = "O id é obrigatório")
	@Positive(message = "O id do endereco deve ser positivo")
	private Integer id;
	
	@Size(max = 45, message = "O nome do endereco não deve conter mais de 45 caracteries")
	@NotBlank(message = "O nome é obrigatório")
	private String nome;
	
	@Size(max = 45, message = "O cep não deve conter mais de 45 caracteries")
	@NotBlank(message = "O cep é obrigatório")
	private String cep;
	
	@Size(max = 45, message = "A cidade não deve conter mais de 45 caracteries")
	@NotBlank(message = "A cidade é obrigatória")
	private String cidade;
	
	@Size(max = 45, message = "O bairro não deve conter mais de 45 caracteries")
	@NotBlank(message = "O bairro é obrigatório")
	private String bairro;
	
	@Size(max = 45, message = "O estado não deve conter mais de 45 caracteries")
	@NotBlank(message = "O estado é obrigatório")
	private String estado;
	
	@Size(max = 45, message = "O numero não deve conter mais de 45 caracteries")
	@NotBlank(message = "O numero é obrigatório")
	private String numero;
	
	private String complemento;
	
}
