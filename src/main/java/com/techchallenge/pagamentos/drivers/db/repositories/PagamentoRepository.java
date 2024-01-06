package com.techchallenge.pagamentos.drivers.db.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techchallenge.pagamentos.drivers.db.entities.PagamentoEntity;
import com.techchallenge.pagamentos.drivers.db.entities.PagamentoPKEntity;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoEntity, PagamentoPKEntity> {
	
}
