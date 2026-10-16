package com.gimnasio.gimnasio_backend.modulo1.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Usuario", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UsuarioID")
    private Integer usuarioId;

    @Column(name = "NombreUsuario", nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @Column(name = "Correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "Contrasena", nullable = false)
    private String contrasena;

    @Column(name = "Rol", nullable = false, length = 20)
    private String rol; // Ej: 'ADMIN', 'RECEPCION', 'ENTRENADOR', 'CLIENTE'

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;

    @Column(name = "FechaRegistro", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}