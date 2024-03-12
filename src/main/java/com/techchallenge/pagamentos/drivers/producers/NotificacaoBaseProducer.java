package com.techchallenge.pagamentos.drivers.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public abstract class NotificacaoBaseProducer<T> {

	@Autowired
	private JmsTemplate jmsTemplate; 
	
	protected abstract String getQueueName();
	
	protected abstract String convertContent(T value);
	
	public void enviar(T value) {
	    try{
	    	System.out.println(getQueueName());
	    	System.out.println(convertContent(value));
	        jmsTemplate.convertAndSend(getQueueName(), convertContent(value));
	    }catch (Exception e) {
	        throw e;
	    }
	}
}
