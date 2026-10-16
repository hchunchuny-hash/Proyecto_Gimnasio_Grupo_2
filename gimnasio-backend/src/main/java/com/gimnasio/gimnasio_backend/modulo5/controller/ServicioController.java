package com.gimnasio.gimnasio_backend.modulo5.controller;

import com.gimnasio.gimnasio_backend.modulo5.dto.ServicioDTO;
import com.gimnasio.gimnasio_backend.modulo5.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    // GET: http://localhost:8080/api/servicios
    @GetMapping
    public ResponseEntity<List<ServicioDTO.Response>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    // GET: http://localhost:8080/api/servicios/1
    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO.Response> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    // POST: http://localhost:8080/api/servicios
    @PostMapping
    public ResponseEntity<ServicioDTO.Response> crear(@RequestBody ServicioDTO.Request request) {
        return ResponseEntity.ok(servicioService.crear(request));
    }

    // PUT: http://localhost:8080/api/servicios/1
    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTO.Response> actualizar(@PathVariable Integer id, @RequestBody ServicioDTO.Request request) {
        return ResponseEntity.ok(servicioService.actualizar(id, request));
    }
}
