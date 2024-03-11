package com.techchallenge.pagamentos.core.domain.entities.messaging;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Pedido {

	private Long id;
	private BigDecimal valor;
	private OffsetDateTime dataSolicitacao;
	private StatusPedido status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public OffsetDateTime getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(OffsetDateTime dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", valor=" + valor + ", dataSolicitacao=" + dataSolicitacao + ", status=" + status
				+ "]";
	}
}
