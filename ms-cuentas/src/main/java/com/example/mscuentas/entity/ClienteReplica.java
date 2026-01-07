package com.example.mscuentas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "cliente_replica")
@AllArgsConstructor
@NoArgsConstructor
public class ClienteReplica {
    @Id
    private Long id; // Same ID as in ms-clientes
    private String nombre;
}
