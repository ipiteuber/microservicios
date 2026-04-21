package com.duoc.citasmedicas.repository;

import com.duoc.citasmedicas.model.CitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Repositorio JPA de CitaMedica
@Repository
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {

    List<CitaMedica> findByRutPaciente(String rutPaciente);

    List<CitaMedica> findByDoctorId(Long doctorId);

    List<CitaMedica> findByDoctorIdAndFecha(Long doctorId, LocalDate fecha);

    // Para detectar conflictos al programar una cita
    List<CitaMedica> findByDoctorIdAndFechaAndHoraAndEstado(
            Long doctorId, LocalDate fecha, String hora, String estado);
}
