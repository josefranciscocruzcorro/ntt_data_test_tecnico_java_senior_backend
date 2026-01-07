package com.example.mscuentas.service;

import com.example.mscuentas.entity.Cuenta;
import com.example.mscuentas.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class CuentaService {
    private final CuentaRepository repository;

    public Flux<Cuenta> findAll() {
        return Flux.defer(() -> Flux.fromIterable(repository.findAll())).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Cuenta> save(Cuenta cuenta) {
        // Here we could validate if Client exists by checking ClienteReplica
        // but for now simple save
        return Mono.fromCallable(() -> repository.save(cuenta)).subscribeOn(Schedulers.boundedElastic());
    }
    
    public Mono<Void> delete(Long id) {
         return Mono.fromRunnable(() -> repository.deleteById(id)).subscribeOn(Schedulers.boundedElastic()).then();
    }
    
    public Mono<Cuenta> findById(Long id) {
        return Mono.fromCallable(() -> repository.findById(id).orElseThrow()).subscribeOn(Schedulers.boundedElastic());
    }
}
