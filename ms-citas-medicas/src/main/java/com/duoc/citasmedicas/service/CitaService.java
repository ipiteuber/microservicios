package com.duoc.citasmedicas.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.model.Doctor;

/**
 * Servicio para gestionar citas medicas en memoria.
 * Permite programar, cancelar y consultar disponibilidad.
 */
@Service
public class CitaService {

    private final List<CitaMedica> citas = new ArrayList<>();
    private final DoctorService doctorService;
    private Long nextId = 5L;

    /** Inicializa datos de ejemplo e inyecta DoctorService. */
    public CitaService(DoctorService doctorService) {
        this.doctorService = doctorService;

        citas.add(new CitaMedica(1L, "Juan Perez Soto", "12.345.678-9", 1L,
                "Dra. Maria Gonzalez", "Medicina General",
                "2026-04-01", "09:00", "PROGRAMADA", "Control general anual"));
        citas.add(new CitaMedica(2L, "Ana Torres Vega", "15.678.901-2", 2L,
                "Dr. Carlos Munoz", "Pediatria",
                "2026-04-01", "10:00", "PROGRAMADA", "Vacunacion infantil"));
        citas.add(new CitaMedica(3L, "Pedro Rojas Diaz", "10.234.567-8", 3L,
                "Dra. Andrea Silva", "Cardiologia",
                "2026-03-28", "09:00", "COMPLETADA", "Examen cardiologico de rutina"));
        citas.add(new CitaMedica(4L, "Maria Fernanda Contreras", "18.901.234-5", 4L,
                "Dr. Roberto Fernandez", "Dermatologia",
                "2026-03-29", "11:00", "CANCELADA", "Revision de lunares"));
    }

    // Devuelve todas las citas
    public List<CitaMedica> listarCitas() {
        return citas;
    }

    // Busca cita por id
    public Optional<CitaMedica> buscarPorId(Long id) {
        return citas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    // Busca citas por rut de paciente
    public List<CitaMedica> buscarPorPaciente(String rutPaciente) {
        return citas.stream()
                .filter(c -> c.getRutPaciente().equals(rutPaciente))
                .collect(Collectors.toList());
    }

    // Valida datos y programa una cita; retorna mensaje de error o null
    public String programarCita(CitaMedica cita) {
        // Valida nombre paciente
        if (cita.getNombrePaciente() == null || cita.getNombrePaciente().trim().isEmpty()) {
            return "El nombre del paciente es obligatorio";
        }

        // Valida formato del RUT (ej: 12.345.678-9)
        if (cita.getRutPaciente() == null || !validarRut(cita.getRutPaciente())) {
            return "El RUT del paciente no tiene un formato valido (ej: 12.345.678-9)";
        }

        // Valida existencia y disponibilidad del doctor
        if (cita.getDoctorId() == null) {
            return "El ID del doctor es obligatorio";
        }
        Optional<Doctor> doctorOpt = doctorService.buscarPorId(cita.getDoctorId());
        if (doctorOpt.isEmpty()) {
            return "No se encontro un doctor con el ID proporcionado";
        }
        Doctor doctor = doctorOpt.get();
        if (!doctor.isDisponible()) {
            return "El doctor seleccionado no se encuentra disponible actualmente";
        }

        // Valida fecha y que no sea pasada
        if (cita.getFecha() == null || cita.getFecha().trim().isEmpty()) {
            return "La fecha de la cita es obligatoria";
        }
        LocalDate fechaCita;
        try {
            fechaCita = LocalDate.parse(cita.getFecha(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return "El formato de la fecha no es valido. Use YYYY-MM-DD";
        }
        if (fechaCita.isBefore(LocalDate.now())) {
            return "No se pueden programar citas en fechas pasadas";
        }

        // Valida hora y horario del doctor
        if (cita.getHora() == null || cita.getHora().trim().isEmpty()) {
            return "La hora de la cita es obligatoria";
        }
        LocalTime horaCita;
        try {
            horaCita = LocalTime.parse(cita.getHora(), DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            return "El formato de la hora no es valido. Use HH:mm";
        }

        LocalTime inicioDoctor = LocalTime.parse(doctor.getHorarioInicio(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime finDoctor = LocalTime.parse(doctor.getHorarioFin(), DateTimeFormatter.ofPattern("HH:mm"));

        if (horaCita.isBefore(inicioDoctor) || horaCita.isAfter(finDoctor.minusMinutes(30))) {
            return "La hora esta fuera del horario del doctor (" + doctor.getHorarioInicio() + " - " + doctor.getHorarioFin() + ")";
        }

        // Verifica conflicto de horarios
        boolean conflicto = citas.stream()
                .anyMatch(c -> c.getDoctorId().equals(cita.getDoctorId())
                        && c.getFecha().equals(cita.getFecha())
                        && c.getHora().equals(cita.getHora())
                        && "PROGRAMADA".equals(c.getEstado()));
        if (conflicto) {
            return "El doctor ya tiene una cita programada en esa fecha y hora";
        }

        // Valida motivo
        if (cita.getMotivo() == null || cita.getMotivo().trim().isEmpty()) {
            return "El motivo de la cita es obligatorio";
        }

        // Asigna id, datos del doctor y guarda
        cita.setId(nextId++);
        cita.setNombreDoctor(doctor.getNombre());
        cita.setEspecialidad(doctor.getEspecialidad());
        cita.setEstado("PROGRAMADA");
        citas.add(cita);

        return null;
    }

    // Cancela una cita si esta en estado PROGRAMADA
    public String cancelarCita(Long id) {
        Optional<CitaMedica> citaOpt = buscarPorId(id);
        if (citaOpt.isEmpty()) {
            return "NOTFOUND";
        }
        CitaMedica cita = citaOpt.get();
        if (!"PROGRAMADA".equals(cita.getEstado())) {
            return "Solo se pueden cancelar citas con estado PROGRAMADA. Estado actual: " + cita.getEstado();
        }
        cita.setEstado("CANCELADA");
        return null; // null means success
    }

    // Genera horas disponibles de un doctor en una fecha
    public List<String> consultarDisponibilidad(Long doctorId, String fecha) {
        Optional<Doctor> doctorOpt = doctorService.buscarPorId(doctorId);
        if (doctorOpt.isEmpty()) {
            return null;
        }
        Doctor doctor = doctorOpt.get();

        LocalTime inicio = LocalTime.parse(doctor.getHorarioInicio(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime fin = LocalTime.parse(doctor.getHorarioFin(), DateTimeFormatter.ofPattern("HH:mm"));

        // Genera bloques de 30 minutos
        List<String> allSlots = new ArrayList<>();
        LocalTime current = inicio;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        while (current.isBefore(fin)) {
            allSlots.add(current.format(formatter));
            current = current.plusMinutes(30);
        }

        // Horas ya reservadas para ese doctor y fecha
        List<String> bookedSlots = citas.stream()
                .filter(c -> c.getDoctorId().equals(doctorId)
                        && c.getFecha().equals(fecha)
                        && "PROGRAMADA".equals(c.getEstado()))
                .map(CitaMedica::getHora)
                .collect(Collectors.toList());

        // Retorna solo las horas disponibles
        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    // Valida el formato del RUT chileno (ej: 12.345.678-9)
    private boolean validarRut(String rut) {
        return rut.matches("^\\d{1,2}\\.\\d{3}\\.\\d{3}-[\\dkK]$")
                || rut.matches("^\\d{7,8}-[\\dkK]$");
    }
}
