package com.duoc.citasmedicas.repository;

import com.duoc.citasmedicas.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio JPA de Doctor. Hereda findAll, findById, save, deleteById, existsById.
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByEspecialidadIgnoreCase(String especialidad);

    List<Doctor> findByDisponible(Boolean disponible);
}
