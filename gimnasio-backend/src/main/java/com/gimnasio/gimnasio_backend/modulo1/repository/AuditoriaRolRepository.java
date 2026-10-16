package com.gimnasio.gimnasio_backend.modulo1.repository;

import com.gimnasio.gimnasio_backend.modulo1.entity.AuditoriaRol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditoriaRolRepository extends JpaRepository<AuditoriaRol, Integer> {
    List<AuditoriaRol> findTop500ByOrderByFechaDesc();
}
