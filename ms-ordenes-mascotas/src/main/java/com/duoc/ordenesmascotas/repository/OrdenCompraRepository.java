package com.duoc.ordenesmascotas.repository;

import com.duoc.ordenesmascotas.model.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio JPA de OrdenCompra
@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {

    List<OrdenCompra> findByEstado(String estado);

    List<OrdenCompra> findByEmailClienteIgnoreCase(String emailCliente);
}
