package com.gimnasio.gimnasio_backend.modulo1.repository;

import com.gimnasio.gimnasio_backend.modulo1.entity.Equipamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamientoRepository extends JpaRepository<Equipamiento, Integer> {
    List<Equipamiento> findByAreaGimnasioAreaGimnasioId(Integer areaGimnasioId);
}