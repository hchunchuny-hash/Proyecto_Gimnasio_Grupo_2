package com.gimnasio.gimnasio_backend.modulo2.entity;

import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Entrenador", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EntrenadorID")
    private Integer entrenadorId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioID", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "Nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "Apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "Especialidad", length = 100)
    private String especialidad;

    @Column(name = "Telefono", length = 8)
    private String telefono;

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}