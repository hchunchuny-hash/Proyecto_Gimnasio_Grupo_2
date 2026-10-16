package com.gimnasio.gimnasio_backend.modulo1.controller;

import com.gimnasio.gimnasio_backend.modulo1.dto.TokenRecuperacionDTOs.*;
import com.gimnasio.gimnasio_backend.modulo1.service.TokenRecuperacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth/recuperacion")
public class TokenRecuperacionController {
    private static final int MAX_SOLICITUDES_POR_HORA = 5;
    private final Map<String, ArrayDeque<Instant>> solicitudes = new ConcurrentHashMap<>();

    @Autowired
    private TokenRecuperacionService tokenService;

    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitarRecuperacion(@Valid @RequestBody SolicitarRecuperacionRequest request,
                                                         jakarta.servlet.http.HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        ArrayDeque<Instant> recientes = solicitudes.computeIfAbsent(ip, key -> new ArrayDeque<>());
        synchronized (recientes) {
            Instant limite = Instant.now().minusSeconds(3600);
            while (!recientes.isEmpty() && recientes.peekFirst().isBefore(limite)) recientes.removeFirst();
            if (recientes.size() >= MAX_SOLICITUDES_POR_HORA) return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Si los datos corresponden a una cuenta activa, recibirá instrucciones por correo.");
            recientes.addLast(Instant.now());
        }
        tokenService.solicitarRecuperacion(request.getNombreUsuario(), request.getCorreo());
        return ResponseEntity.ok("Si los datos corresponden a una cuenta activa, recibirá instrucciones por correo.");
    }

    @PostMapping("/resetear")
    public ResponseEntity<String> resetearContrasena(@Valid @RequestBody ResetPasswordRequest request) {
        tokenService.cambiarContrasena(request);
        return ResponseEntity.ok("Contraseña restablecida exitosamente.");
    }
}
