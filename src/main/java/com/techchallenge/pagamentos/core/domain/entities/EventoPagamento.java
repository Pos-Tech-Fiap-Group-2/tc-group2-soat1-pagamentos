package com.techchallenge.pagamentos.core.domain.entities;

public class EventoPagamento {

    private Data data;

    public Data getData() { return data; }

    public void setData(Data data) { this.data = data; }

    public static class Data {
        private Long id;
        private Long pedidoId;

        public Long getId() { return id; }

        public void setId(Long id) { this.id = id; }

		public Long getPedidoId() {
			return pedidoId;
		}

		public void setPedidoId(Long pedidoId) {
			this.pedidoId = pedidoId;
		}
    }
}
