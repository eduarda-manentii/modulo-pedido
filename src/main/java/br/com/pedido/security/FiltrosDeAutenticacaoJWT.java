package br.com.pedido.security;


import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FiltrosDeAutenticacaoJWT extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		boolean isHeaderAuthorizationPresent = authHeader != null &&
				authHeader.startsWith("Bearer ");
		if (isHeaderAuthorizationPresent) {
			authHeader.substring(7);
		}
		
		filterChain.doFilter(request, response);
	}

}