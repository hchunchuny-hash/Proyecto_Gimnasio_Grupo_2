package com.gimnasio.gimnasio_backend.modulo1.service;

import com.gimnasio.gimnasio_backend.modulo1.dto.UsuarioAdminDTO.*;
import com.gimnasio.gimnasio_backend.modulo1.entity.AuditoriaRol;
import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import com.gimnasio.gimnasio_backend.modulo1.repository.AuditoriaRolRepository;
import com.gimnasio.gimnasio_backend.modulo1.repository.UsuarioRepository;
import com.gimnasio.gimnasio_backend.security.SessionTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioAdminService {
    private static final Set<String> ROLES = Set.of("ADMINISTRADOR", "ADMIN", "EMPLEADO", "RECEPCION", "ENTRENADOR", "CLIENTE");
    private static final Set<String> ROLES_ADMIN = Set.of("ADMINISTRADOR", "ADMIN");
    private final UsuarioRepository usuarios;
    private final AuditoriaRolRepository auditoria;
    private final SessionTokenService sesiones;

    public UsuarioAdminService(UsuarioRepository usuarios, AuditoriaRolRepository auditoria, SessionTokenService sesiones) {
        this.usuarios = usuarios; this.auditoria = auditoria; this.sesiones = sesiones;
    }

    public List<UsuarioView> listar(String q) {
        List<Usuario> lista;
        if (q == null || q.isBlank()) lista = usuarios.findAll();
        else {
            java.util.LinkedHashMap<Integer, Usuario> encontrados = new java.util.LinkedHashMap<>();
            usuarios.findByNombreUsuarioContainingIgnoreCaseOrCorreoContainingIgnoreCase(q.trim(), q.trim())
                    .forEach(u -> encontrados.put(u.getUsuarioId(), u));
            try { usuarios.findById(Integer.valueOf(q.trim())).ifPresent(u -> encontrados.put(u.getUsuarioId(), u)); }
            catch (NumberFormatException ignored) { }
            lista = List.copyOf(encontrados.values());
        }
        return lista.stream().map(this::vista).toList();
    }

    @Transactional
    public synchronized UsuarioView cambiarRol(Integer actorId, Integer targetId, String nuevoRol) {
        nuevoRol = nuevoRol.trim().toUpperCase();
        if (!ROLES.contains(nuevoRol)) throw new IllegalArgumentException("Rol no reconocido.");
        if (actorId.equals(targetId)) throw new IllegalArgumentException("No puede cambiar su propio rol.");
        Usuario target = usuarios.findById(targetId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        String anterior = target.getRol();
        if (ROLES_ADMIN.contains(anterior) && !ROLES_ADMIN.contains(nuevoRol) && target.getEstado()
                && usuarios.countByEstadoTrueAndRolIn(ROLES_ADMIN) <= 1) {
            throw new IllegalArgumentException("No se puede retirar el rol al último administrador activo.");
        }
        target.setRol(nuevoRol);
        usuarios.save(target);
        auditoria.save(AuditoriaRol.builder().administradorId(actorId).usuarioAfectadoId(targetId)
                .rolAnterior(anterior).rolNuevo(nuevoRol).resultado("APLICADO").build());
        sesiones.revokeUser(targetId);
        return vista(target);
    }

    @Transactional
    public synchronized EstadoView cambiarEstado(Integer actorId, Integer targetId, boolean activo) {
        if (actorId.equals(targetId) && !activo) throw new IllegalArgumentException("No puede desactivar su propia cuenta.");
        Usuario target = usuarios.findById(targetId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        if (!activo && target.getEstado() && ROLES_ADMIN.contains(target.getRol())
                && usuarios.countByEstadoTrueAndRolIn(ROLES_ADMIN) <= 1) {
            throw new IllegalArgumentException("No se puede desactivar al último administrador activo.");
        }
        target.setEstado(activo);
        usuarios.save(target);
        if (!activo) sesiones.revokeUser(targetId);
        auditoria.save(AuditoriaRol.builder().administradorId(actorId).usuarioAfectadoId(targetId)
                .rolAnterior(target.getRol()).rolNuevo(target.getRol()).resultado(activo ? "ACTIVADO" : "DESACTIVADO").build());
        return new EstadoView(target.getUsuarioId(), target.getNombreUsuario(), target.getRol(), target.getEstado());
    }

    public List<AuditoriaView> auditoriaRoles() {
        return auditoria.findTop500ByOrderByFechaDesc().stream().map(a -> new AuditoriaView(a.getAuditoriaRolId(),
                a.getAdministradorId(), a.getUsuarioAfectadoId(), a.getRolAnterior(), a.getRolNuevo(), a.getFecha(), a.getResultado())).toList();
    }

    private UsuarioView vista(Usuario u) { return new UsuarioView(u.getUsuarioId(), u.getNombreUsuario(), u.getCorreo(), u.getRol(), u.getEstado()); }
}
