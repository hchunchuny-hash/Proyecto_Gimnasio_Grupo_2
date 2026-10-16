package com.gimnasio.gimnasio_backend.modulo1.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

public class AuthDTOs {

    @Data
    public static class LoginRequest {
        @NotBlank(message = "El nombre de usuario es obligatorio.")
        private String nombreUsuario;

        @NotBlank(message = "La contraseña es obligatoria.")
        private String contrasena;
    }

    @Data
    public static class RegisterRequest {
        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
        private String nombreUsuario;

        @NotBlank(message = "El correo es obligatorio.")
        @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@(?i)(gmail\\.com|outlook\\.com|miumg\\.edu\\.gt)$",
            message = "El correo debe ser de un dominio permitido (@gmail.com, @outlook.com o @miumg.edu.gt)."
        )
        @Size(max = 100, message = "El correo no puede exceder los 100 caracteres.")
        private String correo;

        @NotBlank(message = "La contraseña es obligatoria.")
        @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&_#^()+=\\-\\[\\]{}|;:',.<>/~\\\\]).{8,16}$",
            message = "La contraseña debe tener entre 8 y 16 caracteres e incluir letras, números y al menos un carácter especial."
        )
        private String contrasena;

        private String rol; // Opcional, por defecto 'CLIENTE'

        // Campos para la creación automática de perfil (Cliente / Entrenador)
        private String nombres;
        private String apellidos;
        
        @Pattern(regexp = "^$|^[0-9]{7,8}$", message = "El teléfono solo debe contener números (7 u 8 dígitos).")
        private String telefono;

        @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
        private LocalDate fechaNacimiento;

        @Size(max = 100, message = "La especialidad no puede exceder los 100 caracteres.")
        private String especialidad;
    }

    @Data
    public static class AuthResponse {
        private Integer usuarioId;
        private String nombreUsuario;
        private String correo;
        private String rol;
        private String token;
    }
}