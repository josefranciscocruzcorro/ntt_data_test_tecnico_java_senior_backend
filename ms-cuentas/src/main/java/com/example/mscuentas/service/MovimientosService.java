package com.example.mscuentas.service;

import com.example.mscuentas.entity.Cuenta;
import com.example.mscuentas.entity.Movimientos;
import com.example.mscuentas.repository.CuentaRepository;
import com.example.mscuentas.repository.MovimientosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovimientosService {

    private final MovimientosRepository movimientosRepository;
    private final CuentaRepository cuentaRepository;
    private final TransactionTemplate transactionTemplate;

    public Flux<Movimientos> findAll() {
        return Flux.defer(() -> Flux.fromIterable(movimientosRepository.findAll())).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Movimientos> create(Movimientos movimientos) {
        return Mono.fromCallable(() -> transactionTemplate.execute(status -> {
            Long cuentaId = movimientos.getCuenta().getId();
            Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow(() -> new RuntimeException("Cuenta not found"));
            
            BigDecimal currentBalance = cuenta.getSaldoInicial();
            if (movimientos.getValor().compareTo(BigDecimal.ZERO) == 0) throw new RuntimeException("Zero value");
            
            String tipo = movimientos.getTipoMovimiento();
            BigDecimal effectiveAmount;
             
            // Normalize value based on Type: Retiro/Debito is always negative, others positive.
            if ("Retiro".equalsIgnoreCase(tipo) || "Debito".equalsIgnoreCase(tipo)) {
                effectiveAmount = movimientos.getValor().abs().negate(); 
            } else {
                effectiveAmount = movimientos.getValor().abs();
            }
            
            BigDecimal newBalance = currentBalance.add(effectiveAmount);
            
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                 throw new RuntimeException("Saldo no disponible");
            }
            
            cuenta.setSaldoInicial(newBalance);
            cuentaRepository.save(cuenta);
            
            movimientos.setValor(effectiveAmount); // Store signed value
            movimientos.setSaldo(newBalance);
            movimientos.setCuenta(cuenta);
            if(movimientos.getFecha() == null) movimientos.setFecha(LocalDateTime.now());
            
            return movimientosRepository.save(movimientos);
        })).subscribeOn(Schedulers.boundedElastic());
    }
    
    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> movimientosRepository.deleteById(id)).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
