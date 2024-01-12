package com.techchallenge.pagamentos.adapter.external.producao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "http://localhost:8080")
public interface ProducaoAPI {
    @PostMapping(value = "/api/producao/{pedidoId}/adicionar")
    public void adicionarPedidoFilaProducao(@PathVariable String pedidoId);

    @PutMapping(value = "/api/producao/{pedidoId}/status")
    public void atualizarStatusPedidoProducao(@PathVariable String pedidoId, @RequestBody PedidoStatusRequest producaoRequest);
}

