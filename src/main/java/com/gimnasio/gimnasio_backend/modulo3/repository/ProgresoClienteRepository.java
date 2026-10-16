package com.gimnasio.gimnasio_backend.modulo3.repository;

import com.gimnasio.gimnasio_backend.modulo3.entity.ProgresoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgresoClienteRepository extends JpaRepository<ProgresoCliente, Integer> {
    List<ProgresoCliente> findByClienteClienteIdOrderByFechaDesc(Integer clienteId);
}