package com.techchallenge.pagamentos.adapter.controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.driver.model.ItemPedidoModel;
import com.techchallenge.pagamentos.adapter.driver.model.PedidoModel;
import com.techchallenge.pagamentos.adapter.driver.model.input.ItemPedidoInput;
import com.techchallenge.pagamentos.adapter.mapper.api.ItemPedidoApiMapper;
import com.techchallenge.pagamentos.adapter.mapper.api.PedidoApiMapper;
import com.techchallenge.pagamentos.core.domain.entities.ItemPedido;
import com.techchallenge.pagamentos.core.domain.entities.Pedido;
import com.techchallenge.pagamentos.core.domain.entities.StatusPedido;
import com.techchallenge.pagamentos.core.domain.usecases.ItemPedidoUseCase;
import com.techchallenge.pagamentos.core.domain.usecases.PedidoUseCase;

@Component
public class PedidoController {

    @Autowired
    private PedidoUseCase useCase;
    
    @Autowired
    private ItemPedidoUseCase itemPedidoUseCase;
    
    @Autowired
    private PedidoApiMapper mapper;
    
    @Autowired
    private ItemPedidoApiMapper itemPedidoMapper;
    

    public PedidoModel buscarPedidosPorId(Long id) {
        Pedido pedido = useCase.buscarPedidoPorId(id);
        return mapper.toModel(pedido);
    }

    public Collection<PedidoModel> listarPedidos(StatusPedido status) {
    	List<Pedido> pedidos = useCase.buscarPedidosPorStatus(status);
    	return mapper.toCollectionModelOrderByStatus(pedidos);
    }
    
    public Collection<PedidoModel> listarPedidos() {
    	List<Pedido> pedidos = useCase.buscarPedidos();
    	return mapper.toCollectionModelOrderByStatus(pedidos);
    }

    public void atualizarStatusDoPedido(Long id, String novoStatus) {
        Pedido pedido = useCase.buscarPedidoPorId(id);
        StatusPedido statusPedido = StatusPedido.valueOf(novoStatus);
        useCase.atualizarStatusDoPedido(pedido, statusPedido);
    }
    
	public ItemPedidoModel adicionarProduto(Long id, ItemPedidoInput input) {
		ItemPedido itemPedido = itemPedidoMapper.toDomainObject(input);
		itemPedido = itemPedidoUseCase.adicionarItemAoPedido(id, itemPedido);

		return itemPedidoMapper.toModel(itemPedido);
	}
	
	public void atualizarProduto(Long id, ItemPedidoInput input) {
		ItemPedido itemPedido = itemPedidoMapper.toDomainObject(input);
		itemPedidoUseCase.atualizarItemAoPedido(id, itemPedido);
	}
	
	public void remover(Long id, ItemPedidoInput input) {
		itemPedidoUseCase.excluirItemAoPedido(id, input.getProdutoId());
	}

    public String buscarStatusDePagamentoDoPedido(Long id) {
        Pedido pedido = useCase.buscarPedidoPorId(id);
        return mapper.toPagamentoStatus(pedido);
    }
}
