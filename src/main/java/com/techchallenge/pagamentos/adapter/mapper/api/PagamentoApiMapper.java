package com.techchallenge.pagamentos.adapter.mapper.api;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.driver.model.TipoPagamentoModel;
import com.techchallenge.pagamentos.adapter.driver.model.input.EventoPagamentoInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.TipoPagamentoInput;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;

@Component
public class PagamentoApiMapper {

    @Autowired
    private ModelMapper mapper;

    public TipoPagamento toDomainObject(TipoPagamentoInput input) {
        return mapper.map(input, TipoPagamento.class);
    }

    public Collection<TipoPagamentoModel> toCollectionModel(Collection<TipoPagamento> tipoPagamento) {
        return tipoPagamento.stream()
                .map(c -> mapper.map(c, TipoPagamentoModel.class))
                .collect(Collectors.toList());
    }

    public EventoPagamento toDomainObject(EventoPagamentoInput input) {
        return mapper.map(input, EventoPagamento.class);
    }
}
