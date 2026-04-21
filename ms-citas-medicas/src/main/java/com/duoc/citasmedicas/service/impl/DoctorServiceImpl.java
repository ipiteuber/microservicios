package com.duoc.citasmedicas.service.impl;

import com.duoc.citasmedicas.exception.ResourceNotFoundException;
import com.duoc.citasmedicas.model.Doctor;
import com.duoc.citasmedicas.repository.DoctorRepository;
import com.duoc.citasmedicas.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Implementacion del servicio de doctores, usa DoctorRepository de Spring Data JPA
@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Doctor> listarDoctores() {
        List<Doctor> doctores = doctorRepository.findAll();
        log.debug("Se obtuvieron {} doctores", doctores.size());
        return doctores;
    }

    @Override
    public Doctor buscarPorId(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro doctor con ID: " + id));
    }

    @Override
    public List<Doctor> buscarPorEspecialidad(String especialidad) {
        return doctorRepository.findByEspecialidadIgnoreCase(especialidad);
    }

    @Override
    @Transactional
    public Doctor crearDoctor(Doctor doctor) {
        doctor.setId(null); // para que Oracle genere el ID nuevo
        Doctor guardado = doctorRepository.save(doctor);
        log.info("Doctor creado con ID {}", guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public Doctor actualizarDoctor(Long id, Doctor doctor) {
        Doctor existente = buscarPorId(id);
        existente.setNombre(doctor.getNombre());
        existente.setEspecialidad(doctor.getEspecialidad());
        existente.setHorarioInicio(doctor.getHorarioInicio());
        existente.setHorarioFin(doctor.getHorarioFin());
        existente.setDisponible(doctor.getDisponible());
        return doctorRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminarDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontro doctor con ID: " + id);
        }
        doctorRepository.deleteById(id);
        log.info("Doctor {} eliminado", id);
    }
}
