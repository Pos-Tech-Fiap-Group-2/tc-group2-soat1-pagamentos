package com.techchallenge.pagamentos.drivers.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techchallenge.pagamentos.drivers.db.entities.TipoPagamentoEntity;


@Repository
public interface TipoPagamentoRepository extends JpaRepository<TipoPagamentoEntity, Long> {

}
