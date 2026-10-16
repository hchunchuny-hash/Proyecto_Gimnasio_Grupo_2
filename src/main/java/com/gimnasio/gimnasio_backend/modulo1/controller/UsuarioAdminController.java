package com.gimnasio.gimnasio_backend.modulo1.controller;

import com.gimnasio.gimnasio_backend.modulo1.dto.UsuarioAdminDTO.*;
import com.gimnasio.gimnasio_backend.modulo1.service.UsuarioAdminService;
import com.gimnasio.gimnasio_backend.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioAdminController {
    private final UsuarioAdminService service;
    public UsuarioAdminController(UsuarioAdminService service) { this.service = service; }

    @GetMapping public List<UsuarioView> listar(@RequestParam(required = false) String q) { return service.listar(q); }
    @PutMapping("/{id}/rol") public UsuarioView cambiarRol(@PathVariable Integer id, @Valid @RequestBody CambioRol cambio,
            @AuthenticationPrincipal AuthenticatedUser actor) { return service.cambiarRol(actor.usuarioId(), id, cambio.rol()); }
    @PutMapping("/{id}/estado") public EstadoView cambiarEstado(@PathVariable Integer id, @Valid @RequestBody CambioEstado cambio,
            @AuthenticationPrincipal AuthenticatedUser actor) { return service.cambiarEstado(actor.usuarioId(), id, cambio.estado()); }
    @GetMapping("/auditoria-roles") public List<AuditoriaView> auditoria() { return service.auditoriaRoles(); }
}
