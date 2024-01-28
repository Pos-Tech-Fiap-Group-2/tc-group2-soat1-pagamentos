package com.techchallenge.pagamentos.adapter.gateways.impl;

import java.util.List;

import com.techchallenge.pagamentos.adapter.external.producao.PedidoStatusRequest;
import com.techchallenge.pagamentos.adapter.external.producao.ProducaoAPI;
import com.techchallenge.pagamentos.adapter.mapper.business.PagamentoBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDTO;
import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDocumentoDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.adapter.external.mercadopago.MercadoPagoAPI;
import com.techchallenge.pagamentos.adapter.gateways.PagamentoGateway;
import com.techchallenge.pagamentos.adapter.mapper.business.TipoPagamentoBusinessMapper;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.StatusPagamento;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;
import com.techchallenge.pagamentos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoPKEntity;
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
	private PagamentoBusinessMapper pagamentoBusinessMapper;

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

		PagamentoEntity saved = pagamentoRepository.save(pagamentoEntity);

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
		pagamentoPixResponseDTO.setIdPagamento(saved.getIdPagamento());
		pagamentoEntity.setIdPagamentoExterno(pagamentoPixResponseDTO.getId());
		pagamentoRepository.save(pagamentoEntity);

		return pagamentoPixResponseDTO;
	}

	public PagamentoResponseDTO consultarPagamento(Long paymentId) {
		PagamentoEntity pagamentoEntity = pagamentoRepository.findByIdPagamentoExterno(paymentId);
		PagamentoResponseDTO pagamentoResponseDTO = mercadoPagoAPI.consultarPagamento(paymentId);

		if (pagamentoResponseDTO.getStatus().equals("approved")) {
			pagamentoEntity.setStatus(StatusPagamento.APROVADO);
			pagamentoRepository.save(pagamentoEntity);
			producaoAPI.adicionarPedidoFilaProducao(pagamentoEntity.getIdPedido().toString());
		} else {
			pagamentoEntity.setStatus(StatusPagamento.RECUSADO);
			pagamentoRepository.save(pagamentoEntity);
			PedidoStatusRequest request = new PedidoStatusRequest();
			request.setStatus("CANCELADO");
			request.setPedidoId(pagamentoEntity.getIdPedido());
			producaoAPI.atualizarStatusPedidoProducao(pagamentoEntity.getIdPedido().toString(), request);
		}

		return pagamentoResponseDTO;
	}

	public List<TipoPagamento> listar() {
		return businessMapper.toCollectionModel(tipoPagamentoRepository.findAll());
	}

	public Pagamento consultarStatusPagamento(Long pagamentoId, Long pedidoId) {
		PagamentoPKEntity pk = new PagamentoPKEntity();
		pk.setIdPagamento(pagamentoId);
		pk.setIdPedido(pedidoId);
		
		PagamentoEntity entity = pagamentoRepository.findById(pk).get();
		return pagamentoBusinessMapper.toModel(entity);

	}
}
