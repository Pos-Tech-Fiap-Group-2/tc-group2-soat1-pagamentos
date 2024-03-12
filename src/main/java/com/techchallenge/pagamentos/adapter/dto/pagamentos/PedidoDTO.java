package com.techchallenge.pagamentos.adapter.dto.pagamentos;

import java.time.OffsetDateTime;

import com.techchallenge.pagamentos.core.domain.entities.messaging.StatusPedido;

public class PedidoDTO {

	private Long id;
	private OffsetDateTime dataSolicitacao;
	private StatusPedido status;
	private ClienteDTO cliente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OffsetDateTime getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(OffsetDateTime dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public ClienteDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteDTO cliente) {
		this.cliente = cliente;
	}

	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
	}
}
