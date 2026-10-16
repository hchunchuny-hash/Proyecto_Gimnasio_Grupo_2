package com.gimnasio.gimnasio_backend.modulo5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Descuento", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DescuentoID")
    private Integer descuentoId;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    // CK_Descuento_Porcentaje: entre 0 y 100
    @Column(name = "Porcentaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentaje;

    @Column(name = "FechaInicio", nullable = false)
    private LocalDate fechaInicio;

    // CK_Descuento_Fechas: nulo o >= FechaInicio
    @Column(name = "FechaFin")
    private LocalDate fechaFin;

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}
