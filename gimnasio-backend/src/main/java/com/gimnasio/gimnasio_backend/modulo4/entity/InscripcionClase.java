package com.gimnasio.gimnasio_backend.modulo4.entity;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "InscripcionClase", schema = "dbo",
       uniqueConstraints = @UniqueConstraint(name = "UQ_Inscripcion_Cliente_Clase",
                                             columnNames = {"ClaseID", "ClienteID"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class InscripcionClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InscripcionID")
    private Integer inscripcionId;

    // FK_Inscripcion_Clase (se inscribe a la CLASE, no a un horario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClaseID", nullable = false)
    private Clase clase;

    // FK_Inscripcion_Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    @Column(name = "FechaInscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;

    // CK_Inscripcion_Estado: 'ACTIVA', 'FINALIZADA', 'CANCELADA'
    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @PrePersist
    void valoresPorDefecto() {
        if (fechaInscripcion == null) fechaInscripcion = LocalDateTime.now();
        if (estado == null) estado = "ACTIVA";
    }
}
