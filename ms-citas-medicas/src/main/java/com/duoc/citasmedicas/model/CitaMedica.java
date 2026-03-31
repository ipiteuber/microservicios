package com.duoc.citasmedicas.model;

/**
 * Modelo que representa una cita medica.
 * Relaciona paciente y doctor en una fecha y hora con estado.
 * Estados: PROGRAMADA, CANCELADA, COMPLETADA.
 */
public class CitaMedica {

    private Long id;
    private String nombrePaciente;
    private String rutPaciente;
    private Long doctorId;
    private String nombreDoctor;
    private String especialidad;
    private String fecha;
    private String hora;
    private String estado;
    private String motivo;

    /** Constructor vacio. */
    public CitaMedica() {
    }
    /** Constructor completo. */
    public CitaMedica(Long id, String nombrePaciente, String rutPaciente, Long doctorId, String nombreDoctor,
                      String especialidad, String fecha, String hora, String estado, String motivo) {
        this.id = id;
        this.nombrePaciente = nombrePaciente;
        this.rutPaciente = rutPaciente;
        this.doctorId = doctorId;
        this.nombreDoctor = nombreDoctor;
        this.especialidad = especialidad;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.motivo = motivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getRutPaciente() {
        return rutPaciente;
    }

    public void setRutPaciente(String rutPaciente) {
        this.rutPaciente = rutPaciente;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
