package com.gimnasio.gimnasio_backend.modulo2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "TipoMembresia", schema = "dbo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TipoMembresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TipoMembresiaID")
    private Integer tipoMembresiaId;

    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "DuracionMeses", nullable = false)
    private Integer duracionMeses;

    @Column(name = "Precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "Descripcion", length = 250)
    private String descripcion;

    @Column(name = "Estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}