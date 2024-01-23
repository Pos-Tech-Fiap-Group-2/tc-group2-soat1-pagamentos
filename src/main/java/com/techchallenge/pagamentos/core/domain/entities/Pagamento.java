package com.techchallenge.pagamentos.core.domain.entities;

import java.math.BigDecimal;

public class Pagamento {
	
	private Long tipoPagamentoId;
	private Long pedidoId;
	private BigDecimal valor;
	private Cliente cliente;
	private String status;

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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getStatus() { return status; }

	public void setStatus(String status) { this.status = status; }
}
