package com.duoc.citasmedicas.controller;

import com.duoc.citasmedicas.model.Doctor;
import com.duoc.citasmedicas.service.DoctorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controlador REST de doctores. Expone el CRUD completo y las consultas por especialidad.
@RestController
@RequestMapping("/api/doctores")
@Slf4j
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> listarDoctores() {
        log.info("GET /api/doctores");
        return ResponseEntity.ok(doctorService.listarDoctores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/doctores/{}", id);
        return ResponseEntity.ok(doctorService.buscarPorId(id));
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<Doctor>> buscarPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(doctorService.buscarPorEspecialidad(especialidad));
    }

    @PostMapping
    public ResponseEntity<Doctor> crearDoctor(@Valid @RequestBody Doctor doctor) {
        log.info("POST /api/doctores - {}", doctor.getNombre());
        Doctor creado = doctorService.crearDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> actualizarDoctor(@PathVariable Long id,
                                                   @Valid @RequestBody Doctor doctor) {
        log.info("PUT /api/doctores/{}", id);
        return ResponseEntity.ok(doctorService.actualizarDoctor(id, doctor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDoctor(@PathVariable Long id) {
        log.info("DELETE /api/doctores/{}", id);
        doctorService.eliminarDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
