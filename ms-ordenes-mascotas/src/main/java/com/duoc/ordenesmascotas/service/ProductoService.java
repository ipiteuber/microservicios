package com.duoc.ordenesmascotas.service;

import com.duoc.ordenesmascotas.model.Producto;

import java.util.List;

// Contrato del servicio de productos
public interface ProductoService {

    List<Producto> listarProductos();

    Producto buscarPorId(Long id);

    List<Producto> buscarPorCategoria(String categoria);

    Producto crearProducto(Producto producto);

    Producto actualizarProducto(Long id, Producto producto);

    void eliminarProducto(Long id);
}
