package com.techchallenge.pagamentos.drivers.db.entities;

import java.math.BigDecimal;

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
}
