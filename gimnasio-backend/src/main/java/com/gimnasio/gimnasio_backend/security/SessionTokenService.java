package com.gimnasio.gimnasio_backend.security;

import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import com.gimnasio.gimnasio_backend.modulo1.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionTokenService {
    private static final long TTL_SECONDS = 8 * 60 * 60;
    private final SecureRandom random = new SecureRandom();
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final UsuarioRepository usuarios;

    public SessionTokenService(UsuarioRepository usuarios) { this.usuarios = usuarios; }

    public String create(Usuario usuario) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        sessions.put(token, new Session(usuario.getUsuarioId(), Instant.now().plusSeconds(TTL_SECONDS)));
        return token;
    }

    public AuthenticatedUser authenticate(String token) {
        Session session = sessions.get(token);
        if (session == null) return null;
        if (session.expiresAt().isBefore(Instant.now())) { sessions.remove(token); return null; }
        Usuario usuario = usuarios.findById(session.usuarioId()).orElse(null);
        if (usuario == null || !Boolean.TRUE.equals(usuario.getEstado())) { sessions.remove(token); return null; }
        return new AuthenticatedUser(usuario.getUsuarioId(), usuario.getNombreUsuario(), usuario.getRol());
    }

    public void revoke(String token) { if (token != null) sessions.remove(token); }

    public void revokeUser(Integer usuarioId) {
        sessions.entrySet().removeIf(entry -> entry.getValue().usuarioId().equals(usuarioId));
    }

    private record Session(Integer usuarioId, Instant expiresAt) { }
}
