package com.duoc.ordenesmascotas.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.ordenesmascotas.model.OrdenCompra;
import com.duoc.ordenesmascotas.service.OrdenService;

/**
 * Controlador REST para ordenes.
 * Endpoints para crear, listar y gestionar ordenes.
 */
@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    // Lista todas las ordenes
    @GetMapping
    public ResponseEntity<List<OrdenCompra>> listarOrdenes() {
        return ResponseEntity.ok(ordenService.listarOrdenes());
    }

    // Obtiene una orden por id
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        return ordenService.buscarPorId(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "Orden con id " + id + " no encontrada.")));
    }

    // Obtiene el estado actual de una orden
    @GetMapping("/{id}/estado")
    public ResponseEntity<Object> obtenerEstado(@PathVariable Long id) {
        Optional<String> estado = ordenService.obtenerEstado(id);
        if (estado.isPresent()) {
            return ResponseEntity.ok(Map.of("ordenId", id, "estado", estado.get()));
        }
        return ResponseEntity.status(404)
                .body(Map.of("error", "Orden con id " + id + " no encontrada."));
    }

    // Crea una orden nueva; valida los datos
    @PostMapping
    public ResponseEntity<Object> crearOrden(@RequestBody OrdenCompra orden) {
        List<String> errores = ordenService.crearOrden(orden);
        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errores", errores));
        }
        return ResponseEntity.status(201).body(orden);
    }

    // Cancela una orden en estado PENDIENTE
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Object> cancelarOrden(@PathVariable Long id) {
        String resultado = ordenService.cancelarOrden(id);

        return switch (resultado) {
            case "ORDEN_NO_ENCONTRADA" -> ResponseEntity.status(404)
                    .body(Map.of("error", "Orden con id " + id + " no encontrada."));
            case "SOLO_PENDIENTE" -> ResponseEntity.badRequest()
                    .body(Map.of("error", "Solo se pueden cancelar ordenes en estado PENDIENTE."));
            case "CANCELADA_OK" -> ResponseEntity.ok(
                    Map.of("mensaje", "Orden " + id + " cancelada exitosamente.", "ordenId", id, "estado", "CANCELADA"));
            default -> ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error inesperado al cancelar la orden."));
        };
    }
}
