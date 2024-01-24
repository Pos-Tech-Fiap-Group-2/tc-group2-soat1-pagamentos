package com.techchallenge.pagamentos.adapter.external.producao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class PedidoStatusRequestTest {


    @Test
    void testGetPedidoStatusRequest() {
        PedidoStatusRequest pedidoStatusRequest = new PedidoStatusRequest("RECEBIDO");

        assertEquals("RECEBIDO", pedidoStatusRequest.getStatus());

        PedidoStatusRequest request = new PedidoStatusRequest();

        assertNull(request.getStatus());

        request.setStatus("CANCELADO");

        assertEquals("CANCELADO", request.getStatus());
    }

    @Test
    void testSetPedidoStatusRequest() {
        PedidoStatusRequest pedidoStatusRequest = new PedidoStatusRequest();
        pedidoStatusRequest.setStatus("CANCELADO");
        assertEquals("CANCELADO", pedidoStatusRequest.getStatus());
    }
}

