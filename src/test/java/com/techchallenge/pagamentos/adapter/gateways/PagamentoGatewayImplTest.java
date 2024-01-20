package com.techchallenge.pagamentos.adapter.gateways;

import com.techchallenge.pagamentos.adapter.dto.cliente.ClienteDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.external.mercadopago.MercadoPagoAPI;
import com.techchallenge.pagamentos.adapter.external.producao.ProducaoAPI;
import com.techchallenge.pagamentos.adapter.gateways.impl.PagamentoGatewayImpl;
import com.techchallenge.pagamentos.adapter.mapper.business.PagamentoBusinessMapper;
import com.techchallenge.pagamentos.adapter.mapper.business.TipoPagamentoBusinessMapper;
import com.techchallenge.pagamentos.core.domain.entities.Cliente;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import com.techchallenge.pagamentos.core.domain.entities.StatusPagamento;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.entities.TipoPagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.repositories.PagamentoRepository;
import com.techchallenge.pagamentos.drivers.db.repositories.TipoPagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void efetuarPagamentoTest() {
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

        PagamentoPixResponseDTO pagamentoPixResponseDTO = new PagamentoPixResponseDTO(123L, "Approved", "Detalhes",
                "Pix", "qrCodeBase64", "qrCode");

        when(tipoPagamentoRepository.findById(1L)).thenReturn(Optional.of(tipoPagamentoEntity));
        when(pagamentoRepository.save(pagamentoEntity)).thenReturn(pagamentoEntity);
        when(mercadoPagoAPI.efetuarPagamentoViaPix(any(PagamentoPixDTO.class))).thenReturn(pagamentoPixResponseDTO);

        PagamentoPixResponseDTO resultado = pagamentoGateway.efetuarPagamento(pagamento);

        when(pagamentoRepository.save(pagamentoEntity)).thenReturn(pagamentoEntity);

        assertNotNull(resultado);
        assertEquals(pagamentoPixResponseDTO.getId(), resultado.getId());
        assertEquals(pagamentoPixResponseDTO.getStatus(), resultado.getStatus());
    }


}
