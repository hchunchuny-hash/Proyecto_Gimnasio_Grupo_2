package com.gimnasio.gimnasio_backend.modulo5.repository;

import com.gimnasio.gimnasio_backend.modulo5.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findAllByOrderByFechaEnvioDesc();
    List<Notificacion> findByClienteClienteIdOrderByFechaEnvioDesc(Integer clienteId);
}
