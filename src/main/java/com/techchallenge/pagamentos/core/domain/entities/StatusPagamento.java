package com.techchallenge.pagamentos.core.domain.entities;

import java.util.Optional;
import java.util.stream.Stream;

public enum StatusPagamento {
    AGUARDANDO_PAGAMENTO("waiting"),
    PROCESSAMENTO("processing"),
    APROVADO("approved"),
    RECUSADO("rejected");
	
	private String status;
    
    private StatusPagamento(String status) {
    	this.status = status;
    }
    
    public String getDescricao() {
    	return this.status;
    }
    
    public static StatusPagamento getStatusPagamentoByStatus(String status) {
    	Optional<StatusPagamento> optional = Stream.of(StatusPagamento.values())
    			.filter(s -> s.status.equals(status)).findFirst();
    	
    	return optional.get();
    }
}