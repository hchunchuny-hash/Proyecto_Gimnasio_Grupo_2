package com.gimnasio.gimnasio_backend.modulo3.controller;

import com.gimnasio.gimnasio_backend.modulo3.dto.EjercicioDTO;
import com.gimnasio.gimnasio_backend.modulo3.service.EjercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejercicios")
public class EjercicioController {

    @Autowired
    private EjercicioService ejercicioService;

    @GetMapping
    public ResponseEntity<List<EjercicioDTO.Response>> listar() {
        return ResponseEntity.ok(ejercicioService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<EjercicioDTO.Response> crear(@RequestBody EjercicioDTO.Request request) {
        return ResponseEntity.ok(ejercicioService.crear(request));
    }
}