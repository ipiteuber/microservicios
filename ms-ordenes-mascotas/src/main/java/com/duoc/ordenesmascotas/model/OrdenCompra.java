package com.duoc.ordenesmascotas.model;

import java.util.List;

/**
 * Modelo que representa una orden de compra con sus items.
 */
public class OrdenCompra {

    private Long id;
    private String nombreCliente;
    private String emailCliente;
    private String fechaCreacion;
    private List<ItemOrden> items;
    private double total;
    private String estado; // PENDIENTE, CONFIRMADA, CANCELADA

    /** Constructor vacio. */
    public OrdenCompra() {
    }

    /** Constructor completo. */
    public OrdenCompra(Long id, String nombreCliente, String emailCliente, String fechaCreacion,
                       List<ItemOrden> items, double total, String estado) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.emailCliente = emailCliente;
        this.fechaCreacion = fechaCreacion;
        this.items = items;
        this.total = total;
        this.estado = estado;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<ItemOrden> getItems() {
        return items;
    }

    public void setItems(List<ItemOrden> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
