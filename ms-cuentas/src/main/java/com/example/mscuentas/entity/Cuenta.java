package com.example.mscuentas.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numeroCuenta;

    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private boolean estado;
    
    // We store only ID of the client, but for report generation we might need the name.
    // We can use a Replica table or just store the ID and rely on a sync call (not senior)
    // or store the name here denormalized (simple CQRS).
    // Let's store client ID and also have a Replica Entity for reports.
    private Long clienteId;
}
