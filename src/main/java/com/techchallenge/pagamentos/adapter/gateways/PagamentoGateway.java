package com.techchallenge.pagamentos.adapter.gateways;

import java.util.List;

import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;


public interface PagamentoGateway {

	PagamentoPixResponseDTO efetuarPagamento(Pagamento pagamento);
	PagamentoResponseDTO consultarPagamento(EventoPagamento evento, String paymentStatus);
	List<TipoPagamento> listar();
	Pagamento consultarStatusPagamento(Long pagamentoId, Long pedidoId);
}
