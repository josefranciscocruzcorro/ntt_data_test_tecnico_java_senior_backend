package com.example.msclientes.service;

import com.example.msclientes.entity.Cliente;
import com.example.msclientes.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;
    
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void whenSaveCliente_thenReturnSavedCliente() {
        Cliente input = new Cliente();
        input.setNombre("Test");
        input.setIdentificacion("123");
        
        Cliente saved = new Cliente();
        saved.setId(1L);
        saved.setNombre("Test");
        
        when(clienteRepository.save(any(Cliente.class))).thenReturn(saved);
        
        Mono<Cliente> result = clienteService.save(input);
        
        StepVerifier.create(result)
            .expectNextMatches(c -> c.getId() == 1L && c.getNombre().equals("Test"))
            .verifyComplete();
    }
}
