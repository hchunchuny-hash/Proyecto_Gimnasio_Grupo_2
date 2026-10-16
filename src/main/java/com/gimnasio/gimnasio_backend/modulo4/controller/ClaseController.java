package com.gimnasio.gimnasio_backend.modulo4.controller;

import com.gimnasio.gimnasio_backend.modulo4.dto.ClaseDTO;
import com.gimnasio.gimnasio_backend.modulo4.service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.gimnasio.gimnasio_backend.security.AuthenticatedUser;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    @Autowired
    private ClaseService claseService;
    @Autowired private ClienteRepository clientes;

    // GET: http://localhost:8080/api/clases
    @GetMapping
    public ResponseEntity<List<ClaseDTO.Response>> listarTodas() {
        return ResponseEntity.ok(claseService.listarTodas());
    }

    // POST: http://localhost:8080/api/clases
    @PostMapping
    public ResponseEntity<ClaseDTO.Response> crear(@RequestBody ClaseDTO.Request request) {
        return ResponseEntity.ok(claseService.crear(request));
    }

    // POST: http://localhost:8080/api/clases/inscripciones
    @PostMapping("/inscripciones")
    public ResponseEntity<ClaseDTO.InscripcionResponse> inscribir(@RequestBody ClaseDTO.InscribirRequest request, @AuthenticationPrincipal AuthenticatedUser user) {
        if ("CLIENTE".equalsIgnoreCase(user.rol())) request.setClienteId(clientes.findByUsuarioUsuarioId(user.usuarioId()).orElseThrow().getClienteId());
        return ResponseEntity.ok(claseService.inscribir(request));
    }

    // GET: http://localhost:8080/api/clases/inscripciones/cliente/1
    @GetMapping("/inscripciones/cliente/{clienteId}")
    public ResponseEntity<List<ClaseDTO.InscripcionResponse>> inscripcionesPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(claseService.listarInscripcionesPorCliente(clienteId));
    }

    // PUT: http://localhost:8080/api/clases/inscripciones/1/cancelar
    @PutMapping("/inscripciones/{id}/cancelar")
    public ResponseEntity<ClaseDTO.InscripcionResponse> cancelarInscripcion(@PathVariable Integer id) {
        return ResponseEntity.ok(claseService.cancelarInscripcion(id));
    }
}
