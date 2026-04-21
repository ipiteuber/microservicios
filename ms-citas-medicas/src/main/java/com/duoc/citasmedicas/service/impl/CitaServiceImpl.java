package com.duoc.citasmedicas.service.impl;

import com.duoc.citasmedicas.exception.BusinessValidationException;
import com.duoc.citasmedicas.exception.ResourceNotFoundException;
import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.model.Doctor;
import com.duoc.citasmedicas.repository.CitaMedicaRepository;
import com.duoc.citasmedicas.service.CitaService;
import com.duoc.citasmedicas.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Servicio de citas medicas, maneja el flujo: programar -> cancelar/completar
@Service
@Slf4j
public class CitaServiceImpl implements CitaService {

    private static final String ESTADO_PROGRAMADA = "PROGRAMADA";
    private static final String ESTADO_CANCELADA = "CANCELADA";
    private static final String ESTADO_COMPLETADA = "COMPLETADA";
    private static final DateTimeFormatter HORA_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final CitaMedicaRepository citaRepository;
    private final DoctorService doctorService;

    public CitaServiceImpl(CitaMedicaRepository citaRepository, DoctorService doctorService) {
        this.citaRepository = citaRepository;
        this.doctorService = doctorService;
    }

    @Override
    public List<CitaMedica> listarCitas() {
        return citaRepository.findAll();
    }

    @Override
    public CitaMedica buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro cita con ID: " + id));
    }

    @Override
    public List<CitaMedica> buscarPorPaciente(String rutPaciente) {
        return citaRepository.findByRutPaciente(rutPaciente);
    }

    @Override
    @Transactional
    public CitaMedica programarCita(CitaMedica cita) {
        // Busca el doctor, lanza 404 si no existe
        Doctor doctor = doctorService.buscarPorId(cita.getDoctorId());

        if (!Boolean.TRUE.equals(doctor.getDisponible())) {
            throw new BusinessValidationException("El doctor no esta disponible actualmente");
        }

        validarHoraEnHorarioDoctor(cita.getHora(), doctor);
        validarSinConflictoHorario(cita);

        // Llena los campos denormalizados desde el doctor
        cita.setId(null);
        cita.setNombreDoctor(doctor.getNombre());
        cita.setEspecialidad(doctor.getEspecialidad());
        cita.setEstado(ESTADO_PROGRAMADA);

        CitaMedica guardada = citaRepository.save(cita);
        log.info("Cita programada con ID {} para paciente {}", guardada.getId(), cita.getNombrePaciente());
        return guardada;
    }

    @Override
    @Transactional
    public CitaMedica actualizarCita(Long id, CitaMedica citaActualizada) {
        CitaMedica existente = buscarPorId(id);

        if (!ESTADO_PROGRAMADA.equals(existente.getEstado())) {
            throw new BusinessValidationException(
                    "Solo se pueden modificar citas PROGRAMADA. Estado actual: " + existente.getEstado());
        }

        // Solo se permite cambiar fecha, hora y motivo (el doctor y paciente no)
        existente.setFecha(citaActualizada.getFecha());
        existente.setHora(citaActualizada.getHora());
        existente.setMotivo(citaActualizada.getMotivo());

        // Re-valida contra el horario del doctor y conflictos
        Doctor doctor = doctorService.buscarPorId(existente.getDoctorId());
        validarHoraEnHorarioDoctor(existente.getHora(), doctor);
        validarSinConflictoHorario(existente);

        return citaRepository.save(existente);
    }

    @Override
    @Transactional
    public CitaMedica cancelarCita(Long id) {
        CitaMedica cita = buscarPorId(id);

        if (!ESTADO_PROGRAMADA.equals(cita.getEstado())) {
            throw new BusinessValidationException(
                    "Solo se pueden cancelar citas PROGRAMADA. Estado actual: " + cita.getEstado());
        }

        cita.setEstado(ESTADO_CANCELADA);
        log.info("Cita {} cancelada", id);
        return citaRepository.save(cita);
    }

    @Override
    @Transactional
    public CitaMedica completarCita(Long id) {
        CitaMedica cita = buscarPorId(id);

        if (!ESTADO_PROGRAMADA.equals(cita.getEstado())) {
            throw new BusinessValidationException(
                    "Solo se pueden completar citas PROGRAMADA. Estado actual: " + cita.getEstado());
        }

        cita.setEstado(ESTADO_COMPLETADA);
        return citaRepository.save(cita);
    }

    @Override
    @Transactional
    public void eliminarCita(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro cita con ID: " + id);
        }
        citaRepository.deleteById(id);
        log.info("Cita {} eliminada", id);
    }

    @Override
    public List<String> consultarDisponibilidad(Long doctorId, LocalDate fecha) {
        Doctor doctor = doctorService.buscarPorId(doctorId);

        LocalTime inicio = LocalTime.parse(doctor.getHorarioInicio(), HORA_FORMATTER);
        LocalTime fin = LocalTime.parse(doctor.getHorarioFin(), HORA_FORMATTER);

        // Genera bloques de 30 minutos desde la hora de inicio hasta el fin
        List<String> todos = new ArrayList<>();
        LocalTime actual = inicio;
        while (actual.isBefore(fin)) {
            todos.add(actual.format(HORA_FORMATTER));
            actual = actual.plusMinutes(30);
        }

        // Saca las horas ya programadas
        List<String> reservadas = citaRepository.findByDoctorIdAndFecha(doctorId, fecha).stream()
                .filter(c -> ESTADO_PROGRAMADA.equals(c.getEstado()))
                .map(CitaMedica::getHora)
                .toList();

        return todos.stream().filter(h -> !reservadas.contains(h)).toList();
    }

    // Valida que la hora caiga dentro del horario del doctor (deja al menos 30 min antes del cierre)
    private void validarHoraEnHorarioDoctor(String hora, Doctor doctor) {
        LocalTime horaCita = LocalTime.parse(hora, HORA_FORMATTER);
        LocalTime inicioDoc = LocalTime.parse(doctor.getHorarioInicio(), HORA_FORMATTER);
        LocalTime finDoc = LocalTime.parse(doctor.getHorarioFin(), HORA_FORMATTER);

        if (horaCita.isBefore(inicioDoc) || horaCita.isAfter(finDoc.minusMinutes(30))) {
            throw new BusinessValidationException(
                    "La hora esta fuera del horario del doctor (" + doctor.getHorarioInicio()
                            + " - " + doctor.getHorarioFin() + ")");
        }
    }

    // Verifica que no exista ya una cita PROGRAMADA para el mismo doctor/fecha/hora
    private void validarSinConflictoHorario(CitaMedica cita) {
        List<CitaMedica> existentes = citaRepository.findByDoctorIdAndFechaAndHoraAndEstado(
                cita.getDoctorId(), cita.getFecha(), cita.getHora(), ESTADO_PROGRAMADA);

        // En caso de update, la propia cita aparece en el resultado -> se excluye
        boolean hayConflicto = existentes.stream().anyMatch(c -> !c.getId().equals(cita.getId()));
        if (hayConflicto) {
            throw new BusinessValidationException("El doctor ya tiene una cita en esa fecha y hora");
        }
    }
}
