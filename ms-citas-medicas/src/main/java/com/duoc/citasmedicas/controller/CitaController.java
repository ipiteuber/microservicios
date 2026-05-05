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

  import com.duoc.citasmedicas.model.CitaMedica;
  import com.duoc.citasmedicas.service.CitaService;

  import jakarta.validation.Valid;
  import lombok.extern.slf4j.Slf4j;

  @RestController
  @RequestMapping("/api/citas")
  @Slf4j
  public class CitaController {

      private final CitaService citaService;

      public CitaController(CitaService citaService) {
          this.citaService = citaService;
      }

      @GetMapping
      public CollectionModel<EntityModel<CitaMedica>> listarCitas() {
          List<EntityModel<CitaMedica>> citas = citaService.listarCitas().stream()
                  .map(this::toEntityModel)
                  .collect(Collectors.toList());

          return CollectionModel.of(citas,
                  linkTo(methodOn(CitaController.class).listarCitas()).withSelfRel());
      }

      @GetMapping("/{id}")
      public EntityModel<CitaMedica> buscarPorId(@PathVariable Long id) {
          return toEntityModel(citaService.buscarPorId(id));
      }

      @PostMapping
      public ResponseEntity<EntityModel<CitaMedica>> programarCita(@Valid @RequestBody CitaMedica
  cita) {
          log.info("POST /api/citas");
          CitaMedica programada = citaService.programarCita(cita);
          return ResponseEntity.status(HttpStatus.CREATED).body(toEntityModel(programada));
      }

      @PutMapping("/{id}")
      public EntityModel<CitaMedica> actualizarCita(@PathVariable Long id,
                                                    @Valid @RequestBody CitaMedica cita) {
          log.info("PUT /api/citas/{}", id);
          return toEntityModel(citaService.actualizarCita(id, cita));
      }

      @PutMapping("/{id}/cancelar")
      public EntityModel<CitaMedica> cancelarCita(@PathVariable Long id) {
          log.info("Cancelando cita {}", id);
          return toEntityModel(citaService.cancelarCita(id));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
          log.info("DELETE /api/citas/{}", id);
          citaService.eliminarCita(id);
          return ResponseEntity.noContent().build();
      }

      private EntityModel<CitaMedica> toEntityModel(CitaMedica cita) {
          return EntityModel.of(cita,
                  linkTo(methodOn(CitaController.class).buscarPorId(cita.getId())).withSelfRel(),
                  linkTo(methodOn(CitaController.class).listarCitas()).withRel("todas"),

  linkTo(methodOn(CitaController.class).cancelarCita(cita.getId())).withRel("cancelar"));
      }
}