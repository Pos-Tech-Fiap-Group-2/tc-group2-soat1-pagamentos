package com.techchallenge.pagamentos.drivers.db.entities;

import javax.persistence.*;

@Entity(name = "Pagamento")
public class PagamentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
