package com.gimnasio.gimnasio_backend.modulo2.controller;

import com.gimnasio.gimnasio_backend.modulo2.dto.MembresiaDTO;
import com.gimnasio.gimnasio_backend.modulo2.service.MembresiaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membresias")
public class MembresiaController {

    @Autowired
    private MembresiaService membresiaService;

    @GetMapping
    public ResponseEntity<List<MembresiaDTO.Response>> listarTodas() {
        return ResponseEntity.ok(membresiaService.listarTodas());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<MembresiaDTO.Response>> listarPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(membresiaService.listarPorCliente(clienteId));
    }

    @PostMapping("/asignar")
    public ResponseEntity<MembresiaDTO.Response> asignarMembresia(@Valid @RequestBody MembresiaDTO.AsignarRequest request) {
        return ResponseEntity.ok(membresiaService.asignarMembresia(request));
    }

    @PostMapping("/actualizar-vencidas")
    public ResponseEntity<String> actualizarVencidas() {
        membresiaService.ejecutarMantenimientoVencimientos();
        return ResponseEntity.ok("Proceso de actualización de membresías vencidas ejecutado correctamente.");
    }
}