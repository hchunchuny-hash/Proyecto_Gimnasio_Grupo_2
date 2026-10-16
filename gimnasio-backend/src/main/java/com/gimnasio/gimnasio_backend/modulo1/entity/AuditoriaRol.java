package com.gimnasio.gimnasio_backend.modulo1.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AuditoriaRol", schema = "dbo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditoriaRol {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AuditoriaRolID")
    private Integer auditoriaRolId;
    @Column(name = "AdministradorID", nullable = false)
    private Integer administradorId;
    @Column(name = "UsuarioAfectadoID", nullable = false)
    private Integer usuarioAfectadoId;
    @Column(name = "RolAnterior", nullable = false, length = 20)
    private String rolAnterior;
    @Column(name = "RolNuevo", nullable = false, length = 20)
    private String rolNuevo;
    @Column(name = "Fecha", nullable = false)
    private LocalDateTime fecha;
    @Column(name = "Resultado", nullable = false, length = 30)
    private String resultado;
    @PrePersist void alCrear() { if (fecha == null) fecha = LocalDateTime.now(); }
}
