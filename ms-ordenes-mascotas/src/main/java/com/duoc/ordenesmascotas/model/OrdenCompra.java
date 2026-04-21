package com.duoc.ordenesmascotas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Entidad OrdenCompra - tabla ORDEN_COMPRA
// Estados: PENDIENTE (default) -> CONFIRMADA / CANCELADA
@Entity
@Table(name = "orden_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 120)
    @Column(name = "nombre_cliente", nullable = false, length = 120)
    private String nombreCliente;

    @NotBlank(message = "El email del cliente es obligatorio")
    @Email(message = "Formato de email no valido")
    @Size(max = 120)
    @Column(name = "email_cliente", nullable = false, length = 120)
    private String emailCliente;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    // OneToMany con cascade ALL: al guardar la orden se guardan los items,
    // al eliminarla se eliminan tambien (orphanRemoval).
    // @JsonManagedReference + @JsonBackReference en ItemOrden evitan el loop en JSON.
    @Valid
    @NotEmpty(message = "La orden debe tener al menos un item")
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ItemOrden> items = new ArrayList<>();
}
