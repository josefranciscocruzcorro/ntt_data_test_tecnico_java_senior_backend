package com.example.msclientes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "cliente_id")
public class Cliente extends Persona {
    @Schema(description = "Contraseña segura del cliente", example = "1234")
    private String contrasena;

    @Schema(description = "Estado del cliente (true=Activo, false=Inactivo)", example = "true")
    private boolean estado;
}
