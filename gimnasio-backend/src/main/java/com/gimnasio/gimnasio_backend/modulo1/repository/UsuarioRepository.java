package com.gimnasio.gimnasio_backend.modulo1.repository;

import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByCorreo(String correo);
    
    // Método para validar que coincidan ambos datos
    Optional<Usuario> findByNombreUsuarioAndCorreo(String nombreUsuario, String correo);

    Boolean existsByNombreUsuario(String nombreUsuario);
    Boolean existsByCorreo(String correo);
    long countByEstadoTrueAndRolIn(java.util.Collection<String> roles);
    java.util.List<Usuario> findByNombreUsuarioContainingIgnoreCaseOrCorreoContainingIgnoreCase(String nombreUsuario, String correo);
}
