package com.techchallenge.pagamentos.adapter.driver.model.input;

import java.math.BigDecimal;

public class PagamentoInput {

	private Long tipoPagamentoId;
	private Long pedidoId;
	private BigDecimal valor;
	
	private ClienteInput cliente;

	public Long getTipoPagamentoId() {
		return tipoPagamentoId;
	}

	public void setTipoPagamentoId(Long tipoPagamentoId) {
		this.tipoPagamentoId = tipoPagamentoId;
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public ClienteInput getCliente() {
		return cliente;
	}

	public void setCliente(ClienteInput cliente) {
		this.cliente = cliente;
	}

}
