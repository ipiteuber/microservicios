package com.duoc.citasmedicas.controller;

import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.service.CitaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// Controlador REST de citas medicas. Maneja programacion, cancelacion, completacion y consulta de disponibilidad.
@RestController
@RequestMapping("/api/citas")
@Slf4j
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<List<CitaMedica>> listarCitas() {
        return ResponseEntity.ok(citaService.listarCitas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaMedica> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.buscarPorId(id));
    }

    @GetMapping("/paciente/{rut}")
    public ResponseEntity<List<CitaMedica>> buscarPorPaciente(@PathVariable String rut) {
        return ResponseEntity.ok(citaService.buscarPorPaciente(rut));
    }

    // Devuelve las horas libres de un doctor en una fecha determinada
    @GetMapping("/disponibilidad")
    public ResponseEntity<Object> consultarDisponibilidad(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<String> horas = citaService.consultarDisponibilidad(doctorId, fecha);
        return ResponseEntity.ok(Map.of(
                "doctorId", doctorId,
                "fecha", fecha.toString(),
                "horasDisponibles", horas
        ));
    }

    @PostMapping
    public ResponseEntity<CitaMedica> programarCita(@Valid @RequestBody CitaMedica cita) {
        log.info("POST /api/citas - paciente: {}", cita.getNombrePaciente());
        CitaMedica programada = citaService.programarCita(cita);
        return ResponseEntity.status(HttpStatus.CREATED).body(programada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaMedica> actualizarCita(@PathVariable Long id,
                                                     @Valid @RequestBody CitaMedica cita) {
        log.info("PUT /api/citas/{}", id);
        return ResponseEntity.ok(citaService.actualizarCita(id, cita));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<CitaMedica> cancelarCita(@PathVariable Long id) {
        log.info("Cancelando cita {}", id);
        return ResponseEntity.ok(citaService.cancelarCita(id));
    }

    @PutMapping("/{id}/completar")
    public ResponseEntity<CitaMedica> completarCita(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.completarCita(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        log.info("DELETE /api/citas/{}", id);
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}
