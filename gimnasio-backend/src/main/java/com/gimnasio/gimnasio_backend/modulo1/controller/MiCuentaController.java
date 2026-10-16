package com.gimnasio.gimnasio_backend.modulo1.controller;

import com.gimnasio.gimnasio_backend.modulo2.dto.ClienteDTO;
import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import com.gimnasio.gimnasio_backend.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/mi-cuenta")
public class MiCuentaController {
    private final ClienteRepository clientes;
    public MiCuentaController(ClienteRepository clientes) { this.clientes = clientes; }

    @GetMapping("/perfil")
    public ClienteDTO.Response perfil(@AuthenticationPrincipal AuthenticatedUser user) {
        Cliente cliente = clientes.findByUsuarioUsuarioId(user.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de cliente no encontrado."));
        ClienteDTO.Response result = new ClienteDTO.Response();
        result.setClienteId(cliente.getClienteId());
        result.setUsuarioId(user.usuarioId());
        result.setNombres(cliente.getNombres());
        result.setApellidos(cliente.getApellidos());
        result.setTelefono(cliente.getTelefono());
        result.setFechaNacimiento(cliente.getFechaNacimiento());
        result.setEstado(cliente.getEstado());
        return result;
    }
}
