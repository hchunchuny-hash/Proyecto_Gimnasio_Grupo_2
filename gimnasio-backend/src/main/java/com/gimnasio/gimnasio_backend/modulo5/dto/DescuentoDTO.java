package com.gimnasio.gimnasio_backend.modulo5.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DescuentoDTO {

    @Data
    public static class Request {
        private String nombre;
        private BigDecimal porcentaje;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private Boolean estado;
    }

    @Data
    public static class Response {
        private Integer descuentoId;
        private String nombre;
        private BigDecimal porcentaje;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private Boolean estado;
    }
}
