package com.gimnasio.gimnasio_backend.modulo4.repository;

import com.gimnasio.gimnasio_backend.modulo4.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    List<Reserva> findAllByOrderByFechaDescHoraInicioDesc();

    List<Reserva> findByClienteClienteIdOrderByFechaDescHoraInicioDesc(Integer clienteId);
}
