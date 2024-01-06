package com.techchallenge.pagamentos.adapter.gateways.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDTO;
//import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDocumentoDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.adapter.external.mercadopago.MercadoPagoAPI;
import com.techchallenge.pagamentos.adapter.gateways.PagamentoGateway;
//import com.techchallenge.pagamentos.adapter.gateways.PedidoGateway;
import com.techchallenge.pagamentos.adapter.mapper.business.TipoPagamentoBusinessMapper;
//import com.techchallenge.pagamentos.core.domain.entities.Pedido;
import com.techchallenge.pagamentos.core.domain.entities.StatusPagamento;
//import com.techchallenge.pagamentos.core.domain.entities.StatusPedido;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;
import com.techchallenge.pagamentos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pagamentos.drivers.db.entities.TipoPagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.repositories.TipoPagamentoRepository;

@Component
public class PagamentoGatewayImpl implements PagamentoGateway {
	
//	@Autowired
//	private PedidoGateway pedidoGateway;
	@Autowired
	private TipoPagamentoRepository tipoPagamentoRepository;
	@Autowired
	private TipoPagamentoBusinessMapper businessMapper;

	@Autowired
	private MercadoPagoAPI mercadoPagoAPI;
	
	public PagamentoPixResponseDTO efetuarPagamento(Long pedidoId, TipoPagamento tipoPagamento) {
		Long id = tipoPagamento.getId();
		
		TipoPagamentoEntity entity = tipoPagamentoRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException(
                String.format("Não existe um cadastro de tipo de pagamento com código %d", id)));
		
//		pedidoGateway.atualizarTipoPagamento(pedidoId, businessMapper.toModel(entity));
//
//		Pedido pedido = pedidoGateway.buscarPedidoPorId(pedidoId);
//		
//		pedido.setStatusPagamento(StatusPagamento.PROCESSAMENTO);
//		pedidoGateway.atualizarStatusPagamento(id, StatusPagamento.PROCESSAMENTO);
		
//		ClienteDocumentoDTO clienteDocumentoDTO = new ClienteDocumentoDTO();
//		clienteDocumentoDTO.setTipo("CPF");
//		clienteDocumentoDTO.setNumero(pedido.getCliente().getCpf().toString());

//		ClienteDTO clienteDTO = new ClienteDTO();
//		clienteDTO.setDocumento(clienteDocumentoDTO);
//		clienteDTO.setEmail(pedido.getCliente().getEmail());
//		clienteDTO.setNome(pedido.getCliente().getNome());

		PagamentoPixDTO pagamentoPixDTO = new PagamentoPixDTO();
//		pagamentoPixDTO.setCliente(clienteDTO);
//		pagamentoPixDTO.setTotal(pedido.getValor());
//		pagamentoPixDTO.setDescricao("Pagamento do pedido " + pedido.getId());

		PagamentoPixResponseDTO pagamentoPixResponseDTO = mercadoPagoAPI.efetuarPagamentoViaPix(pagamentoPixDTO);

//		pedidoGateway.atualizarPaymentId(pedidoId, pagamentoPixResponseDTO.getId());

		return pagamentoPixResponseDTO;
	}
	
	public PagamentoResponseDTO consultarPagamento(Long paymentId) {
		PagamentoResponseDTO pagamentoResponseDTO= mercadoPagoAPI.consultarPagamento(paymentId);

		if (pagamentoResponseDTO.getStatus().equals("approved")) {
//			Pedido pedido = pedidoGateway.buscarPedidoPorPaymentId(paymentId);

//			pedidoGateway.atualizarStatusPagamento(pedido.getId(), StatusPagamento.APROVADO);
//			pedidoGateway.atualizarStatusDoPedido(pedido, StatusPedido.PREPARACAO);
		}

		return pagamentoResponseDTO;
	}
	
	public List<TipoPagamento> listar() {
		return businessMapper.toCollectionModel(tipoPagamentoRepository.findAll());
	}
}
