package com.gimnasio.gimnasio_backend.modulo2.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MembresiaDTO {

    @Data
    public static class AsignarRequest {
        @NotNull(message = "El ID de cliente es obligatorio.")
        private Integer clienteId;

        @NotNull(message = "El ID del tipo de membresía es obligatorio.")
        private Integer tipoMembresiaId;

        private LocalDate fechaInicio; // Si es nulo, se usará la fecha actual
    }

    @Data
    public static class Response {
        private Integer membresiaId;
        private Integer clienteId;
        private String nombreCliente;
        private Integer tipoMembresiaId;
        private String nombreTipoMembresia;
        private LocalDate fechaInicio;
        private LocalDate fechaVencimiento;
        private BigDecimal precio;
        private String estado;
    }
}