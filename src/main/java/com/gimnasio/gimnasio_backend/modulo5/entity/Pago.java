package com.gimnasio.gimnasio_backend.modulo5.entity;

import com.gimnasio.gimnasio_backend.modulo2.entity.Membresia;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pago", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PagoID")
    private Integer pagoId;

    // FK_Pago_Membresia (el pago NO tiene ClienteID: se llega al cliente por la membresia)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MembresiaID", nullable = false)
    private Membresia membresia;

    // FK_Pago_Descuento (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DescuentoID")
    private Descuento descuento;

    @Column(name = "FechaPago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "MontoOriginal", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoOriginal;

    @Column(name = "MontoDescuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoDescuento;

    @Column(name = "MontoPagado", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPagado;

    // CK_Pago_Metodo: 'EFECTIVO', 'TARJETA', 'TRANSFERENCIA'
    @Column(name = "MetodoPago", nullable = false, length = 20)
    private String metodoPago;

    // CK_Pago_Estado: 'PAGADO', 'ANULADO'
    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @PrePersist
    void valoresPorDefecto() {
        if (fechaPago == null) fechaPago = LocalDateTime.now();
        if (montoDescuento == null) montoDescuento = BigDecimal.ZERO;
        if (estado == null) estado = "PAGADO";
    }
}
