package com.duoc.citasmedicas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.duoc.citasmedicas.model.Doctor;

/**
 * Servicio para gestionar doctores en memoria.
 * Datos de ejemplo cargados en el constructor.
 */
@Service
public class DoctorService {

    private final List<Doctor> doctores = new ArrayList<>();

    /** Inicializa lista de doctores con datos de ejemplo. */
    public DoctorService() {
        doctores.add(new Doctor(1L, "Dra. Maria Gonzalez", "Medicina General", "08:00", "16:00", true));
        doctores.add(new Doctor(2L, "Dr. Carlos Munoz", "Pediatria", "09:00", "17:00", true));
        doctores.add(new Doctor(3L, "Dra. Andrea Silva", "Cardiologia", "08:30", "14:30", true));
        doctores.add(new Doctor(4L, "Dr. Roberto Fernandez", "Dermatologia", "10:00", "18:00", true));
        doctores.add(new Doctor(5L, "Dra. Francisca Lopez", "Medicina General", "14:00", "20:00", false));
    }

    /**
     * Returns all doctors registered in the system.
     *
     * @return list of all doctors
     */
    // Devuelve lista de doctores
    public List<Doctor> listarDoctores() {
        return doctores;
    }

    /**
     * Finds a doctor by their unique ID.
     *
     * @param id the doctor ID
     * @return an Optional containing the doctor if found, or empty otherwise
     */
    // Busca doctor por id
    public Optional<Doctor> buscarPorId(Long id) {
        return doctores.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    /**
     * Finds all doctors that match the given specialty (case-insensitive).
     *
     * @param especialidad the specialty to search for
     * @return list of doctors matching the specialty
     */
    // Filtra doctores por especialidad (case-insensitive)
    public List<Doctor> buscarPorEspecialidad(String especialidad) {
        return doctores.stream()
                .filter(d -> d.getEspecialidad().equalsIgnoreCase(especialidad))
                .collect(Collectors.toList());
    }
}
