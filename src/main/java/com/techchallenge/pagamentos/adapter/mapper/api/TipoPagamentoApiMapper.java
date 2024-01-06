package com.techchallenge.pagamentos.adapter.mapper.api;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.driver.model.TipoPagamentoModel;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;

@Component
public class TipoPagamentoApiMapper {

    @Autowired
    private ModelMapper mapper;

    public Collection<TipoPagamentoModel> toCollectionModel(Collection<TipoPagamento> pagamentos) {
        return pagamentos.stream()
                .map(c -> mapper.map(c, TipoPagamentoModel.class))
                .collect(Collectors.toList());
    }
}
