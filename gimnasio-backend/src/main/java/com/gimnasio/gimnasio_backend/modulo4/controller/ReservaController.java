package com.gimnasio.gimnasio_backend.modulo4.controller;

import com.gimnasio.gimnasio_backend.modulo4.dto.ReservaDTO;
import com.gimnasio.gimnasio_backend.modulo4.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.gimnasio.gimnasio_backend.security.AuthenticatedUser;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    @Autowired private ClienteRepository clientes;

    // GET: http://localhost:8080/api/reservas
    @GetMapping
    public ResponseEntity<List<ReservaDTO.Response>> listarTodas() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    // GET: http://localhost:8080/api/reservas/cliente/1
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ReservaDTO.Response>> listarPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(reservaService.listarPorCliente(clienteId));
    }

    // POST: http://localhost:8080/api/reservas
    @PostMapping
    public ResponseEntity<ReservaDTO.Response> crear(@RequestBody ReservaDTO.Request request, @AuthenticationPrincipal AuthenticatedUser user) {
        if ("CLIENTE".equalsIgnoreCase(user.rol())) request.setClienteId(clientes.findByUsuarioUsuarioId(user.usuarioId()).orElseThrow().getClienteId());
        return ResponseEntity.ok(reservaService.crear(request));
    }

    // PUT: http://localhost:8080/api/reservas/1/estado
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReservaDTO.Response> cambiarEstado(@PathVariable Integer id, @RequestBody ReservaDTO.EstadoRequest request) {
        return ResponseEntity.ok(reservaService.cambiarEstado(id, request));
    }
}
