package com.duoc.citasmedicas.model;

/**
 * Modelo que representa un doctor.
 * Contiene nombre, especialidad, horario y disponibilidad.
 */
public class Doctor {

    private Long id;
    private String nombre;
    private String especialidad;
    private String horarioInicio;
    private String horarioFin;
    private boolean disponible;

    /** Constructor vacio. */
    public Doctor() {
    }

    /** Constructor completo. */
    public Doctor(Long id, String nombre, String especialidad, String horarioInicio, String horarioFin, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(String horarioFin) {
        this.horarioFin = horarioFin;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
