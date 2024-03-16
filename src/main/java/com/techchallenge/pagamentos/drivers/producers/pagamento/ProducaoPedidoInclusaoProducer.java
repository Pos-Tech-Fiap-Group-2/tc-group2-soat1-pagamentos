package com.techchallenge.pagamentos.drivers.producers.pagamento;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.external.producao.PedidoStatusRequest;
import com.techchallenge.pagamentos.adapter.mapper.messaging.ProducaoPedidoMapper;
import com.techchallenge.pagamentos.drivers.producers.NotificacaoBaseProducer;

@Component
public class ProducaoPedidoInclusaoProducer extends NotificacaoBaseProducer<PedidoStatusRequest> {

	@Value("${queue.name.producao-pedido-inclusao}")
	private String queueName;
	
	@Autowired
	private ProducaoPedidoMapper mapper;
	
	@Override
	protected String getQueueName() {
		Queue queue = new Queue(queueName, Boolean.TRUE);
		return queue.getName();
	}

	@Override
	protected String convertContent(PedidoStatusRequest value) {
		return mapper.toModel(value);
	}
}
