package com.gimnasio.gimnasio_backend.modulo5.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoDTO {

    @Data
    public static class ProcesarRequest {
        private Integer membresiaId;
        private Integer descuentoId;   // opcional
        private String metodoPago;     // EFECTIVO, TARJETA o TRANSFERENCIA
    }

    @Data
    public static class Response {
        private Integer pagoId;
        private Integer membresiaId;
        private Integer clienteId;
        private String nombreCliente;
        private Integer descuentoId;
        private String nombreDescuento;
        private LocalDateTime fechaPago;
        private BigDecimal montoOriginal;
        private BigDecimal montoDescuento;
        private BigDecimal montoPagado;
        private String metodoPago;
        private String estado;
        private Integer facturaId;
        private String numeroFactura;
    }
}
