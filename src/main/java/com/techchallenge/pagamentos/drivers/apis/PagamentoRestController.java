package com.techchallenge.pagamentos.drivers.apis;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.techchallenge.pagamentos.adapter.controllers.PagamentoController;
import com.techchallenge.pagamentos.adapter.driver.model.TipoPagamentoModel;
import com.techchallenge.pagamentos.adapter.driver.model.input.EventoPagamentoInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.PagamentoInput;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Pagamentos")
@RestController
@RequestMapping(value = "/pagamentos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PagamentoRestController {
	
    @Autowired
    private PagamentoController controller;
    
	@ApiOperation("Efetuar pagamento do pedido na plataforma")
	@ApiResponses({ 
			@ApiResponse(code = 201, message = "Pagamento registrado com sucesso"),
			@ApiResponse(code = 404, message = "Caso o pedido ou pagamento com o ID informado não exista")
			})
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public PagamentoPixResponseDTO realizarPagamento(@RequestBody PagamentoInput pagamentoInput) {
		return controller.realizarPagamento(pagamentoInput);
	}
	
	@ApiOperation("Consultar tipos de pagamentos aceitos na plataforma")
	@ApiResponses({ 
			@ApiResponse(code = 201, message = "Consulta efetuada com sucesso"),
			@ApiResponse(code = 404, message = "Caso não exista registros na plataforma")
			})
	@GetMapping("/tipos-pagamento")
	@ResponseStatus(HttpStatus.OK)
	public Collection<TipoPagamentoModel> listar() {
		return controller.listar();
	}

	@ApiOperation("Webhook para receber confirmação do pagamento aprovado ou pagamento recusado")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Evento de confirmação do pagamento aprovado ou pagamento recusado recebido com sucesso"),
			@ApiResponse(code = 404, message = "Caso o pedido ou pagamento com o ID informado não exista")
	})
	@PostMapping("/mercadopago/notifications")
	@ResponseStatus(HttpStatus.OK)
	public void confirmarPagamento(@RequestBody EventoPagamentoInput eventoPagamentoInput) {
		controller.confirmarPagamento(eventoPagamentoInput);
	}

	@ApiOperation("Consultar status do pagamento")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Consulta efetuada com sucesso"),
			@ApiResponse(code = 404, message = "Caso não exista registros na plataforma")
	})
	@GetMapping("/status/pedidos/{pedidoId}")
	@ResponseStatus(HttpStatus.OK)
	public String consultarStatusPagamento(@PathVariable Long pedidoId) {
		return controller.consultarStatusPagamento(pedidoId);
	}
}