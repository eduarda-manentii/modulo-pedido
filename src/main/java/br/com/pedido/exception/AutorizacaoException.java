package br.com.pedido.exception;

public class AutorizacaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AutorizacaoException(String mensagem) {
		super(mensagem);
	}

}
