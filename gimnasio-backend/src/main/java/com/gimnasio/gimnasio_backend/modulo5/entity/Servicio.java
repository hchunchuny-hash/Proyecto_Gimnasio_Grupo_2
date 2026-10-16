package com.gimnasio.gimnasio_backend.modulo5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Servicio", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ServicioID")
    private Integer servicioId;

    @Column(name = "Nombre", nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "Descripcion", length = 250)
    private String descripcion;

    @Column(name = "Precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}
