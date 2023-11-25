package br.com.pedido.integration.router;

import java.io.Serializable;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.pedido.integration.processor.ErrorProcessor;

@Component
public class FromOpcao extends RouteBuilder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Value("${mktplace.url}")
	private String urlDeEnvio;
	
	@Value("${mktplace.url.token}")
	private String urlDeToken;
	
	@Value("${mktplace.login}")
	private String login;
	
	@Value("${mktplace.password}")
	private String senha;
	
	@Autowired
	private ErrorProcessor errorProcessor;	

	@Override
	public void configure() throws Exception {
	    from("direct:receberOpcao")
	        .doTry()
	        .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
	        .setHeader(Exchange.CONTENT_TYPE, constant("application/json;charset=UTF-8"))
	        .process(new Processor() {
	            @Override
	            public void process(Exchange exchange) throws Exception {
	            	 String responseJson = exchange.getIn().getBody(String.class);
	                 JSONObject jsonObject = new JSONObject(responseJson);
	                 Integer idDaOpcao = jsonObject.getInt("idDaOpcao");
	                 Integer idDoCardapio = jsonObject.getInt("idDoCardapio");
	                 exchange.setProperty("idDaOpcao", idDaOpcao);
	                 exchange.setProperty("idDoCardapio", idDoCardapio);

	            }
	        })
	        .to("direct:autenticarCardapios")
	        .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
	        .setHeader(Exchange.CONTENT_TYPE, constant("application/json;charset=UTF-8"))
	        .setHeader("Authorization", simple("Bearer ${exchangeProperty.token}"))	        
	        .toD(urlDeEnvio + "/opcoes-cardapio/cardapio/${exchangeProperty.idDoCardapio}/opcao/${exchangeProperty.idDaOpcao}")
	        .process(new Processor() {				
				@Override
				public void process(Exchange exchange) throws Exception {					
					String responseJson = exchange.getIn().getBody(String.class);
					JSONObject jsonObject = new JSONObject(responseJson);
	                exchange.getMessage().setBody(jsonObject.toString());
				}
			})
	        .doCatch(Exception.class)
	        .setProperty("error", simple("${exception}"))
	        .process(errorProcessor)
	        .end();
	}


}