package com.gimnasio.gimnasio_backend.modulo5.repository;

import java.math.BigDecimal;

// Mapea las columnas de la vista dbo.vw_IngresosMensuales
public interface IngresoMensualProjection {
    Integer getAnio();
    Integer getMes();
    String getTipoMembresia();
    Integer getCantidadPagos();
    BigDecimal getIngresos();
}
