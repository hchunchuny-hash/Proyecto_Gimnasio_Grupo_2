package com.gimnasio.gimnasio_backend.modulo3.repository;

import com.gimnasio.gimnasio_backend.modulo3.entity.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Integer> {
    List<Rutina> findByClienteClienteIdAndEstadoTrue(Integer clienteId);
    List<Rutina> findByEntrenadorEntrenadorIdAndEstadoTrue(Integer entrenadorId);
}