package com.example.mscuentas.entity;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "movimientos")
public class Movimientos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Fecha del movimiento", example = "2026-01-07T12:00:00")
    private LocalDateTime fecha;

    @Schema(description = "Tipo de movimiento (Retiro, Deposito)", example = "Retiro")
    private String tipoMovimiento;

    @Schema(description = "Valor del movimiento", example = "-575.00")
    private BigDecimal valor;

    @Schema(description = "Saldo disponible después del movimiento", accessMode = Schema.AccessMode.READ_ONLY, example = "1425.00")
    private BigDecimal saldo; // Balance after movement

    @ManyToOne(optional = false)
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;
}
