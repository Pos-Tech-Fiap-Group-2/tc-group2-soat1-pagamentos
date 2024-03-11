package com.techchallenge.pagamentos.adapter.mapper.messaging;

import java.io.IOException;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.techchallenge.pagamentos.core.domain.entities.messaging.Mensagem;

@Component
public class MensagemClienteMapper {

	public String toModel(Mensagem content) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(OffsetDateTime.class, new TypeAdapterDate())
				.create();
		return gson.toJson(content);
	}
	
	class TypeAdapterDate extends TypeAdapter<OffsetDateTime> {

		@Override
		public void write(JsonWriter out, OffsetDateTime value) throws IOException {
			if (value != null) {
				out.value(value.toString());	
			}
		}

		@Override
		public OffsetDateTime read(JsonReader in) throws IOException {
			return OffsetDateTime.parse(in.nextString());
		}
	}
}
