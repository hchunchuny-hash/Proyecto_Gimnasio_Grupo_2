package com.gimnasio.gimnasio_backend.modulo1.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

public class EquipamientoDTO {

    @Data
    public static class Request {
        @NotBlank(message = "El nombre del equipamiento es obligatorio.")
        @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
        private String nombre;

        @Size(max = 50, message = "El número de serie no puede exceder los 50 caracteres.")
        private String numeroSerie;

        @PastOrPresent(message = "La fecha de adquisición no puede ser futura.")
        private LocalDate fechaAdquisicion;

        @NotBlank(message = "El estado del equipamiento es obligatorio.")
        @Size(max = 30, message = "El estado no puede exceder los 30 caracteres.")
        private String estado; // Ej: 'OPERATIVO', 'MANTENIMIENTO', 'FUERA_DE_SERVICIO'

        @NotNull(message = "El ID del área de gimnasio asignada es obligatorio.")
        private Integer areaGimnasioId;
    }

    @Data
    public static class Response {
        private Integer equipamientoId;
        private String nombre;
        private String numeroSerie;
        private LocalDate fechaAdquisicion;
        private String estado;
        private Integer areaGimnasioId;
        private String nombreArea;
    }
}