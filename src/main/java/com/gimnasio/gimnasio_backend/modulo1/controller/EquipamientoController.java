package com.gimnasio.gimnasio_backend.modulo1.controller;

import com.gimnasio.gimnasio_backend.modulo1.dto.EquipamientoDTO;
import com.gimnasio.gimnasio_backend.modulo1.service.EquipamientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamiento")
public class EquipamientoController {

    @Autowired
    private EquipamientoService equipamientoService;

    @GetMapping
    public ResponseEntity<List<EquipamientoDTO.Response>> listarTodos() {
        return ResponseEntity.ok(equipamientoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamientoDTO.Response> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(equipamientoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EquipamientoDTO.Response> crear(@Valid @RequestBody EquipamientoDTO.Request request) {
        return ResponseEntity.ok(equipamientoService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamientoDTO.Response> actualizar(@PathVariable Integer id, @Valid @RequestBody EquipamientoDTO.Request request) {
        return ResponseEntity.ok(equipamientoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        equipamientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}