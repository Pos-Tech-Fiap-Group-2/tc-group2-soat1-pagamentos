package com.techchallenge.pagamentos.adapter.external.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PaymentDTO;

@FeignClient(name = "payment-service", url = "${payment.api.url}")
public interface PaymentStatus {

    @GetMapping(value = "/v1/payments/status/{paymentId}", headers = {"Content-type : application/json"})
    public PaymentDTO consultarPagamento(@PathVariable Long paymentId, @RequestHeader("X-mock-status-payment") String paymentStatus);
    
    @PostMapping(value = "/v1/payments", headers = {"Content-type : application/json"})
    public PaymentDTO efetuarPagamentoViaPix(@RequestBody PagamentoPixDTO pagamentoPixDTO);
}
