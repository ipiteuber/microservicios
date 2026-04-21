package com.duoc.citasmedicas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entidad Doctor - tabla DOCTOR en Oracle
@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del doctor es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 60)
    @Column(name = "especialidad", nullable = false, length = 60)
    private String especialidad;

    // Formato HH:mm (24h)
    @NotBlank(message = "El horario de inicio es obligatorio")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "El horario debe tener formato HH:mm")
    @Column(name = "horario_inicio", nullable = false, length = 5)
    private String horarioInicio;

    @NotBlank(message = "El horario de fin es obligatorio")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "El horario debe tener formato HH:mm")
    @Column(name = "horario_fin", nullable = false, length = 5)
    private String horarioFin;

    @NotNull(message = "La disponibilidad es obligatoria")
    @Column(name = "disponible", nullable = false)
    private Boolean disponible;
}
