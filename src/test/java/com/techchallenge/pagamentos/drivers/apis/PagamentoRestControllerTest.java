package com.techchallenge.pagamentos.drivers.apis;

import com.google.gson.Gson;
import com.techchallenge.pagamentos.adapter.controllers.PagamentoController;
import com.techchallenge.pagamentos.adapter.driver.exceptionhandler.ApiExceptionHandler;
import com.techchallenge.pagamentos.adapter.driver.model.TipoPagamentoModel;
import com.techchallenge.pagamentos.adapter.driver.model.input.ClienteInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.EventoPagamentoInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.PagamentoInput;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.core.domain.entities.Cliente;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.util.ResourceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PagamentoRestControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    PagamentoRestController pagamentoRestController;

    @Mock
    PagamentoController controller;

    private ApiExceptionHandler createHandler() {

        // Como não estamos subindo o contexto do Spring boot, o MessageSource fica com referência nula
        // por estarmos instanciando diretamente o Handler.
        ApiExceptionHandler handler = new ApiExceptionHandler();
        return handler;
    }

    @BeforeEach
    private void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        // Handler mock.
        ApiExceptionHandler handler = createHandler();

        mockMvc = MockMvcBuilders.standaloneSetup(pagamentoRestController).setControllerAdvice(handler)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*").build();
    }

    @Test
    void dadoUmPagamentoQuandoRealizarPagamentoEntaoDeveRetornar200() throws Exception {
        String content = ResourceUtil.getContentFromResource(
                "/json/correto/pagamento-input.json");

        PagamentoInput pagamentoInput = new PagamentoInput();
        ClienteInput clienteInput = new ClienteInput();
        clienteInput.setCpf(123456789L);
        clienteInput.setNome("Teste");
        clienteInput.setEmail("teste@teste.com");

        pagamentoInput.setCliente(clienteInput);
        pagamentoInput.setPedidoId(1L);
        pagamentoInput.setValor(BigDecimal.valueOf(10));
        pagamentoInput.setTipoPagamentoId(1L);

        PagamentoPixResponseDTO pagamentoPixResponseDTO = new PagamentoPixResponseDTO(123L, "Approved", "Detalhes",
                "Pix", "qrCodeBase64", "qrCode");


        when(controller.realizarPagamento(pagamentoInput)).thenReturn(pagamentoPixResponseDTO);

        mockMvc.perform(post("/pagamentos").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk());

        verify(controller, times(1)).realizarPagamento(any(PagamentoInput.class));
    }


    @Test
    void listarRetorna200() throws Exception {
        TipoPagamentoModel model1 = new TipoPagamentoModel();
        model1.setId(1L);
        model1.setNome("QR Code");

        TipoPagamentoModel model2 = new TipoPagamentoModel();
        model2.setId(1L);
        model2.setNome("Cartão de Crédito");

        List<TipoPagamentoModel> tipoPagamentoModels = Arrays.asList(model1, model2);

        when(controller.listar()).thenReturn(tipoPagamentoModels);

        MvcResult result = mockMvc.perform(get("/pagamentos/tipos-pagamento")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        List<?> lista = new Gson().fromJson(content, List.class);
        assertTrue(lista.size() > 0);
    }

    @Test
    void confirmarPagamentoRetorna200() throws Exception {
        String content = ResourceUtil.getContentFromResource(
                "/json/correto/evento-pagamento-input.json");

        EventoPagamentoInput eventoPagamentoInput = new EventoPagamentoInput();
        EventoPagamentoInput.Data data = new EventoPagamentoInput.Data();
        data.setId(123L);
        eventoPagamentoInput.setData(data);

        doNothing().when(controller).confirmarPagamento(eventoPagamentoInput);

        mockMvc.perform(post("/pagamentos/mercadopago/notifications").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk());

        verify(controller, times(1)).confirmarPagamento(any(EventoPagamentoInput.class));

    }

    @Test
    void consultarStatusPagamentoRetorna200() throws Exception {
        when(controller.consultarStatusPagamento(1L, 123L)).thenReturn("APROVADO");

        mockMvc.perform(get("/pagamentos/status/pagamentos/1/pedidos/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("APROVADO"));
    }


    @Test
    void dadoStatus_quandoConsultarComMediaTypeIncorreto_entaoStatus400() throws Exception {
        mockMvc.perform(put("/pagamentos/status/pedidos/123").contentType(MediaType.APPLICATION_XML).content(""))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void dadoIdDoPedido_quandoConsultarStatus_entaoStatus500() throws Exception {
        when(controller.consultarStatusPagamento(1L, 1L))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/pagamentos/status/pagamentos/1/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void dadoPedido_quandoEfetuarPagamentoComRequestIncorreto_entaoStatus400() throws Exception {

        String content = ResourceUtil.getContentFromResource(
                "/json/incorreto/pagamento-input-sem-valor.json");

        mockMvc.perform(post("/pagamentos").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().is4xxClientError());
    }
}
