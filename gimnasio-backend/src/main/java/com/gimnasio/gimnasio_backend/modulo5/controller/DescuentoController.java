package com.gimnasio.gimnasio_backend.modulo5.controller;

import com.gimnasio.gimnasio_backend.modulo5.dto.DescuentoDTO;
import com.gimnasio.gimnasio_backend.modulo5.service.DescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descuentos")
public class DescuentoController {

    @Autowired
    private DescuentoService descuentoService;

    // GET: http://localhost:8080/api/descuentos
    @GetMapping
    public ResponseEntity<List<DescuentoDTO.Response>> listarTodos() {
        return ResponseEntity.ok(descuentoService.listarTodos());
    }

    // POST: http://localhost:8080/api/descuentos
    @PostMapping
    public ResponseEntity<DescuentoDTO.Response> crear(@RequestBody DescuentoDTO.Request request) {
        return ResponseEntity.ok(descuentoService.crear(request));
    }

    // PUT: http://localhost:8080/api/descuentos/1
    @PutMapping("/{id}")
    public ResponseEntity<DescuentoDTO.Response> actualizar(@PathVariable Integer id, @RequestBody DescuentoDTO.Request request) {
        return ResponseEntity.ok(descuentoService.actualizar(id, request));
    }
}
