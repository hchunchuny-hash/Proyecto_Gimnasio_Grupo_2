package com.gimnasio.gimnasio_backend.modulo2.repository;

import com.gimnasio.gimnasio_backend.modulo2.entity.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Integer> {

    @Procedure(procedureName = "dbo.sp_ActualizarMembresiasVencidas")
    void actualizarMembresiasVencidas();

    List<Membresia> findByClienteClienteId(Integer clienteId);

    @Query("SELECT m FROM Membresia m WHERE m.cliente.clienteId = :clienteId AND m.estado = 'ACTIVA'")
    Optional<Membresia> findMembresiaActivaByClienteId(Integer clienteId);
}