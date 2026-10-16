package com.gimnasio.gimnasio_backend.modulo3.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DiaRutina", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DiaRutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DiaRutinaID")
    private Integer diaRutinaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RutinaID", nullable = false)
    private Rutina rutina;

    @Column(name = "DiaSemana", nullable = false, length = 15)
    private String diaSemana;

    @Column(name = "OrdenDia", nullable = false)
    private Integer ordenDia;

    @OneToMany(mappedBy = "diaRutina", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RutinaEjercicio> ejercicios = new ArrayList<>();
}