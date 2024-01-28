package com.techchallenge.pagamentos.drivers.db.entities;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.techchallenge.pagamentos.core.domain.entities.StatusPagamento;

@Entity(name = "Pagamento")
@IdClass(PagamentoPKEntity.class)
public class PagamentoEntity {

	@Id
	@Column(name = "pagamento_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPagamento;

	@Id
    @Column(name = "pedido_id")
	private Long idPedido;

	@Id
	@Column(name = "pagamento_id_externo")
	private Long idPagamentoExterno;

	@Enumerated(EnumType.STRING)
	private StatusPagamento status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="tipo_pagamento_id", nullable = true)
	private TipoPagamentoEntity tipoPagamento;

	private BigDecimal valor;

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public StatusPagamento getStatus() {
		return status;
	}

	public void setStatus(StatusPagamento status) {
		this.status = status;
	}

	public TipoPagamentoEntity getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamentoEntity tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

    public Long getIdPagamentoExterno() { return idPagamentoExterno; }

    public void setIdPagamentoExterno(Long idPagamentoExterno) { this.idPagamentoExterno = idPagamentoExterno; }

	@Override
	public int hashCode() {
		return Objects.hash(idPagamento, idPedido);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagamentoEntity other = (PagamentoEntity) obj;
		return Objects.equals(idPagamento, other.idPagamento) && Objects.equals(idPedido, other.idPedido);
	}
}
