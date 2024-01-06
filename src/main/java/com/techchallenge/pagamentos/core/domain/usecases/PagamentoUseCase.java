package com.techchallenge.pagamentos.core.domain.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.adapter.gateways.PagamentoGateway;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;

@Service
public class PagamentoUseCase {
	
	@Autowired
	private PagamentoGateway gateway;

	public PagamentoPixResponseDTO efetuarPagamento(Long pedidoId, TipoPagamento tipoPagamento) {
		return gateway.efetuarPagamento(pedidoId, tipoPagamento);
	}
	
	public PagamentoResponseDTO consultarPagamento(Long paymentId) {
		return gateway.consultarPagamento(paymentId);
	}
	
	public List<TipoPagamento> listar() {
		return gateway.listar();
	}
}