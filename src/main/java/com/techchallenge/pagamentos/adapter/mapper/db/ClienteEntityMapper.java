package com.techchallenge.pagamentos.adapter.mapper.db;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.core.domain.entities.Cliente;
import com.techchallenge.pagamentos.drivers.db.entities.ClienteEntity;

@Component
public class ClienteEntityMapper {

	@Autowired
	private ModelMapper mapper;
	
	public ClienteEntity toModel(Cliente cliente) {
		return mapper.map(cliente, ClienteEntity.class);
	}
	
	public Collection<ClienteEntity> toCollectionModel(Collection<Cliente> clientes) {
		return clientes.stream()
				.map(c -> mapper.map(c, ClienteEntity.class))
				.collect(Collectors.toList());
	}
}
