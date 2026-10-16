package com.gimnasio.gimnasio_backend.modulo5.controller;

import com.gimnasio.gimnasio_backend.modulo5.dto.PagoDTO;
import com.gimnasio.gimnasio_backend.modulo5.repository.IngresoMensualProjection;
import com.gimnasio.gimnasio_backend.modulo5.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    // GET: http://localhost:8080/api/pagos
    @GetMapping
    public ResponseEntity<List<PagoDTO.Response>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    // GET: http://localhost:8080/api/pagos/cliente/1
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PagoDTO.Response>> listarPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(pagoService.listarPorCliente(clienteId));
    }

    // GET: http://localhost:8080/api/pagos/ingresos-mensuales (vista vw_IngresosMensuales)
    @GetMapping("/ingresos-mensuales")
    public ResponseEntity<List<IngresoMensualProjection>> ingresosMensuales() {
        return ResponseEntity.ok(pagoService.obtenerIngresosMensuales());
    }

    // POST: http://localhost:8080/api/pagos/procesar
    @PostMapping("/procesar")
    public ResponseEntity<PagoDTO.Response> procesar(@RequestBody PagoDTO.ProcesarRequest request) {
        return ResponseEntity.ok(pagoService.procesar(request));
    }

    // PUT: http://localhost:8080/api/pagos/1/anular
    @PutMapping("/{id}/anular")
    public ResponseEntity<PagoDTO.Response> anular(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.anular(id));
    }
}
