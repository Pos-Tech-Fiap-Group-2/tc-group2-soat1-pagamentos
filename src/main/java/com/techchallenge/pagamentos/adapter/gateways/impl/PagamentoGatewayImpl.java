package com.techchallenge.pagamentos.adapter.gateways.impl;

import java.util.List;

import com.techchallenge.pagamentos.adapter.external.producao.ProducaoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDTO;
import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDocumentoDTO;
//import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDTO;
//import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDocumentoDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.adapter.external.mercadopago.MercadoPagoAPI;
import com.techchallenge.pagamentos.adapter.gateways.PagamentoGateway;
//import com.techchallenge.pagamentos.adapter.gateways.PedidoGateway;
import com.techchallenge.pagamentos.adapter.mapper.business.TipoPagamentoBusinessMapper;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.StatusPagamento;
//import com.techchallenge.pagamentos.core.domain.entities.StatusPedido;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;
import com.techchallenge.pagamentos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.entities.TipoPagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.repositories.PagamentoRepository;
import com.techchallenge.pagamentos.drivers.db.repositories.TipoPagamentoRepository;

@Component
public class PagamentoGatewayImpl implements PagamentoGateway {
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private TipoPagamentoRepository tipoPagamentoRepository;
	@Autowired
	private TipoPagamentoBusinessMapper businessMapper;

	@Autowired
	private MercadoPagoAPI mercadoPagoAPI;

	@Autowired
	private ProducaoAPI producaoAPI;
	
	public PagamentoPixResponseDTO efetuarPagamento(Pagamento pagamento) {
		Long id = pagamento.getTipoPagamentoId();
		
		TipoPagamentoEntity entity = tipoPagamentoRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException(
                String.format("Não existe um cadastro de tipo de pagamento com código %d", id)));
		
		PagamentoEntity pagamentoEntity = new PagamentoEntity();
		pagamentoEntity.setTipoPagamento(entity);
		pagamentoEntity.setStatus(StatusPagamento.PROCESSAMENTO);
		pagamentoEntity.setIdPedido(pagamento.getPedidoId());
		pagamentoEntity.setValor(pagamento.getValor());
		
		pagamentoRepository.save(pagamentoEntity);
		
		// FIXME: A API de pedido deverá manter o estado atualizado do pedido.
//		pedidoGateway.atualizarTipoPagamento(pedidoId, businessMapper.toModel(entity));
//
//		Pedido pedido = pedidoGateway.buscarPedidoPorId(pedidoId);
//		
//		pedido.setStatusPagamento(StatusPagamento.PROCESSAMENTO);
//		pedidoGateway.atualizarStatusPagamento(id, StatusPagamento.PROCESSAMENTO);
		
		ClienteDocumentoDTO clienteDocumentoDTO = new ClienteDocumentoDTO();
		clienteDocumentoDTO.setTipo("CPF");
		clienteDocumentoDTO.setNumero(pagamento.getCliente().getCpf().toString());

		ClienteDTO clienteDTO = new ClienteDTO();
		clienteDTO.setDocumento(clienteDocumentoDTO);
		clienteDTO.setEmail(pagamento.getCliente().getEmail());
		clienteDTO.setNome(pagamento.getCliente().getNome());

		PagamentoPixDTO pagamentoPixDTO = new PagamentoPixDTO();
		pagamentoPixDTO.setCliente(clienteDTO);
		pagamentoPixDTO.setTotal(pagamento.getValor());
		pagamentoPixDTO.setDescricao("Pagamento do pedido " + pagamento.getPedidoId());

		PagamentoPixResponseDTO pagamentoPixResponseDTO = mercadoPagoAPI.efetuarPagamentoViaPix(pagamentoPixDTO);

		// FIXME: Verificar a melhor forma de tratar o id de pagamento externo nesse modelo.
//		pedidoGateway.atualizarPaymentId(pedidoId, pagamentoPixResponseDTO.getId());

		// endpoint de adicionar a fila
		producaoAPI.adicionarPedidoFilaProducao(pagamento.getPedidoId().toString());

		//endpoint de atualizar status do pedido

		return pagamentoPixResponseDTO;
	}
	
	public PagamentoResponseDTO consultarPagamento(Long paymentId) {
		PagamentoResponseDTO pagamentoResponseDTO= mercadoPagoAPI.consultarPagamento(paymentId);

		if (pagamentoResponseDTO.getStatus().equals("approved")) {
			// FIXME: Atualização do estado do pedido com base na condição de pagamento deverá ocorrer pela API de pedidos.
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
