package com.duoc.ordenesmascotas.service.impl;

import com.duoc.ordenesmascotas.exception.BusinessValidationException;
import com.duoc.ordenesmascotas.model.OrdenCompra;
import com.duoc.ordenesmascotas.repository.OrdenCompraRepository;
import com.duoc.ordenesmascotas.service.ProductoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Pruebas unitarias del servicio de ordenes de compra
@ExtendWith(MockitoExtension.class)
class OrdenServiceImplTest {

    @Mock
    private OrdenCompraRepository ordenRepository;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private OrdenServiceImpl ordenService;

    private OrdenCompra ordenEjemplo;

    @BeforeEach
    void setUp() {
        ordenEjemplo = new OrdenCompra();
        ordenEjemplo.setId(1L);
        ordenEjemplo.setNombreCliente("Maria Gonzalez");
        ordenEjemplo.setEmailCliente("maria.gonzalez@mail.cl");
        ordenEjemplo.setEstado("PENDIENTE");
        ordenEjemplo.setTotal(83950.0);
    }

    @AfterEach
    void tearDown() {
        ordenEjemplo = null;
    }

    @Test
    void confirmarOrden_cuandoPendiente_pasaACONFIRMADA() {
        // Given
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(ordenEjemplo));
        when(ordenRepository.save(any(OrdenCompra.class))).thenReturn(ordenEjemplo);

        // When
        OrdenCompra resultado = ordenService.confirmarOrden(1L);

        // Then
        assertEquals("CONFIRMADA", resultado.getEstado());
    }

    @Test
    void cancelarOrden_cuandoYaConfirmada_lanzaBusinessValidationException() {
        // Given
        ordenEjemplo.setEstado("CONFIRMADA");
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(ordenEjemplo));

        // When + Then
        assertThrows(BusinessValidationException.class,
                () -> ordenService.cancelarOrden(1L));
    }
}
