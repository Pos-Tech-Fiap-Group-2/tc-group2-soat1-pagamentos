package com.techchallenge.pagamentos.adapter.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.driver.model.TipoPagamentoModel;
import com.techchallenge.pagamentos.adapter.driver.model.input.EventoPagamentoInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.PagamentoInput;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.mapper.api.MercadoPagoApiMapper;
import com.techchallenge.pagamentos.adapter.mapper.api.PagamentoApiMapper;
import com.techchallenge.pagamentos.adapter.mapper.api.TipoPagamentoApiMapper;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.usecases.PagamentoUseCase;

@Component
public class PagamentoController {
	
    @Autowired
    private PagamentoUseCase useCase;
    
    @Autowired
    private PagamentoApiMapper mapper;

	@Autowired
	private MercadoPagoApiMapper mercadoPagoApiMapper;
	
	@Autowired
	private TipoPagamentoApiMapper tipoPagamentoApiMapper;
	
	public PagamentoPixResponseDTO realizarPagamento(PagamentoInput pagamentoInput) {
		Pagamento pagamento = mapper.toDomainObject(pagamentoInput);
		PagamentoPixResponseDTO pagamentoPixResponseDTO = useCase.efetuarPagamento(pagamento);

		return mercadoPagoApiMapper.toDomainObject(pagamentoPixResponseDTO);
	}
	
	public Collection<TipoPagamentoModel> listar() {
		return tipoPagamentoApiMapper.toCollectionModel(useCase.listar());
	}

	public void confirmarPagamento(EventoPagamentoInput eventoPagamentoInput) {
		EventoPagamento eventoPagamento = mapper.toDomainObject(eventoPagamentoInput);
		Long paymentId = eventoPagamento.getData().getId();

		mercadoPagoApiMapper.toDomainObject(useCase.consultarPagamento(paymentId));
	}

	public String consultarStatusPagamento(Long pedidoId) {
		return mapper.toPagamentoStatus(useCase.consultaStatusPagamento(pedidoId));
	}

}