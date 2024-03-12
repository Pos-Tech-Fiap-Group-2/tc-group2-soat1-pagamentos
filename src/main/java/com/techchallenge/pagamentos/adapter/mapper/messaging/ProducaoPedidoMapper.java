package com.techchallenge.pagamentos.adapter.mapper.messaging;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techchallenge.pagamentos.adapter.external.producao.PedidoStatusRequest;

@Component
public class ProducaoPedidoMapper {

	public String toModel(PedidoStatusRequest content) {
		Gson gson = new GsonBuilder()
				.create();
		return gson.toJson(content);
	}
}
