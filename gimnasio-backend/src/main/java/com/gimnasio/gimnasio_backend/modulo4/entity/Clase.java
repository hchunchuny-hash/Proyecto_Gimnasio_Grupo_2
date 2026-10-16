package com.gimnasio.gimnasio_backend.modulo4.entity;

import com.gimnasio.gimnasio_backend.modulo5.entity.Servicio;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Clase", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClaseID")
    private Integer claseId;

    // FK_Clase_Servicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ServicioID", nullable = false)
    private Servicio servicio;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Descripcion", length = 250)
    private String descripcion;

    // CK_Clase_Cupo: mayor que 0
    @Column(name = "CupoMaximo", nullable = false)
    private Integer cupoMaximo;

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}
