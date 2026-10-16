package com.gimnasio.gimnasio_backend.modulo2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Membresia", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Membresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MembresiaID")
    private Integer membresiaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TipoMembresiaID", nullable = false)
    private TipoMembresia tipoMembresia;

    @Column(name = "FechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "FechaVencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "Precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado; // 'ACTIVA', 'VENCIDA', 'SUSPENDIDA'
}