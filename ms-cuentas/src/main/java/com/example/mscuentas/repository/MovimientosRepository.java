package com.example.mscuentas.repository;

import com.example.mscuentas.entity.Movimientos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientosRepository extends JpaRepository<Movimientos, Long> {
    @Query("SELECT m FROM Movimientos m, Cuenta c WHERE m.cuenta.id = c.id AND c.clienteId = :clienteId AND m.fecha BETWEEN :startDate AND :endDate")
    List<Movimientos> findByClienteAndFechaBetween(@Param("clienteId") Long clienteId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
