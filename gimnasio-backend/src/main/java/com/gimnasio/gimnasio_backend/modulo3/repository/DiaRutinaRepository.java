package com.gimnasio.gimnasio_backend.modulo3.repository;

import com.gimnasio.gimnasio_backend.modulo3.entity.DiaRutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaRutinaRepository extends JpaRepository<DiaRutina, Integer> {
    List<DiaRutina> findByRutinaRutinaIdOrderByOrdenDiaAsc(Integer rutinaId);
}