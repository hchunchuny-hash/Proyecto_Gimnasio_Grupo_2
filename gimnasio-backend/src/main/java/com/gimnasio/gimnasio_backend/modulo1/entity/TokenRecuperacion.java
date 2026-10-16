package com.gimnasio.gimnasio_backend.modulo1.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TokenRecuperacion", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TokenRecuperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    // Relación Foreign Key con Usuario (FK_TokenRecuperacion_Usuario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioID", nullable = false)
    private Usuario usuario;

    @Column(name = "Token", nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "FechaExpiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "Usado", nullable = false)
    @Builder.Default
    private Boolean usado = false;
}