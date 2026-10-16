package com.gimnasio.gimnasio_backend.modulo3.entity;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import com.gimnasio.gimnasio_backend.modulo2.entity.Entrenador;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Rutina", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RutinaID")
    private Integer rutinaId;

    // FK_Rutina_Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    // FK_Rutina_Entrenador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EntrenadorID", nullable = false)
    private Entrenador entrenador;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Objetivo", length = 250)
    private String objetivo;

    @Column(name = "FechaInicio", nullable = false) // Corregido: En la BD es FechaInicio
    private LocalDate fechaInicio;

    @Column(name = "FechaFin") // Agregado: Columna de la BD
    private LocalDate fechaFin;

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DiaRutina> dias = new ArrayList<>();
}