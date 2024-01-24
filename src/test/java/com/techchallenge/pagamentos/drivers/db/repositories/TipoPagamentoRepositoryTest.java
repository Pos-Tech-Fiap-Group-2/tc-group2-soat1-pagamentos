package com.techchallenge.pagamentos.drivers.db.repositories;

import com.techchallenge.pagamentos.core.domain.entities.StatusPagamento;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.entities.TipoPagamentoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TipoPagamentoRepositoryTest {
    @Autowired
    private TipoPagamentoRepository repository;

    @Test
    void findByIdPagamentoExternoTest() {
        TipoPagamentoEntity tipoPagamentoEntity = new TipoPagamentoEntity();
        tipoPagamentoEntity.setNome("QR Code");
        tipoPagamentoEntity.setId(1L);

        repository.save(tipoPagamentoEntity);

        Optional<TipoPagamentoEntity> resultado = repository.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.get().getId());
        assertEquals("QR Code", resultado.get().getNome());
    }

}
