package com.gimnasio.gimnasio_backend.modulo5.service;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import com.gimnasio.gimnasio_backend.modulo5.dto.NotificacionDTO;
import com.gimnasio.gimnasio_backend.modulo5.entity.Notificacion;
import com.gimnasio.gimnasio_backend.modulo5.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<NotificacionDTO.Response> listar(Integer clienteId) {
        List<Notificacion> lista = (clienteId != null)
                ? notificacionRepository.findByClienteClienteIdOrderByFechaEnvioDesc(clienteId)
                : notificacionRepository.findAllByOrderByFechaEnvioDesc();
        return lista.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificacionDTO.Response crear(NotificacionDTO.Request req) {
        if (req.getClienteId() == null) {
            throw new RuntimeException("Debe indicar el ID del cliente.");
        }
        if (req.getTitulo() == null || req.getTitulo().trim().isEmpty()) {
            throw new RuntimeException("El título es obligatorio.");
        }
        if (req.getTitulo().trim().length() > 150) {
            throw new RuntimeException("El título no puede superar 150 caracteres.");
        }
        if (req.getMensaje() == null || req.getMensaje().trim().isEmpty()) {
            throw new RuntimeException("El mensaje es obligatorio.");
        }
        if (req.getMensaje().trim().length() > 500) {
            throw new RuntimeException("El mensaje no puede superar 500 caracteres.");
        }

        Cliente cliente = clienteRepository.findById(req.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        Notificacion notificacion = Notificacion.builder()
                .cliente(cliente)
                .titulo(req.getTitulo().trim())
                .mensaje(req.getMensaje().trim())
                .fechaEnvio(LocalDateTime.now())
                .leida(false)
                .build();

        return mapToResponse(notificacionRepository.save(notificacion));
    }

    @Transactional
    public NotificacionDTO.Response marcarLeida(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));
        notificacion.setLeida(true);
        return mapToResponse(notificacionRepository.save(notificacion));
    }

    private NotificacionDTO.Response mapToResponse(Notificacion n) {
        NotificacionDTO.Response res = new NotificacionDTO.Response();
        res.setNotificacionId(n.getNotificacionId());
        res.setClienteId(n.getCliente().getClienteId());
        res.setNombreCliente(n.getCliente().getNombres() + " " + n.getCliente().getApellidos());
        res.setTitulo(n.getTitulo());
        res.setMensaje(n.getMensaje());
        res.setFechaEnvio(n.getFechaEnvio());
        res.setLeida(n.getLeida());
        return res;
    }
}
