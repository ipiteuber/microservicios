package com.duoc.ordenesmascotas.service;

import com.duoc.ordenesmascotas.model.OrdenCompra;

import java.util.List;

// Contrato del servicio de ordenes de compra
public interface OrdenService {

    List<OrdenCompra> listarOrdenes();

    OrdenCompra buscarPorId(Long id);

    List<OrdenCompra> buscarPorEstado(String estado);

    String obtenerEstado(Long id);

    OrdenCompra crearOrden(OrdenCompra orden);

    OrdenCompra actualizarOrden(Long id, OrdenCompra orden);

    OrdenCompra cancelarOrden(Long id);

    OrdenCompra confirmarOrden(Long id);

    void eliminarOrden(Long id);
}
