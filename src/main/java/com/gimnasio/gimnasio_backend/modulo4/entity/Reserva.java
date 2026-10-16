package com.gimnasio.gimnasio_backend.modulo4.entity;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import com.gimnasio.gimnasio_backend.modulo5.entity.Servicio;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Reserva", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReservaID")
    private Integer reservaId;

    // FK_Reserva_Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    // FK_Reserva_Servicio (la reserva es de un SERVICIO, no de una clase)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ServicioID", nullable = false)
    private Servicio servicio;

    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "HoraInicio", nullable = false)
    private LocalTime horaInicio;

    // CK_Reserva_Horas: nulo o mayor que HoraInicio
    @Column(name = "HoraFin")
    private LocalTime horaFin;

    // CK_Reserva_Estado: 'PENDIENTE', 'CONFIRMADA', 'FINALIZADA', 'CANCELADA'
    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "Observaciones", length = 500)
    private String observaciones;

    @PrePersist
    void valoresPorDefecto() {
        if (estado == null) estado = "PENDIENTE";
    }
}
