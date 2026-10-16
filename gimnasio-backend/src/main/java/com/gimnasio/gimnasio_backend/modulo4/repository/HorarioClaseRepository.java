package com.gimnasio.gimnasio_backend.modulo4.repository;

import com.gimnasio.gimnasio_backend.modulo4.entity.HorarioClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HorarioClaseRepository extends JpaRepository<HorarioClase, Integer> {

    List<HorarioClase> findByFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(LocalDate fecha);

    List<HorarioClase> findByClaseClaseIdOrderByFechaAscHoraInicioAsc(Integer claseId);
}
