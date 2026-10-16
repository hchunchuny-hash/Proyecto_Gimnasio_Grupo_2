package com.gimnasio.gimnasio_backend.modulo2.repository;

import java.time.LocalDate;

public interface ClienteRiesgoProjection {
    Integer getClienteID();
    String getNombres();
    String getApellidos();
    String getTelefono();
    LocalDate getUltimaAsistencia();
    Integer getDiasSinAsistir();
}