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
public class FromCupom extends RouteBuilder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Value("${cadastros.url}")
	private String urlDeEnvio;
	
	@Value("${cadastros.url.token}")
	private String urlDeToken;
	
	@Value("${cadastros.login}")
	private String login;
	
	@Value("${cadastros.password}")
	private String senha;
	
	@Autowired
	private ErrorProcessor errorProcessor;	

	@Override
	public void configure() throws Exception {
	    from("direct:receberCupom")
	        .doTry()
	        .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
	        .setHeader(Exchange.CONTENT_TYPE, constant("application/json;charset=UTF-8"))
	        .process(new Processor() {
	            @Override
	            public void process(Exchange exchange) throws Exception {
	            	 String responseJson = exchange.getIn().getBody(String.class);
	                 JSONObject jsonObject = new JSONObject(responseJson);
	                 Integer idDoCupom = jsonObject.getInt("idDoCupom");
	                 exchange.setProperty("idDoCupom", idDoCupom);
	                
	                JSONObject requestBody = new JSONObject();
	                requestBody.put("email", login);
	                requestBody.put("senha", senha);
	                exchange.getMessage().setBody(requestBody.toString());
	            }
	        })
	        .to(urlDeToken)
	        .process(new Processor() {
	            @Override
	            public void process(Exchange exchange) throws Exception {
	                String responseJson = exchange.getMessage().getBody(String.class);
	                JSONObject jsonObject = new JSONObject(responseJson);
	                String token = jsonObject.getString("token");
	                exchange.setProperty("token", token);
	            }
			})
	        .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
	        .setHeader(Exchange.CONTENT_TYPE, constant("application/json;charset=UTF-8"))
	        .setHeader("Authorization", simple("Bearer ${exchangeProperty.token}"))	        
	        .toD(urlDeEnvio + "/cupons/id/${exchangeProperty.idDoCupom}")
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