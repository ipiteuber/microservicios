package com.duoc.ordenesmascotas.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.duoc.ordenesmascotas.model.ItemOrden;
import com.duoc.ordenesmascotas.model.OrdenCompra;

/**
 * Servicio para gestionar ordenes de compra en memoria.
 * Provee operaciones CRUD y manejo de estado.
 */
@Service
public class OrdenService {

    private final List<OrdenCompra> ordenes = new ArrayList<>();
    private final AtomicLong ordenIdCounter = new AtomicLong(3);
    private final AtomicLong itemIdCounter = new AtomicLong(7);

    /** Inicializa ordenes de ejemplo en distintos estados. */
    public OrdenService() {
        // Orden 1 - PENDIENTE
        List<ItemOrden> items1 = new ArrayList<>(Arrays.asList(
                new ItemOrden(1L, 1L, "Alimento Premium para Perros", 2, 32990.0, 65980.0),
                new ItemOrden(2L, 7L, "Snacks Dentales", 3, 5990.0, 17970.0)
        ));
        ordenes.add(new OrdenCompra(1L, "Maria Gonzalez", "maria.gonzalez@mail.cl",
                "2026-03-28", items1, 83950.0, "PENDIENTE"));

        // Orden 2 - CONFIRMADA
        List<ItemOrden> items2 = new ArrayList<>(Arrays.asList(
                new ItemOrden(3L, 3L, "Collar Antipulgas", 1, 15990.0, 15990.0),
                new ItemOrden(4L, 4L, "Shampoo para Gatos", 2, 6990.0, 13980.0)
        ));
        ordenes.add(new OrdenCompra(2L, "Carlos Muñoz", "carlos.munoz@mail.cl",
                "2026-03-25", items2, 29970.0, "CONFIRMADA"));

        // Orden 3 - CANCELADA
        List<ItemOrden> items3 = new ArrayList<>(Arrays.asList(
                new ItemOrden(5L, 5L, "Cama para Mascotas", 1, 24990.0, 24990.0),
                new ItemOrden(6L, 8L, "Transportador de Mascotas", 1, 45990.0, 45990.0)
        ));
        ordenes.add(new OrdenCompra(3L, "Andrea Lopez", "andrea.lopez@mail.cl",
                "2026-03-20", items3, 70980.0, "CANCELADA"));
    }

    // Devuelve todas las ordenes
    public List<OrdenCompra> listarOrdenes() {
        return ordenes;
    }

    // Busca orden por id
    public Optional<OrdenCompra> buscarPorId(Long id) {
        return ordenes.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    // Valida y crea una orden; retorna lista de errores (vacia si ok)
    public List<String> crearOrden(OrdenCompra orden) {
        List<String> errores = new ArrayList<>();
        // Valida nombre cliente
        if (orden.getNombreCliente() == null || orden.getNombreCliente().isBlank()) {
            errores.add("El nombre del cliente es obligatorio.");
        }

        // Valida email cliente
        if (orden.getEmailCliente() == null || orden.getEmailCliente().isBlank()) {
            errores.add("El email del cliente es obligatorio.");
        } else if (!orden.getEmailCliente().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            errores.add("El formato del email no es valido.");
        }

        // Valida que la orden tenga items
        if (orden.getItems() == null || orden.getItems().isEmpty()) {
            errores.add("La orden debe contener al menos un item.");
        } else {
            // Valida cantidad y precio de cada item
            for (int i = 0; i < orden.getItems().size(); i++) {
                ItemOrden item = orden.getItems().get(i);
                if (item.getCantidad() <= 0) {
                    errores.add("El item " + (i + 1) + " debe tener una cantidad mayor a 0.");
                }
                if (item.getPrecioUnitario() < 0) {
                    errores.add("El item " + (i + 1) + " tiene un precio unitario negativo.");
                }
            }
        }

        // Si hay errores, devuelve sin persistir
        if (!errores.isEmpty()) {
            return errores;
        }

        // Asigna ids y calcula subtotales/total
        orden.setId(ordenIdCounter.incrementAndGet());
        orden.setFechaCreacion(LocalDate.now().toString());
        orden.setEstado("PENDIENTE");

        double total = 0.0;
        for (ItemOrden item : orden.getItems()) {
            item.setId(itemIdCounter.incrementAndGet());
            item.setSubtotal(item.getPrecioUnitario() * item.getCantidad());
            total += item.getSubtotal();
        }
        orden.setTotal(total);

        ordenes.add(orden);
        return errores; // lista vacia indica exito
    }

    // Cancela una orden si esta en PENDIENTE
    public String cancelarOrden(Long id) {
        Optional<OrdenCompra> optional = buscarPorId(id);
        if (optional.isEmpty()) {
            return "ORDEN_NO_ENCONTRADA";
        }

        OrdenCompra orden = optional.get();
        if (!"PENDIENTE".equals(orden.getEstado())) {
            return "SOLO_PENDIENTE";
        }

        orden.setEstado("CANCELADA");
        return "CANCELADA_OK";
    }

    // Obtiene estado actual de una orden por id
    public Optional<String> obtenerEstado(Long id) {
        return buscarPorId(id).map(OrdenCompra::getEstado);
    }
}
