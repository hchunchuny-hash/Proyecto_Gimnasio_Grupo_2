package com.gimnasio.gimnasio_backend.modulo5.controller;

import com.gimnasio.gimnasio_backend.modulo5.dto.NotificacionDTO;
import com.gimnasio.gimnasio_backend.modulo5.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    // GET: http://localhost:8080/api/notificaciones (todas)
    // GET: http://localhost:8080/api/notificaciones?clienteId=1 (de un cliente)
    @GetMapping
    public ResponseEntity<List<NotificacionDTO.Response>> listar(@RequestParam(required = false) Integer clienteId) {
        return ResponseEntity.ok(notificacionService.listar(clienteId));
    }

    // POST: http://localhost:8080/api/notificaciones
    @PostMapping
    public ResponseEntity<NotificacionDTO.Response> crear(@RequestBody NotificacionDTO.Request request) {
        return ResponseEntity.ok(notificacionService.crear(request));
    }

    // PUT: http://localhost:8080/api/notificaciones/1/leida
    @PutMapping("/{id}/leida")
    public ResponseEntity<NotificacionDTO.Response> marcarLeida(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.marcarLeida(id));
    }
}
