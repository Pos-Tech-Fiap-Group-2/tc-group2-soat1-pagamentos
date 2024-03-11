package com.techchallenge.pagamentos.adapter.external.producao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "producao-service", url = "${producao.api.url}")
public interface ProducaoAPI {
    @PostMapping(value = "/api/producao/{pedidoId}/adicionar", consumes = "application/json")
    public void adicionarPedidoFilaProducao(@PathVariable String pedidoId);

    @PutMapping(value = "/api/producao/{pedidoId}/status", consumes = "application/json")
    public void atualizarStatusPedidoProducao(@PathVariable String pedidoId, @RequestBody PedidoStatusRequest producaoRequest);
}

