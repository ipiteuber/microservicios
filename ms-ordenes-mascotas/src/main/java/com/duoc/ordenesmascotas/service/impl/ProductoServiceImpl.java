package com.duoc.ordenesmascotas.service.impl;

import com.duoc.ordenesmascotas.exception.ResourceNotFoundException;
import com.duoc.ordenesmascotas.model.Producto;
import com.duoc.ordenesmascotas.repository.ProductoRepository;
import com.duoc.ordenesmascotas.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro producto con ID: " + id));
    }

    @Override
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoriaIgnoreCase(categoria);
    }

    @Override
    @Transactional
    public Producto crearProducto(Producto producto) {
        producto.setId(null);
        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado con ID {}", guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public Producto actualizarProducto(Long id, Producto producto) {
        Producto existente = buscarPorId(id);
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setCategoria(producto.getCategoria());
        existente.setStock(producto.getStock());
        return productoRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro producto con ID: " + id);
        }
        productoRepository.deleteById(id);
        log.info("Producto {} eliminado", id);
    }
}
