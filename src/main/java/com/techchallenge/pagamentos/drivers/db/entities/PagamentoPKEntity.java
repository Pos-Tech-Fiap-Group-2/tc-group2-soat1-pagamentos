package com.techchallenge.pagamentos.drivers.db.entities;

import java.io.Serializable;
import java.util.Objects;

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
		PagamentoPKEntity other = (PagamentoPKEntity) obj;
		return Objects.equals(idPagamento, other.idPagamento) && Objects.equals(idPedido, other.idPedido);
	}

}