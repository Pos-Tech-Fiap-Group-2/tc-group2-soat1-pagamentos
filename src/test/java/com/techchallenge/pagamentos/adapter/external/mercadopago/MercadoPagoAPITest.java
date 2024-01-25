package com.techchallenge.pagamentos.adapter.external.mercadopago;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDTO;
import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDocumentoDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.util.ResourceUtil;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
@TestPropertySource(locations = "classpath:application-mercado-pago-test.properties")
public class MercadoPagoAPITest {
    @Autowired
    private MercadoPagoAPI mercadoPagoAPI;

    @Value("${mercadopago.access_token}")
    private String mercadoPagoAccessToken;

    @Value("${mercadopago.notification_url}")
    private String mercadoPagoNotificationUrl;

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    public void setup() throws MPException, MPApiException {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void efetuarPagamentoViaPixTest() throws MPApiException, MPException {
        String content = ResourceUtil.getContentFromResource(
                "/json/wiremock/create-payment-response.json");

        System.out.println("Content from resource: " + content);

        stubFor(post(urlEqualTo("/v1/payments"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("/json/wiremock/create-payment-response.json")));

        // Arrange
        ClienteDocumentoDTO clienteDocumentoDTO = new ClienteDocumentoDTO();
        clienteDocumentoDTO.setNumero("123456789");
        clienteDocumentoDTO.setTipo("CPF");

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNome("Teste");
        clienteDTO.setEmail("teste@teste.com");
        clienteDTO.setDocumento(clienteDocumentoDTO);

        PagamentoPixDTO pagamentoPixDTO = new PagamentoPixDTO();
        pagamentoPixDTO.setCliente(clienteDTO);
        pagamentoPixDTO.setDescricao("Descricao");
        pagamentoPixDTO.setTotal(BigDecimal.valueOf(50));

        // Act
        PagamentoPixResponseDTO responseDTO = mercadoPagoAPI.efetuarPagamentoViaPix(pagamentoPixDTO);

        // Assert
        assertNotNull(responseDTO);
    }


    @Test
    void consultar() throws MPApiException, MPException {
        String content = ResourceUtil.getContentFromResource(
                "/json/wiremock/create-payment-response.json");

        System.out.println("Content from resource: " + content);

        stubFor(get(urlEqualTo("pagamentos/mercadopago/notifications"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile(content)));

        // Act
        PagamentoResponseDTO responseDTO = mercadoPagoAPI.consultarPagamento(123L);

        // Assert
        assertNotNull(responseDTO);
    }
}


