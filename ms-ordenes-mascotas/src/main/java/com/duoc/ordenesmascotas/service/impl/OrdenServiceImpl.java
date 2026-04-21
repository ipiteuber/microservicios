package com.duoc.ordenesmascotas.service.impl;

import com.duoc.ordenesmascotas.exception.BusinessValidationException;
import com.duoc.ordenesmascotas.exception.ResourceNotFoundException;
import com.duoc.ordenesmascotas.model.ItemOrden;
import com.duoc.ordenesmascotas.model.OrdenCompra;
import com.duoc.ordenesmascotas.model.Producto;
import com.duoc.ordenesmascotas.repository.OrdenCompraRepository;
import com.duoc.ordenesmascotas.service.OrdenService;
import com.duoc.ordenesmascotas.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class OrdenServiceImpl implements OrdenService {

    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_CONFIRMADA = "CONFIRMADA";
    private static final String ESTADO_CANCELADA = "CANCELADA";

    private final OrdenCompraRepository ordenRepository;
    private final ProductoService productoService;

    public OrdenServiceImpl(OrdenCompraRepository ordenRepository, ProductoService productoService) {
        this.ordenRepository = ordenRepository;
        this.productoService = productoService;
    }

    @Override
    public List<OrdenCompra> listarOrdenes() {
        return ordenRepository.findAll();
    }

    @Override
    public OrdenCompra buscarPorId(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro orden con ID: " + id));
    }

    @Override
    public List<OrdenCompra> buscarPorEstado(String estado) {
        return ordenRepository.findByEstado(estado.toUpperCase());
    }

    @Override
    public String obtenerEstado(Long id) {
        return buscarPorId(id).getEstado();
    }

    @Override
    @Transactional
    public OrdenCompra crearOrden(OrdenCompra orden) {
        // Valores por defecto: fecha actual y estado PENDIENTE
        orden.setId(null);
        orden.setFechaCreacion(LocalDate.now());
        orden.setEstado(ESTADO_PENDIENTE);

        double total = 0.0;
        for (ItemOrden item : orden.getItems()) {
            Producto producto = productoService.buscarPorId(item.getProductoId());

            if (item.getCantidad() > producto.getStock()) {
                throw new BusinessValidationException(
                        "Stock insuficiente para '" + producto.getNombre() + "'. Disponible: "
                                + producto.getStock() + ", solicitado: " + item.getCantidad());
            }

            // Usamos el precio actual del catalogo, no el que manda el cliente
            item.setId(null);
            item.setNombreProducto(producto.getNombre());
            item.setPrecioUnitario(producto.getPrecio());
            item.setSubtotal(producto.getPrecio() * item.getCantidad());
            item.setOrden(orden);

            total += item.getSubtotal();
        }
        orden.setTotal(total);

        OrdenCompra guardada = ordenRepository.save(orden);
        log.info("Orden creada con ID {} total ${}", guardada.getId(), guardada.getTotal());
        return guardada;
    }

    @Override
    @Transactional
    public OrdenCompra actualizarOrden(Long id, OrdenCompra ordenActualizada) {
        OrdenCompra existente = buscarPorId(id);

        if (!ESTADO_PENDIENTE.equals(existente.getEstado())) {
            throw new BusinessValidationException(
                    "Solo se pueden modificar ordenes PENDIENTE. Estado actual: " + existente.getEstado());
        }

        existente.setNombreCliente(ordenActualizada.getNombreCliente());
        existente.setEmailCliente(ordenActualizada.getEmailCliente());

        // Vacia y vuelve a llenar los items (orphanRemoval borra los viejos en BD)
        existente.getItems().clear();
        double total = 0.0;
        for (ItemOrden nuevo : ordenActualizada.getItems()) {
            Producto producto = productoService.buscarPorId(nuevo.getProductoId());
            nuevo.setId(null);
            nuevo.setNombreProducto(producto.getNombre());
            nuevo.setPrecioUnitario(producto.getPrecio());
            nuevo.setSubtotal(producto.getPrecio() * nuevo.getCantidad());
            nuevo.setOrden(existente);
            existente.getItems().add(nuevo);
            total += nuevo.getSubtotal();
        }
        existente.setTotal(total);

        return ordenRepository.save(existente);
    }

    @Override
    @Transactional
    public OrdenCompra cancelarOrden(Long id) {
        OrdenCompra orden = buscarPorId(id);
        if (!ESTADO_PENDIENTE.equals(orden.getEstado())) {
            throw new BusinessValidationException(
                    "Solo se pueden cancelar ordenes PENDIENTE. Estado actual: " + orden.getEstado());
        }
        orden.setEstado(ESTADO_CANCELADA);
        log.info("Orden {} cancelada", id);
        return ordenRepository.save(orden);
    }

    @Override
    @Transactional
    public OrdenCompra confirmarOrden(Long id) {
        OrdenCompra orden = buscarPorId(id);
        if (!ESTADO_PENDIENTE.equals(orden.getEstado())) {
            throw new BusinessValidationException(
                    "Solo se pueden confirmar ordenes PENDIENTE. Estado actual: " + orden.getEstado());
        }
        orden.setEstado(ESTADO_CONFIRMADA);
        log.info("Orden {} confirmada", id);
        return ordenRepository.save(orden);
    }

    @Override
    @Transactional
    public void eliminarOrden(Long id) {
        if (!ordenRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro orden con ID: " + id);
        }
        ordenRepository.deleteById(id);
        log.info("Orden {} eliminada", id);
    }
}
