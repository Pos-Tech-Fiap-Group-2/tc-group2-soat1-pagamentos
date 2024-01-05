package com.techchallenge.pagamentos.adapter.controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.driver.model.CategoriaModel;
import com.techchallenge.pagamentos.adapter.mapper.api.CategoriaApiMapper;
import com.techchallenge.pagamentos.core.domain.entities.Categoria;
import com.techchallenge.pagamentos.core.domain.usecases.CategoriaUseCase;

@Component
public class CategoriaController {

	@Autowired
	private CategoriaUseCase useCase;

    @Autowired
    private CategoriaApiMapper mapper;
	
    public Collection<CategoriaModel> listar() {
        List<Categoria> todasCategorias = useCase.buscarTodos();
        return mapper.toCollectionModel(todasCategorias);
    }
}
