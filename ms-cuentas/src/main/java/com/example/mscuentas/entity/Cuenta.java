package com.example.mscuentas.entity;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Identificador de la cuenta")
    private Long id;

    @Column(unique = true)
    @Schema(description = "Número de cuenta único", example = "478758")
    private String numeroCuenta;

    @Schema(description = "Tipo de cuenta (Ahorros, Corriente)", example = "Ahorros")
    private String tipoCuenta;

    @Schema(description = "Saldo inicial de la cuenta", example = "2000.00")
    private BigDecimal saldoInicial;

    @Schema(description = "Estado de la cuenta", example = "true")
    private boolean estado;
    
    // We store only ID of the client, but for report generation we might need the name.
    // We can use a Replica table or just store the ID and rely on a sync call (not senior)
    // or store the name here denormalized (simple CQRS).
    // Let's store client ID and also have a Replica Entity for reports.
    @Schema(description = "ID del cliente asociado", example = "1")
    private Long clienteId;
}
