package com.gimnasio.gimnasio_backend.modulo1.controller;

import com.gimnasio.gimnasio_backend.modulo1.entity.AreaGimnasio;
import com.gimnasio.gimnasio_backend.modulo1.service.AreaGimnasioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaGimnasioController {

    @Autowired
    private AreaGimnasioService areaService;

    @GetMapping
    public ResponseEntity<List<AreaGimnasio>> listarTodas() {
        return ResponseEntity.ok(areaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaGimnasio> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(areaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AreaGimnasio> crear(@RequestBody AreaGimnasio area) {
        return ResponseEntity.ok(areaService.crear(area));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaGimnasio> actualizar(@PathVariable Integer id, @RequestBody AreaGimnasio area) {
        return ResponseEntity.ok(areaService.actualizar(id, area));
    }
}