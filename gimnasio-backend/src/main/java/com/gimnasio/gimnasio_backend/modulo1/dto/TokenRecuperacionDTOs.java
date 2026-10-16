package com.gimnasio.gimnasio_backend.modulo1.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

public class TokenRecuperacionDTOs {

    @Data
    public static class SolicitarRecuperacionRequest {
        @NotBlank(message = "El nombre de usuario es obligatorio.")
        private String nombreUsuario;

        @NotBlank(message = "El correo electrónico es obligatorio.")
        @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@(?i)(gmail\\.com|outlook\\.com|miumg\\.edu\\.gt)$",
            message = "El correo debe ser de un dominio permitido (@gmail.com, @outlook.com o @miumg.edu.gt)."
        )
        private String correo;
    }

    @Data
    public static class ResetPasswordRequest {
        @NotBlank(message = "El token es obligatorio.")
        private String token;

        @NotBlank(message = "La nueva contraseña es obligatoria.")
        @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&_#^()+=\\-\\[\\]{}|;:',.<>/~\\\\]).{8,16}$",
            message = "La nueva contraseña debe tener entre 8 y 16 caracteres e incluir letras, números y al menos un carácter especial."
        )
        private String nuevaContrasena;
    }
}
