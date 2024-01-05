package com.techchallenge.pagamentos.adapter.gateways.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.gateways.CategoriaGateway;
import com.techchallenge.pagamentos.adapter.mapper.business.CategoriaBusinessMapper;
import com.techchallenge.pagamentos.core.domain.entities.Categoria;
import com.techchallenge.pagamentos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pagamentos.drivers.db.entities.CategoriaEntity;
import com.techchallenge.pagamentos.drivers.db.repositories.CategoriaRepository;

@Component
public class CategoriaGatewayImpl implements CategoriaGateway {
	
	private static final String MSG_CATEGORIA_NAO_EXISTE = "Não existe uma categoria com código %d";

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private CategoriaBusinessMapper mapper;

    public List<Categoria> buscarTodos() {
    	List<CategoriaEntity> entities = repository.findAll();
    	return mapper.toCollectionModel(entities);
    }

	@Override
	public Categoria buscarPorId(Long id) {
		Optional<CategoriaEntity> optional = repository.findById(id);
		CategoriaEntity entity = optional.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format(MSG_CATEGORIA_NAO_EXISTE, id)));
		
		return mapper.toModel(entity);
	}
}
