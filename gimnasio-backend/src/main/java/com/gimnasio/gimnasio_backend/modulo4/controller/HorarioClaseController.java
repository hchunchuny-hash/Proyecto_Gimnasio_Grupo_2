package com.gimnasio.gimnasio_backend.modulo4.controller;

import com.gimnasio.gimnasio_backend.modulo4.dto.HorarioClaseDTO;
import com.gimnasio.gimnasio_backend.modulo4.service.HorarioClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases/horarios")
public class HorarioClaseController {

    @Autowired
    private HorarioClaseService horarioService;

    // GET: http://localhost:8080/api/clases/horarios (horarios desde hoy en adelante)
    @GetMapping
    public ResponseEntity<List<HorarioClaseDTO.Response>> listarProximos() {
        return ResponseEntity.ok(horarioService.listarProximos());
    }

    // GET: http://localhost:8080/api/clases/horarios/clase/1
    @GetMapping("/clase/{claseId}")
    public ResponseEntity<List<HorarioClaseDTO.Response>> listarPorClase(@PathVariable Integer claseId) {
        return ResponseEntity.ok(horarioService.listarPorClase(claseId));
    }

    // POST: http://localhost:8080/api/clases/horarios
    @PostMapping
    public ResponseEntity<HorarioClaseDTO.Response> crear(@RequestBody HorarioClaseDTO.Request request) {
        return ResponseEntity.ok(horarioService.crear(request));
    }
}
