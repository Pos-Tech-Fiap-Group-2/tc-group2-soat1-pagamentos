package com.techchallenge.pagamentos.adapter.driver.model.input;

public class EventoPagamentoInput {

	private Data data;

	public Data getData() {
		if (data == null) {
			this.data = new Data();
		}
		
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data {
		private Long id;
		private Long pedidoId;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getPedidoId() {
			return pedidoId;
		}

		public void setPedidoId(Long pedidoId) {
			this.pedidoId = pedidoId;
		}
	}
}
