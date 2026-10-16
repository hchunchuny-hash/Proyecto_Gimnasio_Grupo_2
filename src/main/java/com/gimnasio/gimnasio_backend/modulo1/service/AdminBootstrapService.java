package com.gimnasio.gimnasio_backend.modulo1.service;

import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import com.gimnasio.gimnasio_backend.modulo1.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminBootstrapService implements ApplicationRunner {
    private final UsuarioRepository usuarios;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Value("${app.bootstrap.admin.username:}") private String username;
    @Value("${app.bootstrap.admin.email:}") private String email;
    @Value("${app.bootstrap.admin.password:}") private String password;

    public AdminBootstrapService(UsuarioRepository usuarios) { this.usuarios = usuarios; }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (usuarios.countByEstadoTrueAndRolIn(java.util.Set.of("ADMINISTRADOR", "ADMIN")) > 0) return;
        boolean configured = !username.isBlank() || !email.isBlank() || !password.isBlank();
        if (!configured) return;
        if (username.isBlank() || email.isBlank() || password.length() < 12 || password.length() > 72)
            throw new IllegalStateException("Configure usuario, correo y una contraseña inicial de 12 a 72 caracteres para crear el primer administrador.");
        if (usuarios.existsByNombreUsuario(username) || usuarios.existsByCorreo(email))
            throw new IllegalStateException("El usuario o correo configurado para el administrador inicial ya existe.");
        usuarios.save(Usuario.builder().nombreUsuario(username.trim()).correo(email.trim())
                .contrasena(encoder.encode(password)).rol("ADMINISTRADOR").estado(true).build());
    }
}
