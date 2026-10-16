package com.gimnasio.gimnasio_backend.modulo4.repository;

import com.gimnasio.gimnasio_backend.modulo4.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {

    // Ejecuta el procedimiento dbo.sp_RegistrarAsistencia(@ClienteID).
    // El procedimiento decide solo si la asistencia queda VALIDADA o RECHAZADA.
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "EXEC dbo.sp_RegistrarAsistencia @ClienteID = :clienteId", nativeQuery = true)
    void registrarAsistencia(@Param("clienteId") Integer clienteId);

    Optional<Asistencia> findTopByClienteClienteIdOrderByAsistenciaIdDesc(Integer clienteId);

    List<Asistencia> findByClienteClienteIdOrderByFechaDescHoraEntradaDesc(Integer clienteId);
}
