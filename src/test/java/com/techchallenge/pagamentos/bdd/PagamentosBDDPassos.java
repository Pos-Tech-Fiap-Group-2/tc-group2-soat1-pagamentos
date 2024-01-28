package com.techchallenge.pagamentos.bdd;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.techchallenge.pagamentos.adapter.driver.model.input.ClienteInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.EventoPagamentoInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.PagamentoInput;
import com.techchallenge.pagamentos.util.CpfUtil;
import com.techchallenge.pagamentos.util.ResourceUtil;

import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class PagamentosBDDPassos {

    public Response response;

    public static String ENDPOINT_PAGAMENTOS;
    
    private static String ENDPOINT_PRODUCAO;
    
    private Long pedidoId;
    
    private Long idPagamento;

    static {
        Properties prop = new Properties();
        InputStream is = PagamentosBDDPassos.class.getResourceAsStream("/bdd-config.properties");

        try {
            prop.load(is);
            ENDPOINT_PAGAMENTOS = prop.getProperty("bdd.pagamentos.endpoint.url");
            ENDPOINT_PRODUCAO = prop.getProperty("bdd.producao.endpoint.url");

        } catch (IOException e) {

        }
    }
    
    private PagamentoInput createPagamentoInput(Long tipoPagamentoId, Long pedidoId, BigDecimal valor, ClienteInput clienteInput) {
    	PagamentoInput input = new PagamentoInput();
    	
    	input.setCliente(clienteInput);
    	input.setPedidoId(pedidoId);
    	input.setTipoPagamentoId(tipoPagamentoId);
    	input.setValor(valor);
    	
    	return input;
    }
    
    private ClienteInput createClienteInput(Long cpf, String email, String nome) {
    	ClienteInput input = new ClienteInput();
    	
    	input.setCpf(cpf);
    	input.setEmail(email);
    	input.setNome(nome);
    	
    	return input;
    }
    
    private EventoPagamentoInput createEventoPagamentoInput(Long eventoId) {
    	EventoPagamentoInput input = new EventoPagamentoInput();
    	
    	input.getData().setId(eventoId);
    	
    	return input;
    }
    
    private Long createPedidoId() {
    	return new Random().nextLong(1L, 999L);
    }

    @Quando("realizar pagamento")
    public void realizarPagamento() {
        String content = ResourceUtil.getContentFromResource(
                "/json/correto/pagamento-input.json");
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(content)
                .when()
                .post(ENDPOINT_PAGAMENTOS);
    }

    @Entao("o pagamento é efetuado com sucesso")
    public void pagamentoEfetuadoComSucesso() {
        response.then().statusCode(HttpStatus.OK.value());
    }

    @Quando("solicitar registros de tipos de pagamentos na plataforma")
    public void solicitarListaDeTiposDePagamentos() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT_PAGAMENTOS + "/tipos-pagamento");
    }

    @Então("os tipos de pagamentos são exibidos com sucesso")
    public void tiposDePagamentosExibidosComSucesso() {
        response.then().statusCode(HttpStatus.OK.value());
    }

    @Quando("receber evento de pagamento aprovado ou recusado")
    public void receberEventoPagamento() {
    	
    	Long pedidoId = createPedidoId();
    	Long cpf = CpfUtil.cpf();
    	
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("pedidoId", pedidoId)
                .when()
                .post(ENDPOINT_PRODUCAO + "/{pedidoId}/adicionar");
    	
    	ClienteInput clienteInput = createClienteInput(cpf, "teste"+cpf+"@teste.com.br", "Nome Teste " + cpf);
    	PagamentoInput pagamentoInput = createPagamentoInput(1L, pedidoId, new BigDecimal("29.99"), clienteInput);
    	
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pagamentoInput)
                .when()
                .post(ENDPOINT_PAGAMENTOS);
        
		String idPagamentoValue = response.getBody().jsonPath().getJsonObject("idPagamento").toString();
		idPagamento = Long.valueOf(idPagamentoValue);
        
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		Long paymentId = Long.valueOf(idValue);
        
        EventoPagamentoInput eventoInput = createEventoPagamentoInput(paymentId);
        
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(eventoInput)
                .when()
                .post(ENDPOINT_PAGAMENTOS + "/mercadopago/notifications");
    }

    @Então("deve confirmar pagamento")
    public void pedidoExibidoComSucesso() {
        response.then().statusCode(HttpStatus.OK.value());
    }

    @Quando("requisitar a consulta do status do pagamento")
    public void requisitarStatusPagamento() {
    	Long pedidoId = createPedidoId();
    	Long cpf = CpfUtil.cpf();
    	
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("pedidoId", pedidoId)
                .when()
                .post(ENDPOINT_PRODUCAO + "/{pedidoId}/adicionar");
    	
        ClienteInput clienteInput = createClienteInput(cpf, "teste"+cpf+"@teste.com.br", "Nome Teste " + cpf);
    	PagamentoInput pagamentoInput = createPagamentoInput(1L, pedidoId, new BigDecimal("29.99"), clienteInput);
    	
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pagamentoInput)
                .when()
                .post(ENDPOINT_PAGAMENTOS);
        
		String idPagamentoValue = response.getBody().jsonPath().getJsonObject("idPagamento").toString();
		idPagamento = Long.valueOf(idPagamentoValue);
		
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("pagamentoId", idPagamento)
                .pathParam("pedidoId", pedidoId)
                .when()
                .get(ENDPOINT_PAGAMENTOS + "/status/pagamentos/{pagamentoId}/pedidos/{pedidoId}");
    }

    @Então("o status do pagamento deve ser exibido com sucesso")
    public void statusPagamentoExibidoComSucesso() {
        response.then().statusCode(HttpStatus.OK.value());
    }

}
