package br.com.pedido.exception.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import br.com.pedido.exception.ErroDaApi;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AcessoNaoAutorizadoHandler implements AccessDeniedHandler{
	
	@Autowired
	private ErrorConverter errorConverter;
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.access.AccessDeniedException accessDeniedException)
			throws IOException, ServletException {

		JSONObject body = errorConverter.criarJsonDeErro(ErroDaApi.ACESSO_NAO_PERMITIDO, 
				"O usuário do token gerado não pode acessar esse recurso");

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		response.getOutputStream().write(body.toString().getBytes(StandardCharsets.UTF_8));
		
	}

}