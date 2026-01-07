package com.example.mscuentas.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEvent {
    private Long id;
    private String nombre;
    private String identificacion;
    private boolean estado;
    private String type;
}
