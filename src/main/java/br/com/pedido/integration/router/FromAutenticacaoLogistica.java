package br.com.pedido.integration.router;

import java.io.Serializable;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FromAutenticacaoLogistica extends RouteBuilder implements Serializable{
	
	private static final long serialVersionUID = 1L;	

	@Value("${logistica.url.token}")
	private String urlDeToken;

	@Value("${logistica.login}")
	private String login;

	@Value("${logistica.password}")
	private String senha;
	
	@Override
	public void configure() throws Exception {
		from("direct:autenticarLogistica")
		.setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
		.setHeader(Exchange.CONTENT_TYPE, constant("application/json;charset=UTF-8"))
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				JSONObject requestBody = new JSONObject();
				requestBody.put("login", login);
				requestBody.put("senha", senha);
				exchange.getMessage().setBody(requestBody.toString());
			}
		})
		.to(urlDeToken).process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				String responseJson = exchange.getMessage().getBody(String.class);
				JSONObject jsonObject = new JSONObject(responseJson);
				String token = jsonObject.getString("token");
				exchange.setProperty("token", token);
			}
		})
		.end();
	}
	
	
}
