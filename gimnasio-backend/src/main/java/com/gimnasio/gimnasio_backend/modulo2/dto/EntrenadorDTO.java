package com.gimnasio.gimnasio_backend.modulo2.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

public class EntrenadorDTO {

    @Data
    public static class Request {
@NotNull(message = "El ID de usuario es obligatorio.")
        private Integer usuarioId;

        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
        private String nombres;

        @NotBlank(message = "El apellido es obligatorio.")
        @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres.")
        private String apellidos;

        @Size(max = 100, message = "La especialidad no puede exceder los 100 caracteres.")
        private String especialidad;

        @Pattern(regexp = "^$|^[0-9]{7,15}$", message = "El teléfono solo debe contener números")
        @Size(max = 8, message = "El teléfono no puede exceder los 8 caracteres.")
        private String telefono;
    }

    @Data
    public static class Response {
        private Integer entrenadorId;
        private Integer usuarioId;
        private String nombres;
        private String apellidos;
        private String especialidad;
        private String telefono;
        private Boolean estado;
    }
}