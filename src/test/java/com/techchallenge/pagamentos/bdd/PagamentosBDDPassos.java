package com.techchallenge.pagamentos.bdd;

import com.techchallenge.pagamentos.util.ResourceUtil;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class PagamentosBDDPassos {

    private Response response;

    private static String ENDPOINT_PAGAMENTOS;

    static {
        Properties prop = new Properties();
        InputStream is = PagamentosBDDPassos.class.getResourceAsStream("/bdd-config.properties");

        try {
            prop.load(is);
            ENDPOINT_PAGAMENTOS = prop.getProperty("bdd.endpoint.url");

        } catch (IOException e) {

        }
    }

    @Quando("Realizar pagamento")
    public void realizarPagamento() {
        String content = ResourceUtil.getContentFromResource(
                "/json/correto/pagamento-input.json");
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(content)
                .when()
                .post(ENDPOINT_PAGAMENTOS);
    }

    @Então("o pagamento é efetuado com sucesso")
    public void pagamentoEfetuadoComSucesso() {
        response.then().statusCode(HttpStatus.CREATED.value());
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

    @Dado("um evento de pagamento aprovado ou recusado")
    public void eventoPagamentoAprovadoOuRecusado() {
        String content = ResourceUtil.getContentFromResource(
                "/json/correto/evento-pagamento-input.json");
        receberEventoPagamento(content);
    }

    @Quando("receber evento de pagamento aprovado ou recusado")
    public void receberEventoPagamento(String input) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(input)
                .when()
                .post(ENDPOINT_PAGAMENTOS);
    }

    @Então("deve confirmar pagamento")
    public void pedidoExibidoComSucesso() {
        response.then().statusCode(HttpStatus.OK.value());
    }

    @Quando("requisitar a consulta do status do pagamento")
    public void requisitarStatusPagamento() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("pedidoId", "1")
                .when()
                .get(ENDPOINT_PAGAMENTOS + "/status/pedidos/{pedidoId}");
    }

    @Então("o status do pagamento deve ser exibido com sucesso")
    public void statusPagamentoExibidoComSucesso() {
        response.then().statusCode(HttpStatus.OK.value());
    }

}
