package com.gimnasio.gimnasio_backend.modulo4.repository;

import com.gimnasio.gimnasio_backend.modulo4.entity.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Integer> {
}
