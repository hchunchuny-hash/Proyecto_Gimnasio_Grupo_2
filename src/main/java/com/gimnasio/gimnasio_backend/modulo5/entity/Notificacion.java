package com.gimnasio.gimnasio_backend.modulo5.entity;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notificacion", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NotificacionID")
    private Integer notificacionId;

    // FK_Notificacion_Cliente (va al Cliente, NO al Usuario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    @Column(name = "Titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "Mensaje", nullable = false, length = 500)
    private String mensaje;

    @Column(name = "FechaEnvio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "Leida", nullable = false)
    private Boolean leida;

    @PrePersist
    void valoresPorDefecto() {
        if (fechaEnvio == null) fechaEnvio = LocalDateTime.now();
        if (leida == null) leida = false;
    }
}
