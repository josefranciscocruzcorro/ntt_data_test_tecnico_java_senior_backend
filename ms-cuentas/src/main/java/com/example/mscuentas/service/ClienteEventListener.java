package com.example.mscuentas.service;

import com.example.mscuentas.entity.ClienteReplica;
import com.example.mscuentas.event.ClienteEvent;
import com.example.mscuentas.repository.ClienteReplicaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteEventListener {

    private final ClienteReplicaRepository repository;

    @KafkaListener(topics = "cliente-events", groupId = "ms-cuentas-group")
    public void handle(ClienteEvent event) {
        log.info("Received event: {}", event);
        if ("CREATED".equals(event.getType()) || "UPDATED".equals(event.getType())) {
            repository.save(new ClienteReplica(event.getId(), event.getNombre()));
        } else if ("DELETED".equals(event.getType())) {
            repository.deleteById(event.getId());
        }
    }
}
