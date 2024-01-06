package com.techchallenge.pagamentos.adapter.mapper.api;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.driver.model.PagamentoModel;
import com.techchallenge.pagamentos.adapter.driver.model.input.EventoPagamentoInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.PagamentoInput;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;

@Component
public class PagamentoApiMapper {

    @Autowired
    private ModelMapper mapper;

    public Pagamento toDomainObject(PagamentoInput input) {
        return mapper.map(input, Pagamento.class);
    }

    public Collection<PagamentoModel> toCollectionModel(Collection<Pagamento> pagamentos) {
        return pagamentos.stream()
                .map(c -> mapper.map(c, PagamentoModel.class))
                .collect(Collectors.toList());
    }

    public EventoPagamento toDomainObject(EventoPagamentoInput input) {
        return mapper.map(input, EventoPagamento.class);
    }
}
