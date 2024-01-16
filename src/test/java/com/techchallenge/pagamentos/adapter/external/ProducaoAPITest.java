package com.techchallenge.pagamentos.adapter.external;

import com.techchallenge.pagamentos.adapter.external.producao.PedidoStatusRequest;
import com.techchallenge.pagamentos.adapter.external.producao.ProducaoAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProducaoAPITest {
    @Autowired
    private ProducaoAPI producaoAPI;

    @MockBean
    private ProducaoAPI mockProducaoAPI;

    @Test
    public void testAdicionarPedidoFilaProducao() {
        // Arrange
        doNothing().when(mockProducaoAPI).adicionarPedidoFilaProducao("1");

        // Act
        producaoAPI.adicionarPedidoFilaProducao("1");

        // Assert
        verify(mockProducaoAPI, times(1)).adicionarPedidoFilaProducao("1");
        verify(mockProducaoAPI).adicionarPedidoFilaProducao("1");
    }

    @Test
    public void testAtualizarStatusPedidoProducao() {
        // Arrange
        PedidoStatusRequest request = new PedidoStatusRequest();
        request.setStatus("RECEBIDO");
        doNothing().when(mockProducaoAPI).atualizarStatusPedidoProducao("1", request);

        // Act
        producaoAPI.atualizarStatusPedidoProducao("1", request);

        // Assert
        verify(mockProducaoAPI, times(1)).atualizarStatusPedidoProducao("1", request);
        verify(mockProducaoAPI).atualizarStatusPedidoProducao("1", request);
    }
}

