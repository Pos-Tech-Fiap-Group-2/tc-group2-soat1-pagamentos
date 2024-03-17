package com.techchallenge.pagamentos.adapter.gateways;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PaymentDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PedidoDTO;
import com.techchallenge.pagamentos.adapter.external.mercadopago.MercadoPagoAPI;
import com.techchallenge.pagamentos.adapter.external.payment.PaymentStatus;
import com.techchallenge.pagamentos.adapter.external.pedido.PedidoAPI;
import com.techchallenge.pagamentos.adapter.external.producao.ProducaoAPI;
import com.techchallenge.pagamentos.adapter.gateways.impl.PagamentoGatewayImpl;
import com.techchallenge.pagamentos.adapter.mapper.business.PagamentoBusinessMapper;
import com.techchallenge.pagamentos.adapter.mapper.business.TipoPagamentoBusinessMapper;
import com.techchallenge.pagamentos.core.domain.entities.Cliente;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.EventoPagamento.Data;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.StatusPagamento;
import com.techchallenge.pagamentos.core.domain.entities.TipoPagamento;
import com.techchallenge.pagamentos.core.domain.entities.messaging.Mensagem;
import com.techchallenge.pagamentos.core.domain.entities.messaging.StatusPedido;
import com.techchallenge.pagamentos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoPKEntity;
import com.techchallenge.pagamentos.drivers.db.entities.TipoPagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.repositories.PagamentoRepository;
import com.techchallenge.pagamentos.drivers.db.repositories.TipoPagamentoRepository;
import com.techchallenge.pagamentos.drivers.producers.cliente.NotificacaoClienteProducer;
import com.techchallenge.pagamentos.drivers.producers.pagamento.ProducaoPedidoInclusaoProducer;

@RunWith(SpringJUnit4ClassRunner.class)
public class PagamentoGatewayImplTest {
    @InjectMocks
    PagamentoGatewayImpl pagamentoGateway;

    @Mock
    private PagamentoRepository pagamentoRepository;
    @Mock
    private TipoPagamentoRepository tipoPagamentoRepository;

    @Mock
    private TipoPagamentoBusinessMapper businessMapper;
    @Mock
    private PagamentoBusinessMapper pagamentoBusinessMapper;

    @Mock
    private MercadoPagoAPI mercadoPagoAPI;
    @Mock
    private ProducaoAPI producaoAPI;
    @Mock
	private PaymentStatus paymentAPI;
    @Mock
	private PedidoAPI pedidoAPI;
    @Mock
	private NotificacaoClienteProducer clienteProducer;
    @Mock
	private ProducaoPedidoInclusaoProducer pedidoInclusaoProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void dadoUmTipoDePagamentoQueNaoExisteEntaoefetuarPagamentoDeveRetornarException() {
        Cliente cliente = new Cliente();
        cliente.setCpf(123456789L);
        cliente.setNome("Teste");
        cliente.setEmail("teste@teste.com");

        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamentoId(3L);
        pagamento.setValor(BigDecimal.valueOf(10));
        pagamento.setPedidoId(123L);
        pagamento.setCliente(cliente);

        when(tipoPagamentoRepository.findById(3L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class, () -> {
            pagamentoGateway.efetuarPagamento(pagamento);
        });

        assertEquals(String.format("Não existe um cadastro de tipo de pagamento com código %d", 3), exception.getMessage());
    }

    @Test
    void dadoUmPagamentoAprovadoQuandoConsultarPagamentoDeveRetornarPagamento() 
    		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	
        TipoPagamentoEntity tipoPagamentoEntity = new TipoPagamentoEntity();
        tipoPagamentoEntity.setId(1L);
        tipoPagamentoEntity.setNome("QR Code");

        PagamentoEntity pagamentoEntity = new PagamentoEntity();
        pagamentoEntity.setIdPedido(123L);
        pagamentoEntity.setIdPagamentoExterno(123L);
        pagamentoEntity.setStatus(StatusPagamento.APROVADO);
        pagamentoEntity.setValor(BigDecimal.valueOf(10));
        pagamentoEntity.setTipoPagamento(tipoPagamentoEntity);
        
        String status = "approved";

        PagamentoResponseDTO pagamentoResponseDTO = new PagamentoResponseDTO(123L, "approved", "detalhes", "Pix");
        
        EventoPagamento evento = new EventoPagamento();
        evento.setData(new Data());
        evento.getData().setId(123L);
        evento.getData().setPedidoId(1L);
        
		PedidoDTO dto = new PedidoDTO();
		
		dto.setId(evento.getData().getPedidoId());
		dto.setStatus(StatusPedido.PREPARACAO);
		dto.setDataSolicitacao(OffsetDateTime.now());
		dto.setCliente(new com.techchallenge.pagamentos.adapter.dto.pagamentos.ClienteDTO());
		dto.getCliente().setEmail("email@teste.com.br");
		dto.getCliente().setId(1L);
		dto.getCliente().setNome("Teste");
		
		PaymentDTO paymentDto = new PaymentDTO();
		Class<?> clazz = paymentDto.getClass();
		Class<?> clazzSuper = clazz.getSuperclass();
		
		Field fieldId = clazzSuper.getDeclaredField("id");
		Field fieldStatus = clazzSuper.getDeclaredField("status");
		Field fieldStatusDetails = clazzSuper.getDeclaredField("statusDetail");
		Field fieldPaymentMethodId = clazzSuper.getDeclaredField("paymentMethodId");
		
		fieldId.setAccessible(Boolean.TRUE);
		fieldStatus.setAccessible(Boolean.TRUE);
		fieldStatusDetails.setAccessible(Boolean.TRUE);
		fieldPaymentMethodId.setAccessible(Boolean.TRUE);
		
		fieldId.set(paymentDto, 123L);
		fieldStatus.set(paymentDto, status);
		fieldStatusDetails.set(paymentDto, "teste");
		fieldPaymentMethodId.set(paymentDto, "teste");

        when(pagamentoRepository.findByIdPagamentoExterno(any())).thenReturn(pagamentoEntity);
        when(mercadoPagoAPI.consultarPagamento(any())).thenReturn(pagamentoResponseDTO);
        when(pagamentoRepository.save(pagamentoEntity)).thenReturn(pagamentoEntity);
        when(paymentAPI.consultarPagamento(evento.getData().getId(), pagamentoResponseDTO.getStatus())).thenReturn(paymentDto);
        when(pedidoAPI.buscarDadosPedido(evento.getData().getPedidoId())).thenReturn(dto);
        doNothing().when(producaoAPI).adicionarPedidoFilaProducao(evento.getData().getId().toString());
        doNothing().when(pedidoInclusaoProducer).enviar(any());
        doNothing().when(clienteProducer).enviar(any(Mensagem.class));

        PagamentoResponseDTO resultado = pagamentoGateway.consultarPagamento(evento, status);

        assertNotNull(resultado);
        assertEquals(pagamentoResponseDTO.getId(), resultado.getId());
        assertEquals("approved", resultado.getStatus());
        verify(pagamentoRepository).save(pagamentoEntity);
    }

    @Test
    void dadoUmPagamentoRecusadoQuandoConsultarPagamentoDeveRetornarPagamento() 
    		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	
        TipoPagamentoEntity tipoPagamentoEntity = new TipoPagamentoEntity();
        tipoPagamentoEntity.setId(1L);
        tipoPagamentoEntity.setNome("QR Code");

        PagamentoEntity pagamentoEntity = new PagamentoEntity();
        pagamentoEntity.setIdPedido(123L);
        pagamentoEntity.setIdPagamentoExterno(123L);
        pagamentoEntity.setStatus(StatusPagamento.RECUSADO);
        pagamentoEntity.setValor(BigDecimal.valueOf(10));
        pagamentoEntity.setTipoPagamento(tipoPagamentoEntity);
        
        String status = "approved";
        
        EventoPagamento evento = new EventoPagamento();
        evento.setData(new Data());
        evento.getData().setId(123L);
        evento.getData().setPedidoId(1L);
        
		PedidoDTO dto = new PedidoDTO();
		
		dto.setId(evento.getData().getPedidoId());
		dto.setStatus(StatusPedido.PREPARACAO);
		dto.setDataSolicitacao(OffsetDateTime.now());
		dto.setCliente(new com.techchallenge.pagamentos.adapter.dto.pagamentos.ClienteDTO());
		dto.getCliente().setEmail("email@teste.com.br");
		dto.getCliente().setId(1L);
		dto.getCliente().setNome("Teste");
		
		PaymentDTO paymentDto = new PaymentDTO();
		Class<?> clazz = paymentDto.getClass();
		Class<?> clazzSuper = clazz.getSuperclass();
		
		Field fieldId = clazzSuper.getDeclaredField("id");
		Field fieldStatus = clazzSuper.getDeclaredField("status");
		Field fieldStatusDetails = clazzSuper.getDeclaredField("statusDetail");
		Field fieldPaymentMethodId = clazzSuper.getDeclaredField("paymentMethodId");
		
		fieldId.setAccessible(Boolean.TRUE);
		fieldStatus.setAccessible(Boolean.TRUE);
		fieldStatusDetails.setAccessible(Boolean.TRUE);
		fieldPaymentMethodId.setAccessible(Boolean.TRUE);
		
		fieldId.set(paymentDto, 123L);
		fieldStatus.set(paymentDto, status);
		fieldStatusDetails.set(paymentDto, "teste");
		fieldPaymentMethodId.set(paymentDto, "teste");

        PagamentoResponseDTO pagamentoResponseDTO = new PagamentoResponseDTO(evento.getData().getId(), status, "detalhes", "Pix");

        when(pagamentoRepository.findByIdPagamentoExterno(any())).thenReturn(pagamentoEntity);
        when(mercadoPagoAPI.consultarPagamento(any())).thenReturn(pagamentoResponseDTO);
        when(pagamentoRepository.save(pagamentoEntity)).thenReturn(pagamentoEntity);
        doNothing().when(producaoAPI).atualizarStatusPedidoProducao(any(), any());
        when(paymentAPI.consultarPagamento(evento.getData().getId(), pagamentoResponseDTO.getStatus())).thenReturn(paymentDto);
        when(pedidoAPI.buscarDadosPedido(evento.getData().getPedidoId())).thenReturn(dto);
        doNothing().when(pedidoInclusaoProducer).enviar(any());
        doNothing().when(clienteProducer).enviar(any(Mensagem.class));

        PagamentoResponseDTO resultado = pagamentoGateway.consultarPagamento(evento, status);

        assertNotNull(resultado);
        assertEquals(pagamentoResponseDTO.getId(), resultado.getId());
        assertEquals(pagamentoResponseDTO.getStatus(), resultado.getStatus());
        verify(pagamentoRepository).save(pagamentoEntity);
    }

    @Test
    void listarTest() {
        TipoPagamentoEntity tipoPagamentoEntity1 = new TipoPagamentoEntity();
        tipoPagamentoEntity1.setId(1L);
        tipoPagamentoEntity1.setNome("QR Code");

        TipoPagamentoEntity tipoPagamentoEntity2 = new TipoPagamentoEntity();
        tipoPagamentoEntity2.setId(2L);
        tipoPagamentoEntity2.setNome("Cartão de Crédito");

        List<TipoPagamentoEntity> tipoPagamentoEntities = Arrays.asList(tipoPagamentoEntity1, tipoPagamentoEntity2);

        TipoPagamento tipoPagamento1 = new TipoPagamento();
        tipoPagamento1.setId(1L);
        tipoPagamento1.setNome("QR Code");

        TipoPagamento tipoPagamento2 = new TipoPagamento();
        tipoPagamento2.setId(2L);
        tipoPagamento2.setNome("Cartão de Crédito");

        List<TipoPagamento> tipoPagamentos = Arrays.asList(tipoPagamento1, tipoPagamento2);

        when(tipoPagamentoRepository.findAll()).thenReturn(tipoPagamentoEntities);
        when(businessMapper.toCollectionModel(tipoPagamentoEntities)).thenReturn(tipoPagamentos);

        when(businessMapper.toModel(tipoPagamentoEntity1)).thenReturn(tipoPagamento1);
        when(businessMapper.toModel(tipoPagamentoEntity2)).thenReturn(tipoPagamento2);

        List<TipoPagamento> resultado = pagamentoGateway.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(tipoPagamento1, resultado.get(0));
        assertEquals(tipoPagamento2, resultado.get(1));
    }

    @Test
    void consultarStatusPagamentoTest() {
        Cliente cliente = new Cliente();
        cliente.setCpf(123456789L);
        cliente.setNome("Teste");
        cliente.setEmail("teste@teste.com");

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNome("Teste");
        clienteDTO.setEmail("teste@teste.com");

        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamentoId(1L);
        pagamento.setValor(BigDecimal.valueOf(10));
        pagamento.setPedidoId(123L);
        pagamento.setCliente(cliente);

        TipoPagamentoEntity tipoPagamentoEntity = new TipoPagamentoEntity();
        tipoPagamentoEntity.setId(1L);
        tipoPagamentoEntity.setNome("QR Code");

        PagamentoEntity pagamentoEntity = new PagamentoEntity();
        pagamentoEntity.setIdPedido(123L);
        pagamentoEntity.setIdPagamentoExterno(123456L);
        pagamentoEntity.setStatus(StatusPagamento.APROVADO);
        pagamentoEntity.setValor(BigDecimal.valueOf(10));
        pagamentoEntity.setTipoPagamento(tipoPagamentoEntity);
        
        PagamentoPKEntity pk = new PagamentoPKEntity();
        pk.setIdPagamento(1L);
        pk.setIdPedido(123L);

        when(pagamentoRepository.findById(pk)).thenReturn(Optional.of(pagamentoEntity));
        when(pagamentoBusinessMapper.toModel(pagamentoEntity)).thenReturn(pagamento);
        Pagamento resultado = pagamentoGateway.consultarStatusPagamento(1L, 123L);

        assertNotNull(resultado);
        assertEquals(pagamento.getStatus(), resultado.getStatus());
        assertEquals(pagamento.getTipoPagamentoId(), resultado.getTipoPagamentoId());
    }
}
