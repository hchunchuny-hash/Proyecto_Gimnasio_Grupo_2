package com.gimnasio.gimnasio_backend.modulo5.repository;

import com.gimnasio.gimnasio_backend.modulo5.entity.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Integer> {
}
