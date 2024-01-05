package com.techchallenge.pagamentos.adapter.gateways;

import java.util.List;

import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;


public interface PagamentoGateway {

	PagamentoPixResponseDTO efetuarPagamento(Long pedidoId, TipoPagamento tipoPagamento);
	PagamentoResponseDTO consultarPagamento(Long paymentId);
	List<TipoPagamento> listar();
}
