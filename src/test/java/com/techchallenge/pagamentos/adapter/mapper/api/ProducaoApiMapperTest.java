package com.techchallenge.pagamentos.adapter.mapper.api;

import com.techchallenge.pagamentos.adapter.driver.model.PagamentoModel;
import com.techchallenge.pagamentos.adapter.driver.model.input.ClienteInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.EventoPagamentoInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.PagamentoInput;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProducaoApiMapperTest {

    @InjectMocks
    private PagamentoApiMapper mapper;

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

    private List<Pagamento> createPagamentos() {
        Long pedidoId = 1L;
        Pagamento pagamento1 = new Pagamento();
        pagamento1.setTipoPagamentoId(1L);
        pagamento1.setValor(BigDecimal.valueOf(10));
        pagamento1.setPedidoId(pedidoId);
        pagamento1.setStatus("APROVADO");

        Pagamento pagamento2 = new Pagamento();
        pagamento2.setTipoPagamentoId(1L);
        pagamento2.setValor(BigDecimal.valueOf(10));
        pagamento2.setPedidoId(pedidoId);
        pagamento2.setStatus("APROVADO");

        PagamentoModel model = new PagamentoModel();
        model.setPedidoId(pedidoId);
        model.setStatus("APROVADO");
        model.setTipoPagamentoId(1L);
        model.setValor(BigDecimal.valueOf(10));

        List<Pagamento> pagamentos = Arrays.asList(pagamento1, pagamento2);
        return pagamentos;
    }

    @Test
    void toCollectionModel() {
        Pagamento pagamento = createPagamento();
        PagamentoModel model = createPagamentoModel();
        List<Pagamento> pagamentos = createPagamentos();

        when(modelMapper.map(pagamento, PagamentoModel.class)).thenReturn(model);
        Assertions.assertTrue(mapper.toCollectionModel(pagamentos).size() > 0);
    }

    @Test
    void toDomainObject() {
        PagamentoInput input = new PagamentoInput();
        ClienteInput clienteInput = new ClienteInput();
        clienteInput.setCpf(123456789L);
        clienteInput.setNome("Teste");
        clienteInput.setEmail("teste@teste.com");

        input.setCliente(clienteInput);
        input.setPedidoId(1L);
        input.setValor(BigDecimal.valueOf(10));
        input.setTipoPagamentoId(1L);

        Pagamento pagamento = createPagamento();

        when(modelMapper.map(input, Pagamento.class)).thenReturn(pagamento);
        Assertions.assertNotNull(mapper.toDomainObject(input));
    }

    @Test
    void toEventoPagamentoDomainObject() {
        EventoPagamentoInput input = new EventoPagamentoInput();
        EventoPagamentoInput.Data data = new EventoPagamentoInput.Data();
        data.setId(123L);
        input.setData(data);

        EventoPagamento eventoPagamento = new EventoPagamento();
        EventoPagamento.Data eventoPagamentoData = new EventoPagamento.Data();
        eventoPagamentoData.setId(123L);
        eventoPagamento.setData(eventoPagamentoData);

        when(modelMapper.map(input, EventoPagamento.class)).thenReturn(eventoPagamento);
        Assertions.assertNotNull(mapper.toDomainObject(input));
    }

    @Test
    void toPagamentoStatusTest() {
        Pagamento pagamento = createPagamento();
        PagamentoModel pagamentoModel = createPagamentoModel();
        pagamentoModel.setStatus("APROVADO");

        when(modelMapper.map(pagamento, PagamentoModel.class)).thenReturn(pagamentoModel);
        Assertions.assertNotNull(mapper.toPagamentoStatus(pagamento));

        String result = mapper.toPagamentoStatus(pagamento);

        assertEquals("APROVADO", result);
    }
}
