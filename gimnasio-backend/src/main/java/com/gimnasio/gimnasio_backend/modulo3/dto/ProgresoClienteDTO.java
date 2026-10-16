package com.gimnasio.gimnasio_backend.modulo3.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProgresoClienteDTO {

    @Data
    public static class Request {
        private Integer clienteId;
        private LocalDate fecha;
        private BigDecimal peso;
        private String observaciones;
    }

    @Data
    public static class Response {
        private Integer progresoId;
        private Integer clienteId;
        private LocalDate fecha;
        private BigDecimal peso;
        private String observaciones;
    }
}