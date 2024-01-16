package com.techchallenge.pagamentos.adapter.external.producao;

import feign.form.FormProperty;

public class PedidoStatusRequest {
    @FormProperty("status")
    private String status;

    public PedidoStatusRequest(String status) {
        this.status = status;
    }

    public PedidoStatusRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
