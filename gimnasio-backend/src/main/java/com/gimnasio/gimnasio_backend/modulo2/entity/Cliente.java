package com.gimnasio.gimnasio_backend.modulo2.entity;

import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Cliente", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClienteID")
    private Integer clienteId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioID", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "Nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "Apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "Telefono", length = 8)
    private String telefono;

    @Column(name = "FechaNacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;

    @Column(name = "FechaRegistro", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}