package com.gimnasio.gimnasio_backend.modulo2.controller;

import com.gimnasio.gimnasio_backend.modulo2.dto.EntrenadorDTO;
import com.gimnasio.gimnasio_backend.modulo2.service.EntrenadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorController {

    @Autowired
    private EntrenadorService entrenadorService;

    @GetMapping
    public ResponseEntity<List<EntrenadorDTO.Response>> listarTodos() {
        return ResponseEntity.ok(entrenadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenadorDTO.Response> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(entrenadorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EntrenadorDTO.Response> crear(@Valid @RequestBody EntrenadorDTO.Request request) {
        return ResponseEntity.ok(entrenadorService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrenadorDTO.Response> actualizar(@PathVariable Integer id, @Valid @RequestBody EntrenadorDTO.Request request) {
        return ResponseEntity.ok(entrenadorService.actualizar(id, request));
    }
}