package com.techchallenge.pagamentos.core.domain.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.adapter.gateways.PagamentoGateway;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;

@Service
public class PagamentoUseCase {
	
	@Autowired
	private PagamentoGateway gateway;

	public PagamentoPixResponseDTO efetuarPagamento(Pagamento pagamento) {
		return gateway.efetuarPagamento(pagamento);
	}
	
	public PagamentoResponseDTO consultarPagamento(EventoPagamento evento, String paymentStatus) {
		return gateway.consultarPagamento(evento, paymentStatus);
	}
	
	public List<TipoPagamento> listar() {
		return gateway.listar();
	}

	public Pagamento consultaStatusPagamento(Long pagamentoId, Long pedidoId) {
		return gateway.consultarStatusPagamento(pagamentoId, pedidoId);
	}
}