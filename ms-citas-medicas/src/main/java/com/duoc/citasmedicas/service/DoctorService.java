package com.duoc.citasmedicas.service;

import com.duoc.citasmedicas.model.Doctor;

import java.util.List;

// Contrato del servicio de doctores
public interface DoctorService {

    List<Doctor> listarDoctores();

    Doctor buscarPorId(Long id);

    List<Doctor> buscarPorEspecialidad(String especialidad);

    Doctor crearDoctor(Doctor doctor);

    Doctor actualizarDoctor(Long id, Doctor doctor);

    void eliminarDoctor(Long id);
}
