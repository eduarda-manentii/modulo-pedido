package br.com.pedido.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedido.security.GerenciadorDeTokenJWT;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private GerenciadorDeTokenJWT gerenciadorDeToken;
	
	@PostMapping
	public ResponseEntity<?> logar() {
		String tokenGerado = gerenciadorDeToken.gerarToken();
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("token", tokenGerado);
		return ResponseEntity.ok(response);
	}
	

}