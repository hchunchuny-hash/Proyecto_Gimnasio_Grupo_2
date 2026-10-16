package com.gimnasio.gimnasio_backend.modulo3.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Ejercicio", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EjercicioID")
    private Integer ejercicioId;

    @Column(name = "GrupoMuscularID", nullable = false)
    private Integer grupoMuscularId;

    @Column(name = "NivelDificultadID", nullable = false)
    private Integer nivelDificultadId;

    @Column(name = "EquipamientoID")
    private Integer equipamientoId;

    @Column(name = "Nombre", nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "Descripcion", length = 250)
    private String descripcion;

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}