package com.gimnasio.gimnasio_backend.modulo4.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

public class AsistenciaDTO {

    @Data
    public static class MarcarRequest {
        private Integer clienteId;
    }

    @Data
    public static class Response {
        private Integer asistenciaId;
        private Integer clienteId;
        private String nombreCliente;
        private LocalDate fecha;
        private LocalTime horaEntrada;
        private LocalTime horaSalida;
        private String estado;
        private String mensaje;
    }
}
