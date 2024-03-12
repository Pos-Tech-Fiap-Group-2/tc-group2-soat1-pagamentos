package com.techchallenge.pagamentos.core.domain.entities.messaging;

public class Cliente {

    private String nome;
    private String email;
    private Pedido pedido;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@Override
	public String toString() {
		return "Cliente [nome=" + nome + ", email=" + email + ", pedido=" + pedido + "]";
	}
}