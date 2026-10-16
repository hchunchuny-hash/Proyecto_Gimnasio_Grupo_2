package com.gimnasio.gimnasio_backend.modulo5.repository;

import com.gimnasio.gimnasio_backend.modulo5.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    boolean existsByNombre(String nombre);
}
