package com.example.msclientes.controller;

import com.example.msclientes.entity.Cliente;
import com.example.msclientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public Flux<Cliente> getAll() {
        return clienteService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cliente> create(@RequestBody Cliente cliente) {
        return clienteService.save(cliente);
    }
    
    @PutMapping("/{id}")
    public Mono<Cliente> update(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.update(id, cliente);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return clienteService.delete(id);
    }
}
