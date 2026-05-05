package com.duoc.ordenesmascotas.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.ordenesmascotas.model.OrdenCompra;
import com.duoc.ordenesmascotas.service.OrdenService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/ordenes")
@Slf4j
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping
    public CollectionModel<EntityModel<OrdenCompra>> listarOrdenes() {
        List<EntityModel<OrdenCompra>> ordenes = ordenService.listarOrdenes().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        return CollectionModel.of(ordenes,
                linkTo(methodOn(OrdenController.class).listarOrdenes()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<OrdenCompra> buscarPorId(@PathVariable Long id) {
        return toEntityModel(ordenService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<OrdenCompra>> crearOrden(@Valid @RequestBody OrdenCompra orden) {
        log.info("POST /api/ordenes");
        OrdenCompra creada = ordenService.crearOrden(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(toEntityModel(creada));
    }

    @PutMapping("/{id}")
    public EntityModel<OrdenCompra> actualizarOrden(@PathVariable Long id,
            @Valid @RequestBody OrdenCompra orden) {
        log.info("PUT /api/ordenes/{}", id);
        return toEntityModel(ordenService.actualizarOrden(id, orden));
    }

    @PutMapping("/{id}/confirmar")
    public EntityModel<OrdenCompra> confirmarOrden(@PathVariable Long id) {
        return toEntityModel(ordenService.confirmarOrden(id));
    }

    @PutMapping("/{id}/cancelar")
    public EntityModel<OrdenCompra> cancelarOrden(@PathVariable Long id) {
        return toEntityModel(ordenService.cancelarOrden(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
        log.info("DELETE /api/ordenes/{}", id);
        ordenService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<OrdenCompra> toEntityModel(OrdenCompra orden) {
        return EntityModel.of(orden,
                linkTo(methodOn(OrdenController.class).buscarPorId(orden.getId())).withSelfRel(),
                linkTo(methodOn(OrdenController.class).listarOrdenes()).withRel("todas"),

                linkTo(methodOn(OrdenController.class).confirmarOrden(orden.getId())).withRel("confirmar"),

                linkTo(methodOn(OrdenController.class).cancelarOrden(orden.getId())).withRel("cancelar"));
    }
}