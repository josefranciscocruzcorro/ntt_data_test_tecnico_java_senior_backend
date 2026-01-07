package com.example.mscuentas.controller;

import com.example.mscuentas.entity.Cuenta;
import com.example.mscuentas.entity.Movimientos;
import com.example.mscuentas.dto.ReporteDTO;
import com.example.mscuentas.service.CuentaService;
import com.example.mscuentas.service.MovimientosService;
import com.example.mscuentas.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    private final CuentaService service;
    
    @GetMapping
    public Flux<Cuenta> getAll() { return service.findAll(); }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cuenta> create(@RequestBody Cuenta c) { return service.save(c); }

    @GetMapping("/{id}")
    public Mono<Cuenta> get(@PathVariable Long id) { return service.findById(id); }
    
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) { return service.delete(id); }
}

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
class MovimientosController {
    private final MovimientosService service;

    @GetMapping
    public Flux<Movimientos> getAll() { return service.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Movimientos> create(@RequestBody Movimientos m) { return service.create(m); }
    
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) { return service.delete(id); }
}

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
class ReporteController {
    private final ReportService service;
    
    @GetMapping
    public Flux<ReporteDTO> report(@RequestParam Long clientId, 
                                   @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return service.generate(clientId, startDate, endDate);
    }
}
