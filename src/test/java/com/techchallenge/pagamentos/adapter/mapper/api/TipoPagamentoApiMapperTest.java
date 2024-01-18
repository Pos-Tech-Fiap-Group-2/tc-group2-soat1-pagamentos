package com.techchallenge.pagamentos.adapter.mapper.api;

import com.techchallenge.pagamentos.adapter.driver.model.TipoPagamentoModel;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class TipoPagamentoApiMapperTest {

    @InjectMocks
    private TipoPagamentoApiMapper mapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void toCollectionModel() {

        TipoPagamento tipoPagamento = new TipoPagamento();
        tipoPagamento.setId(1L);
        tipoPagamento.setNome("Pix");

        TipoPagamento tipoPagamento2 = new TipoPagamento();
        tipoPagamento2.setId(2L);
        tipoPagamento2.setNome("Cartão de Crédito");

        TipoPagamentoModel model = new TipoPagamentoModel();
        model.setId(1L);
        model.setNome("Pix");

        List<TipoPagamento> tipoPagamentos = Arrays.asList(tipoPagamento, tipoPagamento2);
        when(modelMapper.map(tipoPagamento, TipoPagamentoModel.class)).thenReturn(model);
        Assertions.assertTrue(mapper.toCollectionModel(tipoPagamentos).size() > 0);
    }
}
