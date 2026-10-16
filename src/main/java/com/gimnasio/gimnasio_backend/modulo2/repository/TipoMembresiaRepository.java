package com.gimnasio.gimnasio_backend.modulo2.repository;

import com.gimnasio.gimnasio_backend.modulo2.entity.TipoMembresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoMembresiaRepository extends JpaRepository<TipoMembresia, Integer> {
    List<TipoMembresia> findByEstadoTrue();
}