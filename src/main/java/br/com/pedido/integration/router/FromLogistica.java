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
public class FromLogistica extends RouteBuilder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Value("${****}")
	private String urlDeEnvio;

	@Value("${***************}")
	private String urlDeToken;

	@Value("${******************}")
	private String login;

	@Value("${******************}")
	private String senha;

	@Autowired
	private ErrorProcessor errorProcessor;

	@Override
	public void configure() throws Exception {
		from("direct:receberValorFrete")
				.doTry()
				.process(new Processor() {					
					@Override
					public void process(Exchange exchange) throws Exception {
						String responseJson = exchange.getIn().getBody(String.class);
						JSONObject jsonObject = new JSONObject(responseJson);
						Integer cepRestaurante = jsonObject.getInt("cepRestaurante");
						Integer cepEndereco = jsonObject.getInt(responseJson);
						exchange.setProperty("cepRestaurante", cepRestaurante);
						exchange.setProperty("cepEndereco", cepEndereco);
					}
				})
				.to("direct:autenticarLogistica")
				.setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
				.setHeader(Exchange.CONTENT_TYPE, constant("application/json;charset=UTF-8"))
				.setHeader("Authorization", simple("Bearer ${exchangeProperty.token}"))
				.toD(urlDeEnvio + "****************************************************")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						String responseJson = exchange.getIn().getBody(String.class);
						JSONObject jsonObject = new JSONObject(responseJson);
						exchange.getMessage().setBody(jsonObject.toString());
					}
				}).doCatch(Exception.class)
				.setProperty("error", simple("${exception}"))
				.process(errorProcessor)
				.end();
	}

}
