package com.techchallenge.pagamentos.drivers.producers.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.mapper.messaging.MensagemClienteMapper;
import com.techchallenge.pagamentos.core.domain.entities.messaging.Mensagem;
import com.techchallenge.pagamentos.drivers.producers.NotificacaoBaseProducer;

@Component
public class NotificacaoClienteProducer extends NotificacaoBaseProducer<Mensagem> {

	@Value("${queue.name.notificacao-cliente}")
	private String queueName;
	
	@Autowired
	private MensagemClienteMapper mapper;
	
	@Override
	protected String getQueueName() {
		return queueName;
	}

	@Override
	protected String convertContent(Mensagem value) {
		return mapper.toModel(value);
	}
}
