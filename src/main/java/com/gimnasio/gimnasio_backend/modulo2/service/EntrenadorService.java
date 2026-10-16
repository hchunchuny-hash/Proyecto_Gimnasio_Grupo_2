package com.gimnasio.gimnasio_backend.modulo2.service;

import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import com.gimnasio.gimnasio_backend.modulo1.repository.UsuarioRepository;
import com.gimnasio.gimnasio_backend.modulo2.dto.EntrenadorDTO;
import com.gimnasio.gimnasio_backend.modulo2.entity.Entrenador;
import com.gimnasio.gimnasio_backend.modulo2.repository.EntrenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntrenadorService {

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<EntrenadorDTO.Response> listarTodos() {
        return entrenadorRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EntrenadorDTO.Response obtenerPorId(Integer id) {
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado con ID: " + id));
        return mapToResponse(entrenador);
    }

    @Transactional
    public EntrenadorDTO.Response crear(EntrenadorDTO.Request req) {
        Usuario usuario = usuarioRepository.findById(req.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("El usuario especificado no existe."));

        if (!Boolean.TRUE.equals(usuario.getEstado())) {
            throw new RuntimeException("No se puede asociar un perfil a un usuario inactivo.");
        }

        if (entrenadorRepository.existsByUsuarioUsuarioId(req.getUsuarioId())) {
            throw new RuntimeException("El usuario ya cuenta con un perfil de Entrenador registrado.");
        }

        Entrenador entrenador = Entrenador.builder()
                .usuario(usuario)
                .nombres(req.getNombres().trim())
                .apellidos(req.getApellidos().trim())
                .especialidad(req.getEspecialidad())
                .telefono(req.getTelefono())
                .estado(true)
                .build();

        return mapToResponse(entrenadorRepository.save(entrenador));
    }

    @Transactional
    public EntrenadorDTO.Response actualizar(Integer id, EntrenadorDTO.Request req) {
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado con ID: " + id));

        entrenador.setNombres(req.getNombres().trim());
        entrenador.setApellidos(req.getApellidos().trim());
        entrenador.setEspecialidad(req.getEspecialidad());
        entrenador.setTelefono(req.getTelefono());

        return mapToResponse(entrenadorRepository.save(entrenador));
    }

    private EntrenadorDTO.Response mapToResponse(Entrenador e) {
        EntrenadorDTO.Response res = new EntrenadorDTO.Response();
        res.setEntrenadorId(e.getEntrenadorId());
        res.setUsuarioId(e.getUsuario().getUsuarioId());
        res.setNombres(e.getNombres());
        res.setApellidos(e.getApellidos());
        res.setEspecialidad(e.getEspecialidad());
        res.setTelefono(e.getTelefono());
        res.setEstado(e.getEstado());
        return res;
    }
}