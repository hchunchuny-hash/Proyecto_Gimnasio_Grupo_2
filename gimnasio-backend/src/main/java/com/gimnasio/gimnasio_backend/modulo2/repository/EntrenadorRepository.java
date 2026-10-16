package com.gimnasio.gimnasio_backend.modulo2.repository;

import com.gimnasio.gimnasio_backend.modulo2.entity.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Integer> {
    Optional<Entrenador> findByUsuarioUsuarioId(Integer usuarioId);
    Boolean existsByUsuarioUsuarioId(Integer usuarioId);
    List<Entrenador> findByEstadoTrue();
}