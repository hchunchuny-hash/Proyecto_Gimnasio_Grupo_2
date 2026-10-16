package com.gimnasio.gimnasio_backend.modulo5.dto;

import lombok.Data;
import java.math.BigDecimal;

public class ServicioDTO {

    @Data
    public static class Request {
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private Boolean estado;
    }

    @Data
    public static class Response {
        private Integer servicioId;
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private Boolean estado;
    }
}
