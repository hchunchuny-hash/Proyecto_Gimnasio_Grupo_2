package com.gimnasio.gimnasio_backend.modulo1.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AreaGimnasio", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AreaGimnasio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AreaID")
    private Integer areaGimnasioId;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Descripcion", length = 255)
    private String descripcion;

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}