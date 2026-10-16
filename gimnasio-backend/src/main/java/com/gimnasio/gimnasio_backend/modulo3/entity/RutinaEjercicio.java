package com.gimnasio.gimnasio_backend.modulo3.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RutinaEjercicio", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RutinaEjercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RutinaEjercicioID")
    private Integer rutinaEjercicioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DiaRutinaID", nullable = false)
    private DiaRutina diaRutina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EjercicioID", nullable = false)
    private Ejercicio ejercicio;

    @Column(name = "Series", nullable = false)
    private Integer series;

    @Column(name = "Repeticiones", nullable = false)
    private Integer repeticiones;

    @Column(name = "DescansoSegundos", nullable = false)
    @Builder.Default
    private Integer descansoSegundos = 60;

    @Column(name = "Observaciones", length = 250)
    private String observaciones;
}