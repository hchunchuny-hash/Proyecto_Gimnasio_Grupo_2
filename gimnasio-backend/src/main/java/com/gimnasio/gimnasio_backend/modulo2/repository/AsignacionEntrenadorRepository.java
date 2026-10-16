package com.gimnasio.gimnasio_backend.modulo2.repository;

import com.gimnasio.gimnasio_backend.modulo2.entity.AsignacionEntrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionEntrenadorRepository extends JpaRepository<AsignacionEntrenador, Integer> {
    List<AsignacionEntrenador> findByClienteClienteIdAndEstadoTrue(Integer clienteId);
    List<AsignacionEntrenador> findByEntrenadorEntrenadorIdAndEstadoTrue(Integer entrenadorId);
}