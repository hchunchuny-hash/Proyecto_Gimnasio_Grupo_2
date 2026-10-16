package com.gimnasio.gimnasio_backend.modulo1.service;

import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import com.gimnasio.gimnasio_backend.modulo1.repository.AuditoriaRolRepository;
import com.gimnasio.gimnasio_backend.modulo1.repository.UsuarioRepository;
import com.gimnasio.gimnasio_backend.security.SessionTokenService;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioAdminServiceTest {
    @Test void preventsChangingOwnRole() {
        UsuarioAdminService service = new UsuarioAdminService(mock(UsuarioRepository.class), mock(AuditoriaRolRepository.class), mock(SessionTokenService.class));
        assertThrows(IllegalArgumentException.class, () -> service.cambiarRol(7, 7, "CLIENTE"));
    }

    @Test void protectsLastActiveAdministrator() {
        UsuarioRepository users = mock(UsuarioRepository.class);
        when(users.findById(8)).thenReturn(Optional.of(Usuario.builder().usuarioId(8).rol("ADMINISTRADOR").estado(true).build()));
        when(users.countByEstadoTrueAndRolIn(anyCollection())).thenReturn(1L);
        UsuarioAdminService service = new UsuarioAdminService(users, mock(AuditoriaRolRepository.class), mock(SessionTokenService.class));
        assertThrows(IllegalArgumentException.class, () -> service.cambiarRol(7, 8, "EMPLEADO"));
        verify(users, never()).save(any());
    }

    @Test void persistsRoleChangeWritesAuditAndRevokesSessions() {
        UsuarioRepository users = mock(UsuarioRepository.class);
        AuditoriaRolRepository audit = mock(AuditoriaRolRepository.class);
        SessionTokenService sessions = mock(SessionTokenService.class);
        Usuario target = Usuario.builder().usuarioId(8).nombreUsuario("empleado").correo("e@example.com").rol("CLIENTE").estado(true).build();
        when(users.findById(8)).thenReturn(Optional.of(target));
        when(users.save(any())).thenAnswer(call -> call.getArgument(0));
        UsuarioAdminService service = new UsuarioAdminService(users, audit, sessions);
        assertEquals("EMPLEADO", service.cambiarRol(7, 8, "empleado").rol());
        verify(audit).save(any());
        verify(sessions).revokeUser(8);
    }
}
