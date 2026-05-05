package com.duoc.citasmedicas.controller;

  import java.util.List;
  import java.util.stream.Collectors;

  import org.springframework.hateoas.CollectionModel;
  import org.springframework.hateoas.EntityModel;
  import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
  import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.DeleteMapping;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.PutMapping;
  import org.springframework.web.bind.annotation.RequestBody;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;

  import com.duoc.citasmedicas.model.Doctor;
  import com.duoc.citasmedicas.service.DoctorService;

  import jakarta.validation.Valid;
  import lombok.extern.slf4j.Slf4j;

  @RestController
  @RequestMapping("/api/doctores")
  @Slf4j
  public class DoctorController {

      private final DoctorService doctorService;

      public DoctorController(DoctorService doctorService) {
          this.doctorService = doctorService;
      }

      @GetMapping
      public CollectionModel<EntityModel<Doctor>> listarDoctores() {
          log.info("GET /api/doctores");
          List<EntityModel<Doctor>> doctores = doctorService.listarDoctores().stream()
                  .map(this::toEntityModel)
                  .collect(Collectors.toList());

          return CollectionModel.of(doctores,
                  linkTo(methodOn(DoctorController.class).listarDoctores()).withSelfRel());
      }

      @GetMapping("/{id}")
      public EntityModel<Doctor> buscarPorId(@PathVariable Long id) {
          log.info("GET /api/doctores/{}", id);
          Doctor doctor = doctorService.buscarPorId(id);
          return toEntityModel(doctor);
      }

      @PostMapping
      public ResponseEntity<EntityModel<Doctor>> crearDoctor(@Valid @RequestBody Doctor doctor) {
          log.info("POST /api/doctores - {}", doctor.getNombre());
          Doctor creado = doctorService.crearDoctor(doctor);
          return ResponseEntity.status(HttpStatus.CREATED).body(toEntityModel(creado));
      }

      @PutMapping("/{id}")
      public EntityModel<Doctor> actualizarDoctor(@PathVariable Long id,
                                                  @Valid @RequestBody Doctor doctor) {
          log.info("PUT /api/doctores/{}", id);
          return toEntityModel(doctorService.actualizarDoctor(id, doctor));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Void> eliminarDoctor(@PathVariable Long id) {
          log.info("DELETE /api/doctores/{}", id);
          doctorService.eliminarDoctor(id);
          return ResponseEntity.noContent().build();
      }

      private EntityModel<Doctor> toEntityModel(Doctor doctor) {
          return EntityModel.of(doctor,
                  linkTo(methodOn(DoctorController.class).buscarPorId(doctor.getId())).withSelfRel(),
                  linkTo(methodOn(DoctorController.class).listarDoctores()).withRel("todos"));
      }
}