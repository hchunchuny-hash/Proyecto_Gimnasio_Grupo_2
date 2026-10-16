package com.gimnasio.gimnasio_backend.modulo5.repository;

import com.gimnasio.gimnasio_backend.modulo5.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByMembresiaClienteClienteId(Integer clienteId);

    boolean existsByMembresiaMembresiaIdAndEstado(Integer membresiaId, String estado);

    // Mapea directamente la vista SQL dbo.vw_IngresosMensuales
    @Query(value = "SELECT Anio AS anio, Mes AS mes, TipoMembresia AS tipoMembresia, "
                 + "CantidadPagos AS cantidadPagos, Ingresos AS ingresos "
                 + "FROM dbo.vw_IngresosMensuales", nativeQuery = true)
    List<IngresoMensualProjection> obtenerIngresosMensuales();
}
