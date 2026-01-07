package com.example.msclientes.entity;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Identificador único autogenerado")
    private Long id;

    @Schema(description = "Nombre completo de la persona", example = "Jose Lema")
    private String nombre;

    @Schema(description = "Género de la persona", example = "Masculino")
    private String genero;

    @Schema(description = "Identificación única (Cédula/DNI)", example = "1234567890")
    private String identificacion; // Unique usually

    @Schema(description = "Dirección de residencia", example = "Otavalo sn y principal")
    private String direccion;

    @Schema(description = "Teléfono de contacto", example = "098254785")
    private String telefono;
}
