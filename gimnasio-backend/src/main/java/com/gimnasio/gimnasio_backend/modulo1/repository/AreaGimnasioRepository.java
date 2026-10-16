package com.gimnasio.gimnasio_backend.modulo1.repository;

import com.gimnasio.gimnasio_backend.modulo1.entity.AreaGimnasio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaGimnasioRepository extends JpaRepository<AreaGimnasio, Integer> {
    List<AreaGimnasio> findByEstadoTrue();
}