package com.gimnasio.gimnasio_backend.modulo5.service;

import com.gimnasio.gimnasio_backend.modulo5.dto.ServicioDTO;
import com.gimnasio.gimnasio_backend.modulo5.entity.Servicio;
import com.gimnasio.gimnasio_backend.modulo5.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<ServicioDTO.Response> listarTodos() {
        return servicioRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicioDTO.Response obtenerPorId(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
        return mapToResponse(servicio);
    }

    @Transactional
    public ServicioDTO.Response crear(ServicioDTO.Request req) {
        validar(req);
        String nombre = req.getNombre().trim();
        if (servicioRepository.existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe un servicio con el nombre: " + nombre);
        }

        Servicio servicio = Servicio.builder()
                .nombre(nombre)
                .descripcion(req.getDescripcion())
                .precio(req.getPrecio())
                .estado(req.getEstado() != null ? req.getEstado() : true)
                .build();

        return mapToResponse(servicioRepository.save(servicio));
    }

    @Transactional
    public ServicioDTO.Response actualizar(Integer id, ServicioDTO.Request req) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
        validar(req);

        String nombre = req.getNombre().trim();
        if (!nombre.equalsIgnoreCase(servicio.getNombre()) && servicioRepository.existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe un servicio con el nombre: " + nombre);
        }

        servicio.setNombre(nombre);
        servicio.setDescripcion(req.getDescripcion());
        servicio.setPrecio(req.getPrecio());
        if (req.getEstado() != null) {
            servicio.setEstado(req.getEstado());
        }

        return mapToResponse(servicioRepository.save(servicio));
    }

    private void validar(ServicioDTO.Request req) {
        if (req.getNombre() == null || req.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del servicio es obligatorio.");
        }
        if (req.getNombre().trim().length() > 100) {
            throw new RuntimeException("El nombre del servicio no puede superar 100 caracteres.");
        }
        if (req.getDescripcion() != null && req.getDescripcion().length() > 250) {
            throw new RuntimeException("La descripción no puede superar 250 caracteres.");
        }
        if (req.getPrecio() == null || req.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El precio es obligatorio y no puede ser negativo.");
        }
    }

    private ServicioDTO.Response mapToResponse(Servicio s) {
        ServicioDTO.Response res = new ServicioDTO.Response();
        res.setServicioId(s.getServicioId());
        res.setNombre(s.getNombre());
        res.setDescripcion(s.getDescripcion());
        res.setPrecio(s.getPrecio());
        res.setEstado(s.getEstado());
        return res;
    }
}
