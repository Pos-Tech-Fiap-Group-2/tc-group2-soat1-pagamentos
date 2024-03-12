package com.techchallenge.pagamentos.drivers.producers.pagamento;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.drivers.producers.NotificacaoBaseProducer;

@Component
public class NotificacaoPagamentoProducer extends NotificacaoBaseProducer<Object> {

	@Value("${cloud.aws.queue.name.pagamento-pedido-aprovado}")
	private String queueName;
	
	@Override
	protected String getQueueName() {
		return queueName;
	}

	@Override
	protected String convertContent(Object value) {
		return null;
	}
}
