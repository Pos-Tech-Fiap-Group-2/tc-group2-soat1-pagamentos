package com.techchallenge.pagamentos.adapter.gateways;

import java.util.List;

import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;


public interface PagamentoGateway {

	PagamentoPixResponseDTO efetuarPagamento(Pagamento pagamento);
	PagamentoResponseDTO consultarPagamento(Long paymentId);
	List<TipoPagamento> listar();
	Pagamento consultarStatusPagamento(Long pedidoId);
}
