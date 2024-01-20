package com.techchallenge.pagamentos.drivers.db.repositories;

import com.techchallenge.pagamentos.core.domain.entities.StatusPagamento;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.entities.TipoPagamentoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class PagamentoRepositoryTest {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByIdPagamentoExternoTest() {
        TipoPagamentoEntity tipoPagamentoEntity = new TipoPagamentoEntity();
        tipoPagamentoEntity.setNome("QR Code");
        tipoPagamentoEntity.setId(1L);

        PagamentoEntity pagamentoEntity = new PagamentoEntity();
        pagamentoEntity.setIdPedido(123L);
        pagamentoEntity.setIdPagamentoExterno(123456L);
        pagamentoEntity.setStatus(StatusPagamento.APROVADO);
        pagamentoEntity.setValor(BigDecimal.valueOf(10));
        pagamentoEntity.setTipoPagamento(tipoPagamentoEntity);

        this.entityManager.persist(pagamentoEntity);

        PagamentoEntity pagamentoEntityRetornado = pagamentoRepository.findByIdPagamentoExterno(123456L);

        assertNotNull(pagamentoEntityRetornado);
        assertEquals(123456L, pagamentoEntityRetornado.getIdPagamentoExterno());
        assertEquals(123L, pagamentoEntityRetornado.getIdPedido());
        assertEquals(StatusPagamento.APROVADO, pagamentoEntityRetornado.getStatus());
        assertEquals(BigDecimal.valueOf(10), pagamentoEntityRetornado.getValor());
        assertEquals(1L, pagamentoEntityRetornado.getTipoPagamento().getId());
        assertEquals("QR Code", pagamentoEntityRetornado.getTipoPagamento().getNome());
    }

    @Test
    void findByIdPedidoTest() {
        TipoPagamentoEntity tipoPagamentoEntity = new TipoPagamentoEntity();
        tipoPagamentoEntity.setNome("QR Code");
        tipoPagamentoEntity.setId(1L);

        PagamentoEntity pagamentoEntity = new PagamentoEntity();
        pagamentoEntity.setIdPedido(123L);
        pagamentoEntity.setIdPagamentoExterno(123456L);
        pagamentoEntity.setStatus(StatusPagamento.APROVADO);
        pagamentoEntity.setValor(BigDecimal.valueOf(10));
        pagamentoEntity.setTipoPagamento(tipoPagamentoEntity);

        this.entityManager.persist(pagamentoEntity);

        PagamentoEntity pagamentoEntityRetornado = pagamentoRepository.findByIdPedido(123L);

        assertNotNull(pagamentoEntityRetornado);
        assertEquals(123456L, pagamentoEntityRetornado.getIdPagamentoExterno());
        assertEquals(123L, pagamentoEntityRetornado.getIdPedido());
        assertEquals(StatusPagamento.APROVADO, pagamentoEntityRetornado.getStatus());
        assertEquals(BigDecimal.valueOf(10), pagamentoEntityRetornado.getValor());
        assertEquals(1L, pagamentoEntityRetornado.getTipoPagamento().getId());
        assertEquals("QR Code", pagamentoEntityRetornado.getTipoPagamento().getNome());
    }
}
