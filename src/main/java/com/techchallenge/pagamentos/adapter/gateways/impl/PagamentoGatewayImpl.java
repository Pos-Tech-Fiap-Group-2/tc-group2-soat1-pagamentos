package com.techchallenge.pagamentos.adapter.gateways.impl;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDTO;
import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDocumentoDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PaymentDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PedidoDTO;
import com.techchallenge.pagamentos.adapter.external.payment.PaymentStatus;
import com.techchallenge.pagamentos.adapter.external.pedido.PedidoAPI;
import com.techchallenge.pagamentos.adapter.external.producao.PedidoStatusRequest;
import com.techchallenge.pagamentos.adapter.gateways.PagamentoGateway;
import com.techchallenge.pagamentos.adapter.mapper.business.PagamentoBusinessMapper;
import com.techchallenge.pagamentos.adapter.mapper.business.TipoPagamentoBusinessMapper;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.StatusPagamento;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.messaging.Mensagem;
import com.techchallenge.pagamentos.core.domain.entities.messaging.Pedido;
import com.techchallenge.pagamentos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoPKEntity;
import com.techchallenge.pagamentos.drivers.db.entities.TipoPagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.repositories.PagamentoRepository;
import com.techchallenge.pagamentos.drivers.db.repositories.TipoPagamentoRepository;
import com.techchallenge.pagamentos.drivers.producers.cliente.NotificacaoClienteProducer;
import com.techchallenge.pagamentos.drivers.producers.pagamento.ProducaoPedidoInclusaoProducer;

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
	private PedidoAPI pedidoAPI;
	
	@Autowired
	private PaymentStatus paymentAPI;
	
	@Autowired
	private NotificacaoClienteProducer clienteProducer;
	
	@Autowired
	private ProducaoPedidoInclusaoProducer pedidoInclusaoProducer;
	
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

		PaymentDTO paymentDto = paymentAPI.efetuarPagamentoViaPix(pagamentoPixDTO);
		PagamentoPixResponseDTO pagamentoPixResponseDTO = new PagamentoPixResponseDTO(
				paymentDto.getId(),
                String.valueOf(paymentDto.getStatus()),
                paymentDto.getStatusDetail(),
                paymentDto.getPaymentMethodId(),
                paymentDto.getPointOfInteraction().getTransactionData().getQrCodeBase64(),
                paymentDto.getPointOfInteraction().getTransactionData().getQrCode());
		pagamentoPixResponseDTO.setIdPagamento(saved.getIdPagamento());
		
		pagamentoEntity.setIdPagamentoExterno(pagamentoPixResponseDTO.getId());
		pagamentoRepository.save(pagamentoEntity);

		return pagamentoPixResponseDTO;
	}

	public PagamentoResponseDTO consultarPagamento(EventoPagamento evento, String paymentStatus) {
		Long paymentId = evento.getData().getId();
		Long pedidoId = evento.getData().getPedidoId();
		
		// Consultas de dados
		PagamentoEntity pagamentoEntity = pagamentoRepository.findByIdPagamentoExterno(paymentId);
		PaymentDTO payment = paymentAPI.consultarPagamento(paymentId, paymentStatus);
		PedidoDTO pedido = pedidoAPI.buscarDadosPedido(pedidoId);
		
		PagamentoResponseDTO paymentDto = new PagamentoResponseDTO(
        		payment.getId(),
                String.valueOf(payment.getStatus()),
                payment.getStatusDetail(),
                payment.getPaymentMethodId());
		
		StatusPagamento statusPagamento = StatusPagamento.getStatusPagamentoByStatus(paymentDto.getStatus());
		
		// Atualização da entidade de pagamento.
		pagamentoEntity.setStatus(statusPagamento);
		pagamentoRepository.save(pagamentoEntity);
		
		// Gera mensagem para API de produção somente se status do pagamento foi aprovado.
		if (statusPagamento == StatusPagamento.APROVADO) {
			PedidoStatusRequest request = new PedidoStatusRequest();
			request.setPedidoId(pagamentoEntity.getIdPedido());
			
			pedidoInclusaoProducer.enviar(request);
		}
		
		enviarNotificacaoCliente(pagamentoEntity, pedido, statusPagamento);
		
		return paymentDto;
	}

	public List<TipoPagamento> listar() {
		return businessMapper.toCollectionModel(tipoPagamentoRepository.findAll());
	}

	public Pagamento consultarStatusPagamento(Long pagamentoId, Long pedidoId) {
		PagamentoPKEntity pk = new PagamentoPKEntity();
		pk.setIdPagamento(pagamentoId);
		pk.setIdPedido(pedidoId);
		
		PagamentoEntity entity = pagamentoRepository.findById(pk).orElseThrow();
		return pagamentoBusinessMapper.toModel(entity);

	}
	
	private void enviarNotificacaoCliente(PagamentoEntity pagamentoEntity, PedidoDTO pedido,
			StatusPagamento statusPagamento) {
		Mensagem mensagem = new Mensagem();
		mensagem.setCliente(new com.techchallenge.pagamentos.core.domain.entities.messaging.Cliente());
		mensagem.getCliente().setEmail(pedido.getCliente().getEmail());
		mensagem.getCliente().setNome(pedido.getCliente().getNome());
		mensagem.getCliente().setPedido(new Pedido());
		mensagem.getCliente().getPedido().setDataSolicitacao(pedido.getDataSolicitacao());
		mensagem.getCliente().getPedido().setId(pagamentoEntity.getIdPedido());
		mensagem.getCliente().getPedido().setStatus(pedido.getStatus());
		mensagem.getCliente().getPedido().setValor(pagamentoEntity.getValor());
		mensagem.setTemplate(statusPagamento.getDescricao());
		mensagem.setTimestamp(OffsetDateTime.now());
		
		clienteProducer.enviar(mensagem);
	}
}
