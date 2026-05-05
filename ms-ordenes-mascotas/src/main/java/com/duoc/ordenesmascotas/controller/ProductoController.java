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

import com.duoc.ordenesmascotas.model.Producto;
import com.duoc.ordenesmascotas.service.ProductoService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/productos")
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public CollectionModel<EntityModel<Producto>> listarProductos() {
        List<EntityModel<Producto>> productos = productoService.listarProductos().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Producto> buscarPorId(@PathVariable Long id) {
        return toEntityModel(productoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Producto>> crearProducto(@Valid @RequestBody Producto producto) {
        log.info("POST /api/productos - {}", producto.getNombre());
        Producto creado = productoService.crearProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toEntityModel(creado));
    }

    @PutMapping("/{id}")
    public EntityModel<Producto> actualizarProducto(@PathVariable Long id,
            @Valid @RequestBody Producto producto) {
        log.info("PUT /api/productos/{}", id);
        return toEntityModel(productoService.actualizarProducto(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        log.info("DELETE /api/productos/{}", id);
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Producto> toEntityModel(Producto producto) {
        return EntityModel.of(producto,

                linkTo(methodOn(ProductoController.class).buscarPorId(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("todos"));
    }
}