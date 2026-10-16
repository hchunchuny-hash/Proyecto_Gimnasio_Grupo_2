package com.gimnasio.gimnasio_backend.security;

import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import com.gimnasio.gimnasio_backend.modulo1.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionTokenServiceTest {
    @Test void emitsRandomSessionAndRevokesIt() {
        UsuarioRepository users = mock(UsuarioRepository.class);
        when(users.findById(12)).thenReturn(Optional.of(Usuario.builder().usuarioId(12).nombreUsuario("ana").rol("CLIENTE").estado(true).build()));
        SessionTokenService service = new SessionTokenService(users);
        String token = service.create(Usuario.builder().usuarioId(12).build());
        assertNotNull(token);
        assertEquals("ana", service.authenticate(token).nombreUsuario());
        service.revoke(token);
        assertNull(service.authenticate(token));
    }

    @Test void inactiveAccountInvalidatesItsSession() {
        UsuarioRepository users = mock(UsuarioRepository.class);
        when(users.findById(12)).thenReturn(Optional.of(Usuario.builder().usuarioId(12).nombreUsuario("ana").rol("CLIENTE").estado(false).build()));
        SessionTokenService service = new SessionTokenService(users);
        String token = service.create(Usuario.builder().usuarioId(12).build());
        assertNull(service.authenticate(token));
    }
}
