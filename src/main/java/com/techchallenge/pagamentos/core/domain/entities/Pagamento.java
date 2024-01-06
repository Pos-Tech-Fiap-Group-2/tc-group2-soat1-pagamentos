package com.techchallenge.pagamentos.core.domain.entities;

import java.math.BigDecimal;

public class Pagamento {
	
	private Long tipoPagamentoId;
	private Long pedidoId;
	private BigDecimal valor;
	private Cliente cliente;

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
}
