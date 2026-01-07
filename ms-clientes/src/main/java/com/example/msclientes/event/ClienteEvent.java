package com.example.msclientes.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEvent {
    private Long id;
    private String nombre;
    private String identificacion;
    private boolean estado;
    private String type; // "CREATED", "UPDATED", "DELETED"
}
