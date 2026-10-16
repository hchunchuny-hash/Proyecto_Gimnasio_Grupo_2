package com.gimnasio.gimnasio_backend.modulo5.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FacturaDTO {

    @Data
    public static class Response {
        private Integer facturaId;
        private String numeroFactura;
        private LocalDateTime fechaEmision;
        private BigDecimal total;
        private Integer pagoId;
        private Integer membresiaId;
        private String tipoMembresia;
        private Integer clienteId;
        private String nombreCliente;
        private BigDecimal montoOriginal;
        private BigDecimal montoDescuento;
        private BigDecimal montoPagado;
        private String metodoPago;
        private String estadoPago;
    }
}
