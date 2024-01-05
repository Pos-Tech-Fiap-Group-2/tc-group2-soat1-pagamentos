package com.techchallenge.pagamentos.adapter.mapper.business;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.core.domain.entities.Pedido;
import com.techchallenge.pagamentos.drivers.db.entities.PedidoEntity;

@Component
public class PedidoBusinessMapper {

	@Autowired
	private ModelMapper mapper;
	
	public Pedido toModel(PedidoEntity entity) {
		return mapper.map(entity, Pedido.class);
	}
	
	public List<Pedido> toCollectionModel(Collection<PedidoEntity> pedidos) {
		return pedidos.stream()
				.map(c -> mapper.map(c, Pedido.class))
				.collect(Collectors.toList());
	}
}
