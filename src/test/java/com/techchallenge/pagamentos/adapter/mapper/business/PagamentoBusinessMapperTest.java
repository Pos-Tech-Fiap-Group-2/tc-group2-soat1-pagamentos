package com.techchallenge.pagamentos.adapter.mapper.business;

import com.techchallenge.pagamentos.adapter.driver.model.PagamentoModel;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.StatusPagamento;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.entities.TipoPagamentoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class PagamentoBusinessMapperTest {

    @InjectMocks
    private PagamentoBusinessMapper mapper;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private Pagamento createPagamento() {
        Long pedidoId = 1L;
        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamentoId(1L);
        pagamento.setValor(BigDecimal.valueOf(10));
        pagamento.setPedidoId(pedidoId);
        pagamento.setStatus("APROVADO");

        return pagamento;
    }

    private PagamentoModel createPagamentoModel() {
        Long pedidoId = 1L;
        PagamentoModel model = new PagamentoModel();
        model.setPedidoId(pedidoId);
        model.setStatus("APROVADO");
        model.setTipoPagamentoId(1L);
        model.setValor(BigDecimal.valueOf(10));

        return model;
    }

    @Test
    void toModelTest() {
        TipoPagamentoEntity tipoPagamentoEntity = new TipoPagamentoEntity();
        tipoPagamentoEntity.setNome("Pix");
        tipoPagamentoEntity.setId(1L);


        PagamentoEntity entity = new PagamentoEntity();
        entity.setIdPedido(1L);
        entity.setIdPagamentoExterno(123456L);
        entity.setStatus(StatusPagamento.APROVADO);
        entity.setValor(BigDecimal.valueOf(10));
        entity.setIdPedido(1L);
        entity.setTipoPagamento(tipoPagamentoEntity);
        Pagamento pagamento = createPagamento();

        when(modelMapper.map(entity, Pagamento.class)).thenReturn(pagamento);
        assertNotNull(mapper.toModel(entity));
    }
}
