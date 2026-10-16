package com.gimnasio.gimnasio_backend.modulo4.entity;

import com.gimnasio.gimnasio_backend.modulo2.entity.Entrenador;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "HorarioClase", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class HorarioClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HorarioID")
    private Integer horarioId;

    // FK_HorarioClase_Clase
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClaseID", nullable = false)
    private Clase clase;

    // FK_HorarioClase_Entrenador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EntrenadorID", nullable = false)
    private Entrenador entrenador;

    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "HoraInicio", nullable = false)
    private LocalTime horaInicio;

    // CK_HorarioClase_Horas: HoraFin > HoraInicio
    @Column(name = "HoraFin", nullable = false)
    private LocalTime horaFin;
}
