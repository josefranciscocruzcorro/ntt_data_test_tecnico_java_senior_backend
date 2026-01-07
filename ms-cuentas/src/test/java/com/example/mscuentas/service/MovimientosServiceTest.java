package com.example.mscuentas.service;

import com.example.mscuentas.entity.Cuenta;
import com.example.mscuentas.entity.Movimientos;
import com.example.mscuentas.repository.CuentaRepository;
import com.example.mscuentas.repository.MovimientosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientosServiceTest {

    @Mock
    private MovimientosRepository movimientosRepository;
    
    @Mock
    private CuentaRepository cuentaRepository;
    
    @Mock
    private TransactionTemplate transactionTemplate;

    @InjectMocks
    private MovimientosService movimientosService;

    @Test
    void whenCreateMovimiento_SaldoInsuficiente_thenThrowException() {
        // Setup
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setSaldoInicial(new BigDecimal("100"));
        
        Movimientos mov = new Movimientos();
        mov.setCuenta(cuenta);
        mov.setTipoMovimiento("Retiro");
        mov.setValor(new BigDecimal("-200"));
        
        // Mock Transaction Execution manually since TransactionTemplate is functional
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            TransactionCallback<Movimientos> callback = invocation.getArgument(0);
            return callback.doInTransaction(null);
        });
        
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        // Act & Assert
        Mono<Movimientos> result = movimientosService.create(mov);
        
        StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Saldo no disponible"))
            .verify();
    }
}
