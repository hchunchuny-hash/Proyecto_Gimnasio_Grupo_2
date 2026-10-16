package com.gimnasio.gimnasio_backend.modulo5.dto;

import lombok.Data;
import java.time.LocalDateTime;

public class NotificacionDTO {

    @Data
    public static class Request {
        private Integer clienteId;
        private String titulo;
        private String mensaje;
    }

    @Data
    public static class Response {
        private Integer notificacionId;
        private Integer clienteId;
        private String nombreCliente;
        private String titulo;
        private String mensaje;
        private LocalDateTime fechaEnvio;
        private Boolean leida;
    }
}
