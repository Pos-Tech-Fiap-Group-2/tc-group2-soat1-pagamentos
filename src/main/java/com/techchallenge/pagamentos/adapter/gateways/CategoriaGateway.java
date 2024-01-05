package com.techchallenge.pagamentos.adapter.gateways;

import java.util.List;

import com.techchallenge.pagamentos.core.domain.entities.Categoria;



public interface CategoriaGateway {

	List<Categoria> buscarTodos();
	Categoria buscarPorId(Long id);
}
