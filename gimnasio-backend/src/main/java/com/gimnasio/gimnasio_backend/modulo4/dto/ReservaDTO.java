package com.gimnasio.gimnasio_backend.modulo4.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaDTO {

    @Data
    public static class Request {
        private Integer clienteId;
        private Integer servicioId;
        private LocalDate fecha;
        private LocalTime horaInicio;
        private LocalTime horaFin;      // opcional
        private String observaciones;   // opcional
    }

    @Data
    public static class EstadoRequest {
        private String estado;          // PENDIENTE, CONFIRMADA, FINALIZADA o CANCELADA
    }

    @Data
    public static class Response {
        private Integer reservaId;
        private Integer clienteId;
        private String nombreCliente;
        private Integer servicioId;
        private String nombreServicio;
        private LocalDate fecha;
        private LocalTime horaInicio;
        private LocalTime horaFin;
        private String estado;
        private String observaciones;
    }
}
