package br.com.pedido.exception.handler;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import br.com.pedido.exception.ErroDaApi;

@Component
public class ErrorConverter {
	
	public JSONObject criarJsonDeErro(ErroDaApi erroDaApi, String msgDeErro) {
		
		JSONObject body = new JSONObject();					
		
		JSONObject detalhe = new JSONObject();
		detalhe.put("mensagem", msgDeErro);
		detalhe.put("codigo", erroDaApi.getCodigo());
		
		JSONArray detalhes = new JSONArray();
		detalhes.put(detalhe);
		
		body.put("erros", detalhes);
		
		return body;
		
	}

	public Map<String, Object> criarMapDeErro(ErroDaApi erroDaApi, String msgDeErro){			
		
		JSONObject body = new JSONObject();					
		
		JSONObject detalhe = new JSONObject();
		detalhe.put("mensagem", msgDeErro);
		detalhe.put("codigo", erroDaApi.getCodigo());
		
		JSONArray detalhes = new JSONArray();
		detalhes.put(detalhe);
		
		body.put("erros", detalhes);
		
		return criarJsonDeErro(erroDaApi, msgDeErro).toMap();
		
	}
	
}
