package com.duoc.citasmedicas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.citasmedicas.model.Doctor;
import com.duoc.citasmedicas.service.DoctorService;

/**
 * Controlador REST para doctores.
 * Endpoints para listar, buscar y filtrar por especialidad.
 */
@RestController
@RequestMapping("/api/doctores")
public class DoctorController {

    private final DoctorService doctorService;

    /** Inyecta el servicio de doctores. */
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Retorna todos los doctores
    @GetMapping
    public ResponseEntity<List<Doctor>> listarDoctores() {
        return ResponseEntity.ok(doctorService.listarDoctores());
    }

    // Busca doctor por id
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        return doctorService.buscarPorId(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(java.util.Map.of("error", "No se encontro doctor con ID: " + id)));
    }

    // Filtra doctores por especialidad
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<Doctor>> buscarPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(doctorService.buscarPorEspecialidad(especialidad));
    }
}
