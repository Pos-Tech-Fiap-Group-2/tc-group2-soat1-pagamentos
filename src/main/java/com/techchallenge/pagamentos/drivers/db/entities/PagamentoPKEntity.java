package com.techchallenge.pagamentos.drivers.db.entities;

import java.io.Serializable;

public class PagamentoPKEntity implements Serializable {

    private static final long serialVersionUID = 2847658200751446734L;
    
    private Long idPagamento;
    
	private Long idPedido;

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

}