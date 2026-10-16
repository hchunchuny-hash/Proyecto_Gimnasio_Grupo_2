package com.gimnasio.gimnasio_backend.modulo4.dto;

import lombok.Data;
import java.time.LocalDateTime;

public class ClaseDTO {

    @Data
    public static class Request {
        private Integer servicioId;
        private String nombre;
        private String descripcion;
        private Integer cupoMaximo;
        private Boolean estado;
    }

    @Data
    public static class Response {
        private Integer claseId;
        private Integer servicioId;
        private String nombreServicio;
        private String nombre;
        private String descripcion;
        private Integer cupoMaximo;
        private Integer inscritos;
        private Boolean estado;
    }

    @Data
    public static class InscribirRequest {
        private Integer claseId;
        private Integer clienteId;
    }

    @Data
    public static class InscripcionResponse {
        private Integer inscripcionId;
        private Integer claseId;
        private String nombreClase;
        private Integer clienteId;
        private String nombreCliente;
        private LocalDateTime fechaInscripcion;
        private String estado;
    }
}
