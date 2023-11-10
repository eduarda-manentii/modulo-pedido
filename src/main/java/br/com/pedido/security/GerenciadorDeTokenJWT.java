package br.com.pedido.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class GerenciadorDeTokenJWT {

	@Value("${spring.jwt.secret}")
	private String secret;
	
	@Value("${spring.jwt.ttl-in-millis}")
	private int ttlInMilles;
	
	private Key getChaveDeAssinatura() {
		byte[] keyByte = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyByte);
	}	
	
	public String gerarToken() {
		return Jwts.builder()
				.setClaims(new HashMap<String, Object>())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + ttlInMilles))
				.signWith(getChaveDeAssinatura(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	private Claims extrairDetalhesDo(String tokenGerado) {
		return Jwts.parserBuilder()
				.setSigningKey(getChaveDeAssinatura())
				.build()
				.parseClaimsJws(tokenGerado)
				.getBody();
	}
	
	public String extrairLoginDo(String tokenGerado) {
		Claims detalhes = extrairDetalhesDo(tokenGerado);
		return detalhes.getSubject();
	}
	
	public Date extrairValidadeDo(String tokenGerado) {
		Claims detalhes = extrairDetalhesDo(tokenGerado);
		return detalhes.getExpiration();
	}
	
	public boolean isVencidade(String tokenGerado) {
		Date validade = extrairValidadeDo(tokenGerado);
		return validade.before(new Date());
	}
	
	public boolean isValido(String tokenGerado) {
		boolean isDentroDaValidade = !isVencidade(tokenGerado);
		return isDentroDaValidade;
	}
}