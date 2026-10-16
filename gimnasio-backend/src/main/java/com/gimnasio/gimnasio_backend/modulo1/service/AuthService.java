package com.gimnasio.gimnasio_backend.modulo1.service;

import com.gimnasio.gimnasio_backend.modulo1.dto.AuthDTOs.*;
import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import com.gimnasio.gimnasio_backend.modulo1.repository.UsuarioRepository;
import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import com.gimnasio.gimnasio_backend.security.SessionTokenService;

@Service
public class AuthService {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private SessionTokenService sessionTokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByNombreUsuario(request.getNombreUsuario())) {
            throw new RuntimeException("El nombre de usuario ya está registrado.");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        String rol = request.getRol() == null || request.getRol().isBlank()
                ? "CLIENTE" : request.getRol().trim().toUpperCase();
        if (!"CLIENTE".equals(rol)) {
            throw new RuntimeException("El registro público solo permite crear cuentas de cliente.");
        }

        // 1. Guardar Usuario
        Usuario usuario = Usuario.builder()
                .nombreUsuario(request.getNombreUsuario())
                .correo(request.getCorreo())
                .contrasena(PASSWORD_ENCODER.encode(request.getContrasena()))
                .rol(rol)
                .estado(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        // 2. Creación del perfil correspondiente dentro de la misma transacción
        validarNombresYApellidos(request.getNombres(), request.getApellidos(), "CLIENTE");

        Cliente cliente = Cliente.builder()
                .usuario(guardado)
                .nombres(request.getNombres().trim())
                .apellidos(request.getApellidos().trim())
                .telefono(request.getTelefono())
                .fechaNacimiento(request.getFechaNacimiento())
                .estado(true)
                .build();

        clienteRepository.save(cliente);

        // 3. Mantenimiento del contrato de la respuesta para el frontend
        AuthResponse response = new AuthResponse();
        response.setUsuarioId(guardado.getUsuarioId());
        response.setNombreUsuario(guardado.getNombreUsuario());
        response.setCorreo(guardado.getCorreo());
        response.setRol(guardado.getRol());
        response.setToken(sessionTokenService.create(guardado));

        return response;
    }

    private void validarNombresYApellidos(String nombres, String apellidos, String rolNombre) {
        if (nombres == null || nombres.trim().isEmpty() || apellidos == null || apellidos.trim().isEmpty()) {
            throw new RuntimeException("Los campos 'nombres' y 'apellidos' son obligatorios para el registro de un " + rolNombre + ".");
        }
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(request.getNombreUsuario())
                .orElseThrow(() -> new BadCredentialsException("Usuario o contraseña incorrectos."));

        String almacenada = usuario.getContrasena();
        boolean esHash = almacenada != null && (almacenada.startsWith("$2a$") || almacenada.startsWith("$2b$") || almacenada.startsWith("$2y$"));
        boolean coincide = esHash
                ? PASSWORD_ENCODER.matches(request.getContrasena(), almacenada)
                : almacenada != null && almacenada.equals(request.getContrasena());
        if (!coincide) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos.");
        }

        // Migra de forma gradual las claves antiguas al formato BCrypt tras un acceso válido.
        if (!esHash) {
            usuario.setContrasena(PASSWORD_ENCODER.encode(request.getContrasena()));
            usuarioRepository.save(usuario);
        }

        if (!usuario.getEstado()) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos.");
        }

        AuthResponse response = new AuthResponse();
        response.setUsuarioId(usuario.getUsuarioId());
        response.setNombreUsuario(usuario.getNombreUsuario());
        response.setCorreo(usuario.getCorreo());
        response.setRol(usuario.getRol());
        response.setToken(sessionTokenService.create(usuario));

        return response;
    }
}
