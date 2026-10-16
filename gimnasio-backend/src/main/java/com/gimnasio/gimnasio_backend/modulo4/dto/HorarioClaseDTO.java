package com.gimnasio.gimnasio_backend.modulo4.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

public class HorarioClaseDTO {

    @Data
    public static class Request {
        private Integer claseId;
        private Integer entrenadorId;
        private LocalDate fecha;
        private LocalTime horaInicio;
        private LocalTime horaFin;
    }

    @Data
    public static class Response {
        private Integer horarioId;
        private Integer claseId;
        private String nombreClase;
        private Integer entrenadorId;
        private String nombreEntrenador;
        private LocalDate fecha;
        private LocalTime horaInicio;
        private LocalTime horaFin;
    }
}
