package com.duoc.citasmedicas.service.impl;

import com.duoc.citasmedicas.exception.ResourceNotFoundException;
import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.repository.CitaMedicaRepository;
import com.duoc.citasmedicas.service.DoctorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

// Pruebas unitarias del servicio de citas medicas
@ExtendWith(MockitoExtension.class)
class CitaServiceImplTest {

    @Mock
    private CitaMedicaRepository citaRepository;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private CitaServiceImpl citaService;

    private CitaMedica citaEjemplo;

    @BeforeEach
    void setUp() {
        citaEjemplo = new CitaMedica();
        citaEjemplo.setId(1L);
        citaEjemplo.setNombrePaciente("Juan Perez Soto");
        citaEjemplo.setRutPaciente("12.345.678-9");
        citaEjemplo.setDoctorId(1L);
        citaEjemplo.setFecha(LocalDate.of(2026, 5, 15));
        citaEjemplo.setHora("09:00");
        citaEjemplo.setEstado("PROGRAMADA");
        citaEjemplo.setMotivo("Control general anual");
    }

    @AfterEach
    void tearDown() {
        citaEjemplo = null;
    }

    @Test
    void buscarPorId_cuandoExiste_retornaCita() {
        // Given
        when(citaRepository.findById(1L)).thenReturn(Optional.of(citaEjemplo));

        // When
        CitaMedica resultado = citaService.buscarPorId(1L);

        // Then
        assertEquals("Juan Perez Soto", resultado.getNombrePaciente());
        assertEquals("PROGRAMADA", resultado.getEstado());
    }

    @Test
    void buscarPorId_cuandoNoExiste_lanzaResourceNotFoundException() {
        // Given
        when(citaRepository.findById(999L)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(ResourceNotFoundException.class,
                () -> citaService.buscarPorId(999L));
    }
}
