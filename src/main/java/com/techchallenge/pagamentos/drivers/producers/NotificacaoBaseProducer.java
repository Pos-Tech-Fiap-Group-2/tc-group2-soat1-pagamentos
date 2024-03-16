package com.techchallenge.pagamentos.drivers.producers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NotificacaoBaseProducer<T> {

	@Autowired
    private RabbitTemplate rabbitTemplate;
	
	protected abstract String getQueueName();
	
	protected abstract String convertContent(T value);
	
	private Logger logger = LoggerFactory.getLogger(NotificacaoBaseProducer.class);
	
	public void enviar(T value) {
	    try{
	    	String queueName = getQueueName();
	    	String content = convertContent(value);
	    	
	    	logger.info(String.format("Nome da fila: %s", queueName));
	    	logger.info(String.format("Conteúdo da mensagem: %s", content));
	    	
	        rabbitTemplate.convertAndSend(queueName, content);
	    }catch (Exception e) {
	        throw e;
	    }
	}
}
