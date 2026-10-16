package com.gimnasio.gimnasio_backend.modulo3.repository;

import com.gimnasio.gimnasio_backend.modulo3.entity.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {
    List<Ejercicio> findByEstadoTrue();
    List<Ejercicio> findByGrupoMuscularIdAndEstadoTrue(Integer grupoMuscularId);
}