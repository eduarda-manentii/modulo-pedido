package br.com.pedido.exception;

public class ConverterException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ConverterException(String mensagem) {
		super(mensagem);
	}

}
