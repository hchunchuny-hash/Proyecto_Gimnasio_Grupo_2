package com.gimnasio.gimnasio_backend.modulo3.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

public class RutinaDTO {

    @Data
    public static class EjercicioRequest {
        private Integer ejercicioId;
        private Integer series;
        private Integer repeticiones;
        private Integer descansoSegundos;
        private String observaciones;
    }

    @Data
    public static class DiaRequest {
        private String diaSemana;
        private Integer ordenDia;
        private List<EjercicioRequest> ejercicios;
    }

    @Data
    public static class Request {
        private Integer clienteId;
        private Integer entrenadorId;
        private String nombre;
        private String objetivo;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private List<DiaRequest> dias;
    }

    @Data
    public static class EjercicioResponse {
        private Integer rutinaEjercicioId;
        private Integer ejercicioId;
        private String nombreEjercicio;
        private Integer series;
        private Integer repeticiones;
        private Integer descansoSegundos;
        private String observaciones;
    }

    @Data
    public static class DiaResponse {
        private Integer diaRutinaId;
        private String diaSemana;
        private Integer ordenDia;
        private List<EjercicioResponse> ejercicios;
    }

    @Data
    public static class Response {
        private Integer rutinaId;
        private Integer clienteId;
        private String nombreCliente;
        private Integer entrenadorId;
        private String nombreEntrenador;
        private String nombre;
        private String objetivo;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private Boolean estado;
        private List<DiaResponse> dias;
    }
}