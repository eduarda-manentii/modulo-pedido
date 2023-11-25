package br.com.pedido.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.pedido.entity.Usuario;
import br.com.pedido.security.CredencialDeAcesso;

@Service
public class CredencialDeAcessoServiceImpl implements UserDetailsService {

	 @Value("${usuario.login}")
	 private String loginSalvo;

	 @Value("${usuario.senha}")
	 private String senhaSalva;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {				
		boolean isLoginValido = loginSalvo.equals(login);
		Preconditions.checkArgument(isLoginValido, "O login enviado não corresponde a nenhum existente.");
		if (isLoginValido) {			
			String senhaSalvaCripto = encodeSenha(senhaSalva);
			Usuario usuario = new Usuario(login, senhaSalvaCripto);
			UserDetails credencial = new CredencialDeAcesso(usuario);	
			return credencial;
		}
		return null;		
	}
	
	 private String encodeSenha(String senha) {
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(senha);
    }
	
}