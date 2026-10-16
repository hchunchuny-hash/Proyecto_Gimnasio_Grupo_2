package com.gimnasio.gimnasio_backend.modulo2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "AsignacionEntrenador", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AsignacionEntrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AsignacionID")
    private Integer asignacionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EntrenadorID", nullable = false)
    private Entrenador entrenador;

    @Column(name = "FechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "FechaFin")
    private LocalDate fechaFin;

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}