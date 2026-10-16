package com.gimnasio.gimnasio_backend.modulo3.controller;

import com.gimnasio.gimnasio_backend.modulo3.dto.RutinaDTO;
import com.gimnasio.gimnasio_backend.modulo3.service.RutinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaController {

    @Autowired
    private RutinaService rutinaService;

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<RutinaDTO.Response>> listarPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(rutinaService.listarPorCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<RutinaDTO.Response> crear(@RequestBody RutinaDTO.Request request) {
        return ResponseEntity.ok(rutinaService.crearRutina(request));
    }
}