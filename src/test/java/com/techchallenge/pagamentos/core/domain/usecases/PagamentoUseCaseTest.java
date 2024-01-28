package com.techchallenge.pagamentos.core.domain.usecases;

import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.adapter.gateways.PagamentoGateway;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class PagamentoUseCaseTest {

    @InjectMocks
    PagamentoUseCase pagamentoUseCase;

    @Mock
    PagamentoGateway pagamentoGateway;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void dadoUmPagamentoQuandoEfetuarPagamentoDeveEfetuarPagamento() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        PagamentoPixResponseDTO responseDTO = new PagamentoPixResponseDTO(123L, "Approved", "Detalhes",
                "Pix", "qrCodeBase64", "qrCode");

        when(pagamentoGateway.efetuarPagamento(any(Pagamento.class))).thenReturn(responseDTO);

        // Act
        PagamentoPixResponseDTO resultado = pagamentoUseCase.efetuarPagamento(pagamento);

        // Assert
        verify(pagamentoGateway).efetuarPagamento(pagamento);
        assertNotNull(resultado);
        assertEquals(responseDTO.getId(), resultado.getId());
        assertEquals(responseDTO.getStatus(), resultado.getStatus());
        assertEquals(responseDTO.getDetalhes(), resultado.getDetalhes());
        assertEquals(responseDTO.getTipo(), resultado.getTipo());
        assertEquals(responseDTO.getQrCodeBase64(), resultado.getQrCodeBase64());
        assertEquals(responseDTO.getQrCode(), resultado.getQrCode());
    }

    @Test
    void dadoUmPedidoIdQuandoConsultarPagamentoEntaoRetornarPagamento() {
        Long paymentId = 1L;
        PagamentoResponseDTO respostaEsperada = new PagamentoResponseDTO(123L, "Approved", "detalhes", "Pix");

        when(pagamentoGateway.consultarPagamento(anyLong())).thenReturn(respostaEsperada);

        PagamentoResponseDTO resultado = pagamentoUseCase.consultarPagamento(paymentId);

        verify(pagamentoGateway).consultarPagamento(paymentId);

        assertNotNull(resultado);
        assertEquals(respostaEsperada.getId(), resultado.getId());
        assertEquals(respostaEsperada.getStatus(), resultado.getStatus());
        assertEquals(respostaEsperada.getDetalhes(), resultado.getDetalhes());
        assertEquals(respostaEsperada.getTipo(), resultado.getTipo());
    }

    @Test
    void dadoUmaListaDeTiposDePagamentosQuandoConsultarEntaoDeveRetornarListaTipoPagamentos() {
        TipoPagamento tipoPagamento1 = new TipoPagamento();
        tipoPagamento1.setId(1L);
        tipoPagamento1.setNome("Pix");

        TipoPagamento tipoPagamento2 = new TipoPagamento();
        tipoPagamento1.setId(2L);
        tipoPagamento1.setNome("Cartão de Crédito");

        List<TipoPagamento> tipoPagamentos = Arrays.asList(tipoPagamento1, tipoPagamento2);

        when(pagamentoGateway.listar()).thenReturn(tipoPagamentos);

        List<TipoPagamento> result = pagamentoUseCase.listar();

        verify(pagamentoGateway).listar();
        assertNotNull(result);
        assertEquals(tipoPagamento1.getId(), result.get(0).getId());
        assertEquals(tipoPagamento1.getNome(), result.get(0).getNome());
        assertEquals(tipoPagamento2.getId(), result.get(1).getId());
        assertEquals(tipoPagamento2.getNome(), result.get(1).getNome());
    }

    @Test
    void dadoUmPedidoIdQuandoConsultarStatusPagamentoEntaoDeveRetornarStatusPagamento() {
        Long pedidoId = 1L;
        Long pagamentoId = 1L;
        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamentoId(1L);
        pagamento.setValor(BigDecimal.valueOf(10));
        pagamento.setPedidoId(pedidoId);

        when(pagamentoGateway.consultarStatusPagamento(anyLong(),  anyLong())).thenReturn(pagamento);

        Pagamento resultado = pagamentoUseCase.consultaStatusPagamento(pagamentoId, pedidoId);

        verify(pagamentoGateway).consultarStatusPagamento(pagamentoId, pedidoId);

        assertNotNull(resultado);
        assertEquals(pagamento.getTipoPagamentoId(), resultado.getTipoPagamentoId());
        assertEquals(pagamento.getValor(), resultado.getValor());
        assertEquals(pagamento.getPedidoId(), resultado.getPedidoId());
    }
}
