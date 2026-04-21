package com.duoc.citasmedicas.service;

import com.duoc.citasmedicas.model.CitaMedica;

import java.time.LocalDate;
import java.util.List;

// Contrato del servicio de citas. La implementacion esta en service/impl/CitaServiceImpl.
public interface CitaService {

    List<CitaMedica> listarCitas();

    CitaMedica buscarPorId(Long id);

    List<CitaMedica> buscarPorPaciente(String rutPaciente);

    CitaMedica programarCita(CitaMedica cita);

    CitaMedica actualizarCita(Long id, CitaMedica citaActualizada);

    CitaMedica cancelarCita(Long id);

    CitaMedica completarCita(Long id);

    void eliminarCita(Long id);

    List<String> consultarDisponibilidad(Long doctorId, LocalDate fecha);
}
