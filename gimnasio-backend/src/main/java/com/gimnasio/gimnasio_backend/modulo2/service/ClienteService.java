package com.gimnasio.gimnasio_backend.modulo2.service;

import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import com.gimnasio.gimnasio_backend.modulo1.repository.UsuarioRepository;
import com.gimnasio.gimnasio_backend.modulo2.dto.ClienteDTO;
import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRiesgoProjection;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<ClienteDTO.Response> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO.Response obtenerPorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return mapToResponse(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteRiesgoProjection> obtenerClientesEnRiesgo() {
        return clienteRepository.obtenerClientesEnRiesgo();
    }

    @Transactional
    public ClienteDTO.Response crear(ClienteDTO.Request req) {
        Usuario usuario = usuarioRepository.findById(req.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("El usuario especificado no existe."));

        if (!Boolean.TRUE.equals(usuario.getEstado())) {
            throw new RuntimeException("No se puede asociar un perfil a un usuario inactivo.");
        }

        if (clienteRepository.existsByUsuarioUsuarioId(req.getUsuarioId())) {
            throw new RuntimeException("El usuario ya cuenta con un perfil de Cliente registrado.");
        }

        Cliente cliente = Cliente.builder()
                .usuario(usuario)
                .nombres(req.getNombres().trim())
                .apellidos(req.getApellidos().trim())
                .telefono(req.getTelefono())
                .fechaNacimiento(req.getFechaNacimiento())
                .estado(true)
                .build();

        return mapToResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteDTO.Response actualizar(Integer id, ClienteDTO.Request req) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        cliente.setNombres(req.getNombres().trim());
        cliente.setApellidos(req.getApellidos().trim());
        cliente.setTelefono(req.getTelefono());
        cliente.setFechaNacimiento(req.getFechaNacimiento());

        return mapToResponse(clienteRepository.save(cliente));
    }

    private ClienteDTO.Response mapToResponse(Cliente c) {
        ClienteDTO.Response res = new ClienteDTO.Response();
        res.setClienteId(c.getClienteId());
        res.setUsuarioId(c.getUsuario().getUsuarioId());
        res.setNombres(c.getNombres());
        res.setApellidos(c.getApellidos());
        res.setTelefono(c.getTelefono());
        res.setFechaNacimiento(c.getFechaNacimiento());
        res.setEstado(c.getEstado());
        return res;
    }
}