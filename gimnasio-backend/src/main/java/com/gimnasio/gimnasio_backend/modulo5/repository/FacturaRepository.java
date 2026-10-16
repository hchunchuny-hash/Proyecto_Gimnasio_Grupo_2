package com.gimnasio.gimnasio_backend.modulo5.repository;

import com.gimnasio.gimnasio_backend.modulo5.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    Optional<Factura> findByPagoPagoId(Integer pagoId);
    java.util.List<Factura> findByPagoMembresiaClienteClienteId(Integer clienteId);
}
