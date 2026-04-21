package com.duoc.citasmedicas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Entidad CitaMedica - tabla CITA_MEDICA en Oracle
// Estados posibles: PROGRAMADA, CANCELADA, COMPLETADA
@Entity
@Table(name = "cita_medica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(max = 120)
    @Column(name = "nombre_paciente", nullable = false, length = 120)
    private String nombrePaciente;

    // RUT chileno con puntos y guion: 12.345.678-9, o sin puntos: 12345678-9
    @NotBlank(message = "El RUT del paciente es obligatorio")
    @Pattern(regexp = "^(\\d{1,2}\\.\\d{3}\\.\\d{3}-[\\dkK]|\\d{7,8}-[\\dkK])$",
             message = "El RUT debe tener formato 12.345.678-9")
    @Column(name = "rut_paciente", nullable = false, length = 15)
    private String rutPaciente;

    @NotNull(message = "El ID del doctor es obligatorio")
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    // Se llenan en el service desde el Doctor (no vienen del request)
    @Column(name = "nombre_doctor", length = 100)
    private String nombreDoctor;

    @Column(name = "especialidad", length = 60)
    private String especialidad;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha no puede estar en el pasado")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotBlank(message = "La hora es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora debe tener formato HH:mm")
    @Column(name = "hora", nullable = false, length = 5)
    private String hora;

    // Al programar = PROGRAMADA, lo setea el service
    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @NotBlank(message = "El motivo de la cita es obligatorio")
    @Size(max = 255)
    @Column(name = "motivo", nullable = false, length = 255)
    private String motivo;
}
