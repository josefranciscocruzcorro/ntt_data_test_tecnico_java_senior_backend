package com.example.mscuentas.service;

import com.example.mscuentas.dto.ReporteDTO;
import com.example.mscuentas.entity.Movimientos;
import com.example.mscuentas.entity.ClienteReplica;
import com.example.mscuentas.repository.MovimientosRepository;
import com.example.mscuentas.repository.ClienteReplicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MovimientosRepository movimientosRepository;
    private final ClienteReplicaRepository clienteReplicaRepository;

    public Flux<ReporteDTO> generate(Long clientId, LocalDate start, LocalDate end) {
        // Fetch client name from replica
        String clientName = clienteReplicaRepository.findById(clientId)
                .map(ClienteReplica::getNombre)
                .orElse("Unknown/External");

        return Flux.defer(() -> Flux.fromIterable(
            movimientosRepository.findByClienteAndFechaBetween(clientId, start.atStartOfDay(), end.atTime(LocalTime.MAX))
        ))
        .map(m -> {
            ReporteDTO dto = new ReporteDTO();
            dto.setFecha(m.getFecha());
            dto.setCliente(clientName);
            dto.setNumeroCuenta(m.getCuenta().getNumeroCuenta());
            dto.setTipo(m.getCuenta().getTipoCuenta());
            dto.setEstado(m.getCuenta().isEstado());
            dto.setSaldoDisponible(m.getSaldo());
            
            // Reverse calc to get previous balance: Current Saldo - Movimiento Value
            dto.setMovimiento(m.getValor());
            dto.setSaldoInicial(m.getSaldo().subtract(m.getValor()));

            return dto;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
