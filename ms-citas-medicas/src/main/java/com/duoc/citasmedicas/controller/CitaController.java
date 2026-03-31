package com.duoc.citasmedicas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.service.CitaService;

/**
 * Controlador REST para citas medicas.
 * Endpoints para listar, buscar, programar y cancelar citas.
 */
@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    /** Inyecta el servicio de citas. */
    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    // Retorna todas las citas
    @GetMapping
    public ResponseEntity<List<CitaMedica>> listarCitas() {
        return ResponseEntity.ok(citaService.listarCitas());
    }

    // Busca una cita por id
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        return citaService.buscarPorId(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "No se encontro cita con ID: " + id)));
    }

    // Lista citas de un paciente por rut
    @GetMapping("/paciente/{rut}")
    public ResponseEntity<List<CitaMedica>> buscarPorPaciente(@PathVariable String rut) {
        return ResponseEntity.ok(citaService.buscarPorPaciente(rut));
    }

    // Consulta horas disponibles para un doctor en una fecha
    @GetMapping("/disponibilidad")
    public ResponseEntity<Object> consultarDisponibilidad(
            @RequestParam Long doctorId,
            @RequestParam String fecha) {

        List<String> disponibilidad = citaService.consultarDisponibilidad(doctorId, fecha);
        if (disponibilidad == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "No se encontro doctor con ID: " + doctorId));
        }
        return ResponseEntity.ok(Map.of(
                "doctorId", doctorId,
                "fecha", fecha,
                "horasDisponibles", disponibilidad
        ));
    }

    // Programa una nueva cita (valida datos)
    @PostMapping
    public ResponseEntity<Object> programarCita(@RequestBody CitaMedica cita) {
        String error = citaService.programarCita(cita);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        return ResponseEntity.status(201).body(cita);
    }

    // Cancela una cita en estado PROGRAMADA
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Object> cancelarCita(@PathVariable Long id) {
        String resultado = citaService.cancelarCita(id);
        if ("NOTFOUND".equals(resultado)) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "No se encontro cita con ID: " + id));
        }
        if (resultado != null) {
            return ResponseEntity.badRequest().body(Map.of("error", resultado));
        }
        // Retorna la cita actualizada 
        return ResponseEntity.ok(citaService.buscarPorId(id).orElse(null));
    }
}
