package com.duoc.citasmedicas.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.duoc.citasmedicas.model.Doctor;
import com.duoc.citasmedicas.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;

// Pruebas unitarias del controlador de Doctor
@SpringBootTest
@AutoConfigureMockMvc
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    private Doctor doctorEjemplo;

    @BeforeEach
    void setUp() {
        // Setup que se ejecuta antes de cada test
        doctorEjemplo = new Doctor();
        doctorEjemplo.setId(1L);
        doctorEjemplo.setNombre("Dra. Maria Gonzalez");
        doctorEjemplo.setEspecialidad("Medicina General");
        doctorEjemplo.setHorarioInicio("08:00");
        doctorEjemplo.setHorarioFin("16:00");
        doctorEjemplo.setDisponible(true);
    }

    @AfterEach
    void tearDown() {
        // Limpieza despues de cada test
        doctorEjemplo = null;
    }

    @Test
    void listarDoctores_debeRetornarListaCon200() throws Exception {
        // Given
        List<Doctor> doctores = Arrays.asList(doctorEjemplo);
        when(doctorService.listarDoctores()).thenReturn(doctores);

        // When + Then
        mockMvc.perform(get("/api/doctores").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.doctorList[0].nombre")
                        .value("Dra. Maria Gonzalez"));
    }

    @Test
    void crearDoctor_debeRetornar201Y_devolverDoctorCreado() throws Exception {
        // Given
        when(doctorService.crearDoctor(any(Doctor.class))).thenReturn(doctorEjemplo);

        // When + Then
        mockMvc.perform(post("/api/doctores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorEjemplo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Dra. Maria Gonzalez"))
                .andExpect(jsonPath("$.especialidad").value("Medicina General"));
    }

    @Test
    void actualizarDoctor_cuandoExiste_retorna200ConDatosActualizados() throws Exception {
        // Given - Doctor con datos actualizados
        Doctor doctorActualizado = new Doctor();
        doctorActualizado.setId(1L);
        doctorActualizado.setNombre("Dra. Maria Gonzalez Actualizada");
        doctorActualizado.setEspecialidad("Cardiologia");
        doctorActualizado.setHorarioInicio("09:00");
        doctorActualizado.setHorarioFin("18:00");
        doctorActualizado.setDisponible(true);

        when(doctorService.actualizarDoctor(any(Long.class), any(Doctor.class)))
                .thenReturn(doctorActualizado);

        // When + Then
        mockMvc.perform(put("/api/doctores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Dra. Maria Gonzalez Actualizada"))
                .andExpect(jsonPath("$.especialidad").value("Cardiologia"));
    }
}
