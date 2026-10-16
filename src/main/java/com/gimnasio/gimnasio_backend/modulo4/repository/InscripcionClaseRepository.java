package com.gimnasio.gimnasio_backend.modulo4.repository;

import com.gimnasio.gimnasio_backend.modulo4.entity.InscripcionClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionClaseRepository extends JpaRepository<InscripcionClase, Integer> {

    boolean existsByClaseClaseIdAndClienteClienteId(Integer claseId, Integer clienteId);

    long countByClaseClaseIdAndEstado(Integer claseId, String estado);

    List<InscripcionClase> findByClienteClienteId(Integer clienteId);
}
