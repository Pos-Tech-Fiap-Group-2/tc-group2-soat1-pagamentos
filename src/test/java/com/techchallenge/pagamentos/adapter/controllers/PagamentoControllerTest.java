package com.techchallenge.pagamentos.adapter.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pagamentos.adapter.driver.model.TipoPagamentoModel;
import com.techchallenge.pagamentos.adapter.driver.model.input.ClienteInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.EventoPagamentoInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.PagamentoInput;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.adapter.mapper.api.MercadoPagoApiMapper;
import com.techchallenge.pagamentos.adapter.mapper.api.PagamentoApiMapper;
import com.techchallenge.pagamentos.adapter.mapper.api.TipoPagamentoApiMapper;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento.Data;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;
import com.techchallenge.pagamentos.core.domain.usecases.PagamentoUseCase;

@RunWith(SpringJUnit4ClassRunner.class)
public class PagamentoControllerTest {

    @InjectMocks
    PagamentoController pagamentoController;

    @Mock
    private PagamentoUseCase useCase;

    @Mock
    private PagamentoApiMapper mapper;

    @Mock
    private MercadoPagoApiMapper mercadoPagoApiMapper;

    @Mock
    private TipoPagamentoApiMapper tipoPagamentoApiMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void realizarPagamento() {
        PagamentoInput input = new PagamentoInput();
        ClienteInput clienteInput = new ClienteInput();
        clienteInput.setCpf(123456789L);
        clienteInput.setNome("Teste");
        clienteInput.setEmail("teste@teste.com");

        input.setCliente(clienteInput);
        input.setPedidoId(1L);
        input.setValor(BigDecimal.valueOf(10));
        input.setTipoPagamentoId(1L);

        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamentoId(1L);
        pagamento.setValor(BigDecimal.valueOf(10));
        pagamento.setPedidoId(123L);

        PagamentoPixResponseDTO pagamentoPixResponseDTO = new PagamentoPixResponseDTO(123L, "Approved", "Detalhes",
                "Pix", "qrCodeBase64", "qrCode");

        when(mapper.toDomainObject(input)).thenReturn(pagamento);
        when(useCase.efetuarPagamento(pagamento)).thenReturn(pagamentoPixResponseDTO);
        when(mercadoPagoApiMapper.toDomainObject(pagamentoPixResponseDTO)).thenReturn(pagamentoPixResponseDTO);

        PagamentoPixResponseDTO pagamentoRetornado = pagamentoController.realizarPagamento(input);

        assertNotNull(pagamentoRetornado);
        assertEquals(pagamentoPixResponseDTO.getId(), pagamentoRetornado.getId());
        assertEquals(pagamentoPixResponseDTO.getQrCode(), pagamentoRetornado.getQrCode());
        assertEquals(pagamentoPixResponseDTO.getTipo(), pagamentoRetornado.getTipo());
        assertEquals(pagamentoPixResponseDTO.getDetalhes(), pagamentoRetornado.getDetalhes());
        assertEquals(pagamentoPixResponseDTO.getStatus(), pagamentoRetornado.getStatus());
        assertEquals(pagamentoPixResponseDTO.getQrCodeBase64(), pagamentoRetornado.getQrCodeBase64());
    }

    @Test
    void listarTest() {
        // Arrange
        TipoPagamentoModel tipoPagamentoModel1 = new TipoPagamentoModel();
        tipoPagamentoModel1.setId(1L);
        tipoPagamentoModel1.setNome("Pix");

        TipoPagamentoModel tipoPagamentoModel2 = new TipoPagamentoModel();
        tipoPagamentoModel2.setId(2L);
        tipoPagamentoModel2.setNome("Cartão de Crédito");

        List<TipoPagamentoModel> tipoPagamentoModels = Arrays.asList(tipoPagamentoModel1, tipoPagamentoModel2);

        TipoPagamento tipoPagamento = new TipoPagamento();
        tipoPagamento.setId(1L);
        tipoPagamento.setNome("Pix");

        TipoPagamento tipoPagamento2 = new TipoPagamento();
        tipoPagamento2.setId(2L);
        tipoPagamento2.setNome("Cartão de Crédito");
        List<TipoPagamento> tipoPagamentos = Arrays.asList(tipoPagamento, tipoPagamento2);


        when(useCase.listar()).thenReturn(tipoPagamentos);
        when(tipoPagamentoApiMapper.toCollectionModel(tipoPagamentos)).thenReturn(tipoPagamentoModels);

        // Act
        Collection<TipoPagamentoModel> resultado = pagamentoController.listar();

        // Assert
        assertEquals(tipoPagamentoModels, resultado);
        verify(useCase).listar();
        verify(tipoPagamentoApiMapper).toCollectionModel(tipoPagamentos);
    }

    @Test
    void confirmarPagamentoTest() {
        EventoPagamentoInput input = new EventoPagamentoInput();
        EventoPagamentoInput.Data data = new EventoPagamentoInput.Data();
        data.setId(123L);
        data.setPedidoId(1L);
        input.setData(data);
        
        String status = "approved";

        EventoPagamento evento = new EventoPagamento();
        evento.setData(new Data());
        evento.getData().setId(123L);
        evento.getData().setPedidoId(1L);

        PagamentoResponseDTO pagamentoResponseDTO = new PagamentoResponseDTO(evento.getData().getId(), status, "Detalhes",
                "Pix");

        when(mapper.toDomainObject(input)).thenReturn(evento);
        when(useCase.consultarPagamento(evento, status)).thenReturn(pagamentoResponseDTO);
        when(mercadoPagoApiMapper.toDomainObject(pagamentoResponseDTO)).thenReturn(pagamentoResponseDTO);

        pagamentoController.confirmarPagamento(input, status);

        verify(mapper).toDomainObject(input);
        verify(useCase).consultarPagamento(evento, status);
        verify(mercadoPagoApiMapper).toDomainObject(pagamentoResponseDTO);
        assertDoesNotThrow(() -> pagamentoController.confirmarPagamento(input, status));
    }

    @Test
    void consultarStatusPagamento() {
    	Long pagamentoId = 1L;
        Long pedidoId = 123456L;
        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamentoId(1L);
        pagamento.setValor(BigDecimal.valueOf(10));
        pagamento.setPedidoId(pedidoId);
        pagamento.setStatus("APROVADO");

        when(useCase.consultaStatusPagamento(pagamentoId, pedidoId)).thenReturn(pagamento);
        when(mapper.toPagamentoStatus(pagamento)).thenReturn(pagamento.getStatus());

        String resultado = pagamentoController.consultarStatusPagamento(pagamentoId, pedidoId);

        verify(mapper).toPagamentoStatus(pagamento);
        assertNotNull(resultado);
        assertEquals(pagamento.getStatus(), resultado);
    }
}
