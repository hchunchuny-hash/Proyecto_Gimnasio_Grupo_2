package com.gimnasio.gimnasio_backend.modulo4.entity;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Asistencia", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AsistenciaID")
    private Integer asistenciaId;

    // FK_Asistencia_Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "HoraEntrada", nullable = false)
    private LocalTime horaEntrada;

    // CK_Asistencia_Horas: nulo o >= HoraEntrada
    @Column(name = "HoraSalida")
    private LocalTime horaSalida;

    // CK_Asistencia_Estado: 'VALIDADA', 'RECHAZADA'
    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @PrePersist
    void valoresPorDefecto() {
        if (estado == null) estado = "VALIDADA";
    }
}
