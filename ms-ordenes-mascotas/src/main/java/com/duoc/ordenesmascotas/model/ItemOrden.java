package com.duoc.ordenesmascotas.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Item de una orden - tabla ITEM_ORDEN
@Entity
@Table(name = "item_orden")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "orden") // para evitar loop infinito con la orden padre
public class ItemOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK a la orden. @JsonBackReference evita el loop al serializar a JSON
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    @JsonBackReference
    private OrdenCompra orden;

    @NotNull(message = "El ID del producto es obligatorio")
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    // Se llena en el service desde el producto
    @Column(name = "nombre_producto", length = 120)
    private String nombreProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @PositiveOrZero
    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    // cantidad * precioUnitario - lo calcula el service
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;
}
