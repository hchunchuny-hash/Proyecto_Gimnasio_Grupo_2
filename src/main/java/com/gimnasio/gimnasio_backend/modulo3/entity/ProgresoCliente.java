package com.gimnasio.gimnasio_backend.modulo3.entity;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ProgresoCliente", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProgresoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProgresoID")
    private Integer progresoId;

    // FK_Progreso_Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    @Column(name = "Fecha", nullable = false) // Corregido: En la BD es Fecha
    private LocalDate fecha;

    @Column(name = "Peso", precision = 6, scale = 2) // Corregido: En la BD es Peso
    private BigDecimal peso;

    @Column(name = "Observaciones", length = 500) // Corregido: En la BD es VARCHAR(500)
    private String observaciones;
}