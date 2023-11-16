package br.com.pedido.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedido.Dto.SolicitacaoDeToken;
import br.com.pedido.security.GerenciadorDeTokenJWT;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private GerenciadorDeTokenJWT gerenciadorDeToken;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping	
	public ResponseEntity<?> logar(@RequestBody SolicitacaoDeToken solicitacao){		
		this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
						solicitacao.getLogin(), solicitacao.getSenha()));		
		String tokenGerado = gerenciadorDeToken.gerarTokenPor(solicitacao.getLogin());
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("token", tokenGerado);
		return ResponseEntity.ok(response);
	}

}
