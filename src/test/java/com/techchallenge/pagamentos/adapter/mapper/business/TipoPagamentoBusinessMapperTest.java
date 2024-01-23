package com.techchallenge.pagamentos.adapter.mapper.business;

import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;
import com.techchallenge.pagamentos.drivers.db.entities.TipoPagamentoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class TipoPagamentoBusinessMapperTest {

    @InjectMocks
    private TipoPagamentoBusinessMapper mapper;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void toModelTest() {
        TipoPagamentoEntity tipoPagamentoEntity = new TipoPagamentoEntity();
        tipoPagamentoEntity.setNome("Pix");
        tipoPagamentoEntity.setId(1L);

        TipoPagamento tipoPagamento = new TipoPagamento();
        tipoPagamento.setId(1L);
        tipoPagamento.setNome("Pix");

        when(modelMapper.map(tipoPagamentoEntity, TipoPagamento.class)).thenReturn(tipoPagamento);
        Assertions.assertNotNull(mapper.toModel(tipoPagamentoEntity));
    }

    @Test
    void toCollectionModel() {
        TipoPagamentoEntity tipoPagamentoEntity1 = new TipoPagamentoEntity();
        tipoPagamentoEntity1.setNome("Pix");
        tipoPagamentoEntity1.setId(1L);

        TipoPagamentoEntity tipoPagamentoEntity2 = new TipoPagamentoEntity();
        tipoPagamentoEntity2.setId(2L);
        tipoPagamentoEntity2.setNome("Cartão de Crédito");


        TipoPagamento tipoPagamento = new TipoPagamento();
        tipoPagamento.setId(1L);
        tipoPagamento.setNome("Pix");

        TipoPagamento tipoPagamento2 = new TipoPagamento();
        tipoPagamento2.setId(2L);
        tipoPagamento2.setNome("Cartão de Crédito");


        List<TipoPagamento> tipoPagamentos = Arrays.asList(tipoPagamento, tipoPagamento2);
        List<TipoPagamentoEntity> entities = Arrays.asList(tipoPagamentoEntity1, tipoPagamentoEntity2);

        when(modelMapper.map(tipoPagamentoEntity1, TipoPagamento.class)).thenReturn(tipoPagamento);
        Assertions.assertTrue(mapper.toCollectionModel(entities).size() > 0);
    }
}
