package com.duoc.ordenesmascotas.controller;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.duoc.ordenesmascotas.model.Producto;
import com.duoc.ordenesmascotas.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

// Pruebas unitarias del controlador de Producto
@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    private Producto productoEjemplo;

    @BeforeEach
    void setUp() {
        productoEjemplo = new Producto();
        productoEjemplo.setId(1L);
        productoEjemplo.setNombre("Alimento Premium para Perros");
        productoEjemplo.setDescripcion("Alimento seco premium, saco de 15 kg.");
        productoEjemplo.setPrecio(32990.0);
        productoEjemplo.setCategoria("Alimentacion");
        productoEjemplo.setStock(50);
    }

    @AfterEach
    void tearDown() {
        productoEjemplo = null;
    }

    @Test
    void listarProductos_debeRetornar200ConJSON() throws Exception {
        when(productoService.listarProductos()).thenReturn(Arrays.asList(productoEjemplo));

        mockMvc.perform(get("/api/productos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productoList[0].nombre")
                        .value("Alimento Premium para Perros"));
    }

    @Test
    void crearProducto_debeRetornar201Y_devolverProductoCreado() throws Exception {
        when(productoService.crearProducto(any(Producto.class))).thenReturn(productoEjemplo);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoEjemplo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Alimento Premium para Perros"))
                .andExpect(jsonPath("$.precio").value(32990.0));
    }

    @Test
    void eliminarProducto_cuandoExiste_retorna204NoContent() throws Exception {
        // Given - Servicio no lanza excepcion al eliminar
        doNothing().when(productoService).eliminarProducto(1L);

        // When + Then
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }
}
