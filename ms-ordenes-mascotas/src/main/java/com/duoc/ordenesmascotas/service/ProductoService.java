package com.duoc.ordenesmascotas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.duoc.ordenesmascotas.model.Producto;

/**
 * Servicio para gestionar productos en memoria.
 * Datos iniciales cargados en el constructor.
 */
@Service
public class ProductoService {

    private final List<Producto> productos = new ArrayList<>();

        /** Inicializa catalogo de productos con datos de ejemplo. */
        public ProductoService() {
        productos.add(new Producto(1L, "Alimento Premium para Perros",
                "Alimento seco premium para perros adultos, saco de 15 kg. Rico en proteinas y vitaminas.",
                32990.0, "Alimentacion", 50));

        productos.add(new Producto(2L, "Juguete Mordedor",
                "Juguete mordedor de caucho resistente para perros medianos y grandes.",
                8990.0, "Juguetes", 120));

        productos.add(new Producto(3L, "Collar Antipulgas",
                "Collar antipulgas y garrapatas para perros, proteccion por 8 meses.",
                15990.0, "Medicamentos", 75));

        productos.add(new Producto(4L, "Shampoo para Gatos",
                "Shampoo hipoalergenico especial para gatos, 500 ml. Sin parabenos.",
                6990.0, "Higiene", 90));

        productos.add(new Producto(5L, "Cama para Mascotas",
                "Cama acolchada lavable para mascotas tamano mediano, con funda removible.",
                24990.0, "Accesorios", 35));

        productos.add(new Producto(6L, "Arena para Gatos",
                "Arena aglomerante premium para gatos, bolsa de 10 kg. Control de olores.",
                11990.0, "Higiene", 60));

        productos.add(new Producto(7L, "Snacks Dentales",
                "Snacks dentales para perros que ayudan a mantener dientes limpios y sanos.",
                5990.0, "Alimentacion", 200));

        productos.add(new Producto(8L, "Transportador de Mascotas",
                "Transportador rigido aprobado para viajes aereos, tamano mediano.",
                45990.0, "Accesorios", 20));
    }

        // Devuelve todos los productos
        public List<Producto> listarProductos() {
                return productos;
        }

        // Busca producto por id
        public Optional<Producto> buscarPorId(Long id) {
                return productos.stream()
                                .filter(p -> p.getId().equals(id))
                                .findFirst();
        }

        // Filtra productos por categoria
        public List<Producto> buscarPorCategoria(String categoria) {
                return productos.stream()
                                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                                .collect(Collectors.toList());
        }
}
