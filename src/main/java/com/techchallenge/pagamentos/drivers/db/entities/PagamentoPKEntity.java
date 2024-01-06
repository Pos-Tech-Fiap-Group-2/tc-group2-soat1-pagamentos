package com.techchallenge.pagamentos.drivers.db.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Embeddable
public class PagamentoPKEntity implements Serializable {

    private static final long serialVersionUID = 2847658200751446734L;

	@Column(name = "pagamento_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagamento;
    
    @Column(name = "pedido_id")
	private Long idPedido;

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
