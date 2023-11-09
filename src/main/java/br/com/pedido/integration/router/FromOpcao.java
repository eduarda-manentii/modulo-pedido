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
	
	@Value("${opcao.url}")
	private String urlDeEnvio;
	
	@Autowired
	private ErrorProcessor errorProcessor;	

	@Override
	public void configure() throws Exception {
		from("direct:receberOpcao").doTry()
		.setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
		.setHeader(Exchange.CONTENT_TYPE, simple("application/json;charset=UTF-8"))
		.setHeader("Authorization", simple("Bearer eyJhbGciOiJIUzI1NiJ9.eyJwYXBlbCI6IkxPSklTVEEiLCJzdWIiOiJ1c3VhcmlvNS5sb2ppc3RhIiwiaWF0IjoxNjk5NDk0NTMzLCJleHAiOjE2OTk0OTYzMzN9.-4jqqdZ39v3F4ie-minr73BZ-1beWfRIq2EzFo1UDtM"))
//		.process(new Processor() {					
//			@Override
//			public void process(Exchange exchange) throws Exception {				
//				  String responseBody = exchange.getIn().getBody(String.class);
//				  
//              ObjectMapper objectMapper = new ObjectMapper();
//              Object valorItem = objectMapper.readValue(responseBody, new TypeReference<Object>() {});
//
//              exchange.getIn().setBody(valorItem);
//			}
//		})
		.toD(urlDeEnvio)
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				String jsonBody = exchange.getMessage().getBody(String.class);
				JSONObject jsonObject = new JSONObject(jsonBody);
				exchange.getMessage().setBody(jsonObject);				
			}
		})
		.doCatch(Exception.class)
			.setProperty("error", simple("${exception}"))
			.process(errorProcessor)
	.end();
			
	}

}
