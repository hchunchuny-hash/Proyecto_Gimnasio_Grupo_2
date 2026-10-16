package com.gimnasio.gimnasio_backend.modulo1.repository;

import com.gimnasio.gimnasio_backend.modulo1.entity.TokenRecuperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Integer> {
    
    // Buscar por el token generado
    Optional<TokenRecuperacion> findByToken(String token);
    
    // Buscar un token válido que no haya sido consumido aún
    Optional<TokenRecuperacion> findByTokenAndUsadoFalse(String token);
}