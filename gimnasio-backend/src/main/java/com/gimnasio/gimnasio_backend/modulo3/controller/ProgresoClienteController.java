package com.gimnasio.gimnasio_backend.modulo3.controller;

import com.gimnasio.gimnasio_backend.modulo3.dto.ProgresoClienteDTO;
import com.gimnasio.gimnasio_backend.modulo3.service.ProgresoClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.gimnasio.gimnasio_backend.security.AuthenticatedUser;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;

import java.util.List;

@RestController
@RequestMapping("/api/progresos")
public class ProgresoClienteController {

    @Autowired
    private ProgresoClienteService progresoClienteService;
    @Autowired private ClienteRepository clientes;

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ProgresoClienteDTO.Response>> listarPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(progresoClienteService.listarPorCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<ProgresoClienteDTO.Response> registrar(@RequestBody ProgresoClienteDTO.Request request, @AuthenticationPrincipal AuthenticatedUser user) {
        if ("CLIENTE".equalsIgnoreCase(user.rol())) request.setClienteId(clientes.findByUsuarioUsuarioId(user.usuarioId()).orElseThrow().getClienteId());
        return ResponseEntity.ok(progresoClienteService.registrar(request));
    }
}
