package com.gimnasio.gimnasio_backend.modulo1.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Equipamiento", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Equipamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EquipamientoID")
    private Integer equipamientoId;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "NumeroSerie", length = 50)
    private String numeroSerie;

    @Column(name = "FechaAdquisicion")
    private LocalDate fechaAdquisicion;

    @Column(name = "Estado", nullable = false, length = 30)
    private String estado; // Ej: 'OPERATIVO', 'MANTENIMIENTO', 'FUERA_DE_SERVICIO'

    // FK_Equipamiento_AreaGimnasio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AreaID", nullable = false)
    private AreaGimnasio areaGimnasio;
}