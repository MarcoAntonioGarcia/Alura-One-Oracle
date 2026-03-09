package com.aluracursos.forumhub.controller;

import com.aluracursos.forumhub.domain.ValidacionException;
import com.aluracursos.forumhub.domain.topico.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> crearTopico(
            @RequestBody @Valid DatosRegistroTopico datosRegistro,
            UriComponentsBuilder uriBuilder) {

        if (topicoRepository.existsByTituloAndMensaje(datosRegistro.titulo(), datosRegistro.mensaje())) {
            throw new ValidacionException("Ya existe un tópico con el mismo título y mensaje.");
        }

        var topico = new Topico(datosRegistro);
        topicoRepository.save(topico);

        var response = new DatosRespuestaTopico(topico);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaTopico>> listarTopicos(
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion) {
        var page = topicoRepository.findAll(paginacion).map(DatosRespuestaTopico::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> retornarTopico(@PathVariable Long id) {
        var topico = topicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datosActualizar) {
        var topico = topicoRepository.getReferenceById(id);

        if (datosActualizar.titulo() != null && datosActualizar.mensaje() != null) {
            if (topicoRepository.existsByTituloAndMensaje(datosActualizar.titulo(), datosActualizar.mensaje())) {
                throw new ValidacionException("Ya existe un tópico con el mismo título y mensaje.");
            }
        }

        topico.actualizarDatos(datosActualizar);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        var topico = topicoRepository.getReferenceById(id);
        topicoRepository.delete(topico);
        return ResponseEntity.noContent().build();
    }
}
