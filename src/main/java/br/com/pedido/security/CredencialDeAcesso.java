package br.com.pedido.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.pedido.entity.Usuario;
import lombok.Data;

@Data
public class CredencialDeAcesso implements UserDetails {
	
private static final long serialVersionUID = 1L;
	
	private String login;
	
	private String senha;
	
	private List<GrantedAuthority> permissoes;
	
	public CredencialDeAcesso(Usuario usuario) {
		this.login = usuario.getLogin();
		this.senha = usuario.getSenha();
//		this.permissoes = new ArrayList<GrantedAuthority>();
//		this.permissoes.add(new SimpleGrantedAuthority(usuario.getPapel().toString()));
	}

	@Override	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getPermissoes();
	}

	@Override
	public String getPassword() {
		return getSenha();
	}

	@Override
	public String getUsername() {
		return getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
