package com.gimnasio.gimnasio_backend.modulo1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioAdminDTO {
    public record UsuarioView(Integer usuarioId, String nombreUsuario, String correo, String rol, Boolean estado) { }
    public record AuditoriaView(Integer auditoriaRolId, Integer administradorId, Integer usuarioAfectadoId,
                                String rolAnterior, String rolNuevo, java.time.LocalDateTime fecha, String resultado) { }
    public record EstadoView(Integer usuarioId, String nombreUsuario, String rol, Boolean estado) { }
    public record CambioEstado(@NotNull Boolean estado) { }
    public record CambioRol(@NotBlank @Size(max = 20) String rol) { }
}
