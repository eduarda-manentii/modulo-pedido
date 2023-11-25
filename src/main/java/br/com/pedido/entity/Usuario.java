package br.com.pedido.entity;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {
	
	@Id
	@Size(max = 50, message = "O login do usuário não deve conter mais de 50 caracteres")
	@NotBlank(message = "O login do usuário é obrigatório")
	@Column(name = "login")
	private String login;
	
	@NotBlank(message = "A senha do usuário é obrigatória")
	@Column(name = "senha")
	private String senha;
	
//	@Enumerated(value = EnumType.STRING)
//	@NotNull(message = "O papel do usuário é obrigatório")
//	@Column(name = "papel")
//	private Papel papel;
	
}