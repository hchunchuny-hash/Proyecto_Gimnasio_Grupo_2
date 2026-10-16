package com.gimnasio.gimnasio_backend.modulo5.controller;

import com.gimnasio.gimnasio_backend.modulo5.dto.FacturaDTO;
import com.gimnasio.gimnasio_backend.modulo5.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    // GET: http://localhost:8080/api/facturas
    @GetMapping
    public ResponseEntity<List<FacturaDTO.Response>> listarTodas() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<FacturaDTO.Response>> listarPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(facturaService.listarPorCliente(clienteId));
    }

    // GET: http://localhost:8080/api/facturas/1
    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO.Response> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(facturaService.obtenerPorId(id));
    }

    // GET: http://localhost:8080/api/facturas/pago/1
    @GetMapping("/pago/{pagoId}")
    public ResponseEntity<FacturaDTO.Response> obtenerPorPago(@PathVariable Integer pagoId) {
        return ResponseEntity.ok(facturaService.obtenerPorPago(pagoId));
    }
}
