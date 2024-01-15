package com.techchallenge.pagamentos.adapter.mapper.business;

import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PagamentoBusinessMapper {

    @Autowired
    private ModelMapper mapper;
    public Pagamento toModel(PagamentoEntity entity) {
        return mapper.map(entity, Pagamento.class);
    }
}
