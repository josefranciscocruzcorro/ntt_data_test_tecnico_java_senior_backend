package com.example.msclientes.service;

import com.example.msclientes.entity.Cliente;
import com.example.msclientes.event.ClienteEvent;
import com.example.msclientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Flux<Cliente> findAll() {
        return Flux.defer(() -> Flux.fromIterable(clienteRepository.findAll()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Cliente> save(Cliente cliente) {
        return Mono.fromCallable(() -> {
            Cliente saved = clienteRepository.save(cliente);
            // Async event
            kafkaTemplate.send("cliente-events", new ClienteEvent(saved.getId(), saved.getNombre(), saved.getIdentificacion(), saved.isEstado(), "CREATED"));
            return saved;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Cliente> update(Long id, Cliente cliente) {
        return Mono.fromCallable(() -> {
            if (clienteRepository.existsById(id)) {
                 cliente.setId(id); // Ensure ID is set for update (Persona ID)
                 // Note: JPA inheritance update is tricky if we don't set the ID on the superclass correctly if using separated setters, but `cliente.setId` sets inherited ID.
                 // Better pattern: fetch, update fields, save.
                 Cliente existing = clienteRepository.findById(id).orElseThrow();
                 existing.setNombre(cliente.getNombre());
                 existing.setGenero(cliente.getGenero());
                 existing.setIdentificacion(cliente.getIdentificacion());
                 existing.setDireccion(cliente.getDireccion());
                 existing.setTelefono(cliente.getTelefono());
                 existing.setContrasena(cliente.getContrasena());
                 existing.setEstado(cliente.isEstado());
                 
                 Cliente saved = clienteRepository.save(existing);
                 kafkaTemplate.send("cliente-events", new ClienteEvent(saved.getId(), saved.getNombre(), saved.getIdentificacion(), saved.isEstado(), "UPDATED"));
                 return saved;
            }
            throw new RuntimeException("Cliente not found");
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> {
            clienteRepository.deleteById(id);
             // Async event
             kafkaTemplate.send("cliente-events", new ClienteEvent(id, null, null, false, "DELETED"));
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
