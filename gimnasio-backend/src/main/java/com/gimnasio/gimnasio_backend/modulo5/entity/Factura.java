package com.gimnasio.gimnasio_backend.modulo5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Factura", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FacturaID")
    private Integer facturaId;

    // FK_Factura_Pago (un pago tiene una sola factura)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PagoID", nullable = false, unique = true)
    private Pago pago;

    @Column(name = "NumeroFactura", nullable = false, length = 50, unique = true)
    private String numeroFactura;

    @Column(name = "FechaEmision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "Total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @PrePersist
    void valoresPorDefecto() {
        if (fechaEmision == null) fechaEmision = LocalDateTime.now();
    }
}
