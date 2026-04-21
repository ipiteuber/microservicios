package com.duoc.ordenesmascotas.controller;

import com.duoc.ordenesmascotas.model.OrdenCompra;
import com.duoc.ordenesmascotas.service.OrdenService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// Controlador REST de ordenes de compra. Soporta confirmar/cancelar por estado.
@RestController
@RequestMapping("/api/ordenes")
@Slf4j
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    // Lista todas o filtradas por estado (ej: ?estado=PENDIENTE)
    @GetMapping
    public ResponseEntity<List<OrdenCompra>> listarOrdenes(
            @RequestParam(required = false) String estado) {

        List<OrdenCompra> ordenes = (estado == null || estado.isBlank())
                ? ordenService.listarOrdenes()
                : ordenService.buscarPorEstado(estado);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> obtenerEstado(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of(
                "ordenId", id,
                "estado", ordenService.obtenerEstado(id)
        ));
    }

    @PostMapping
    public ResponseEntity<OrdenCompra> crearOrden(@Valid @RequestBody OrdenCompra orden) {
        log.info("POST /api/ordenes - cliente: {}", orden.getNombreCliente());
        OrdenCompra creada = ordenService.crearOrden(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenCompra> actualizarOrden(@PathVariable Long id,
                                                       @Valid @RequestBody OrdenCompra orden) {
        log.info("PUT /api/ordenes/{}", id);
        return ResponseEntity.ok(ordenService.actualizarOrden(id, orden));
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<OrdenCompra> confirmarOrden(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.confirmarOrden(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<OrdenCompra> cancelarOrden(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.cancelarOrden(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
        log.info("DELETE /api/ordenes/{}", id);
        ordenService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }
}
