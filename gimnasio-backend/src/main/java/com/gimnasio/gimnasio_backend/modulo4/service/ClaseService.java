package com.gimnasio.gimnasio_backend.modulo4.service;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import com.gimnasio.gimnasio_backend.modulo4.dto.ClaseDTO;
import com.gimnasio.gimnasio_backend.modulo4.entity.Clase;
import com.gimnasio.gimnasio_backend.modulo4.entity.InscripcionClase;
import com.gimnasio.gimnasio_backend.modulo4.repository.ClaseRepository;
import com.gimnasio.gimnasio_backend.modulo4.repository.InscripcionClaseRepository;
import com.gimnasio.gimnasio_backend.modulo5.entity.Servicio;
import com.gimnasio.gimnasio_backend.modulo5.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private InscripcionClaseRepository inscripcionRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClaseDTO.Response> listarTodas() {
        return claseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClaseDTO.Response crear(ClaseDTO.Request req) {
        if (req.getServicioId() == null) {
            throw new RuntimeException("Debe indicar el ID del servicio.");
        }
        if (req.getNombre() == null || req.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la clase es obligatorio.");
        }
        if (req.getNombre().trim().length() > 100) {
            throw new RuntimeException("El nombre de la clase no puede superar 100 caracteres.");
        }
        if (req.getDescripcion() != null && req.getDescripcion().length() > 250) {
            throw new RuntimeException("La descripción no puede superar 250 caracteres.");
        }
        if (req.getCupoMaximo() == null || req.getCupoMaximo() <= 0) {
            throw new RuntimeException("El cupo máximo debe ser mayor que 0.");
        }

        Servicio servicio = servicioRepository.findById(req.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado."));

        Clase clase = Clase.builder()
                .servicio(servicio)
                .nombre(req.getNombre().trim())
                .descripcion(req.getDescripcion())
                .cupoMaximo(req.getCupoMaximo())
                .estado(req.getEstado() != null ? req.getEstado() : true)
                .build();

        return mapToResponse(claseRepository.save(clase));
    }

    @Transactional
    public ClaseDTO.InscripcionResponse inscribir(ClaseDTO.InscribirRequest req) {
        if (req.getClaseId() == null || req.getClienteId() == null) {
            throw new RuntimeException("Debe indicar el ID de la clase y el ID del cliente.");
        }

        Clase clase = claseRepository.findById(req.getClaseId())
                .orElseThrow(() -> new RuntimeException("Clase no encontrada."));
        Cliente cliente = clienteRepository.findById(req.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        if (!Boolean.TRUE.equals(clase.getEstado())) {
            throw new RuntimeException("La clase no está activa.");
        }
        if (inscripcionRepository.existsByClaseClaseIdAndClienteClienteId(clase.getClaseId(), cliente.getClienteId())) {
            throw new RuntimeException("El cliente ya tiene una inscripción en esta clase.");
        }
        long inscritos = inscripcionRepository.countByClaseClaseIdAndEstado(clase.getClaseId(), "ACTIVA");
        if (inscritos >= clase.getCupoMaximo()) {
            throw new RuntimeException("La clase no tiene cupo disponible.");
        }

        InscripcionClase inscripcion = InscripcionClase.builder()
                .clase(clase)
                .cliente(cliente)
                .fechaInscripcion(LocalDateTime.now())
                .estado("ACTIVA")
                .build();

        return mapToInscripcionResponse(inscripcionRepository.save(inscripcion));
    }

    @Transactional(readOnly = true)
    public List<ClaseDTO.InscripcionResponse> listarInscripcionesPorCliente(Integer clienteId) {
        return inscripcionRepository.findByClienteClienteId(clienteId).stream()
                .map(this::mapToInscripcionResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClaseDTO.InscripcionResponse cancelarInscripcion(Integer inscripcionId) {
        InscripcionClase inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + inscripcionId));
        if (!"ACTIVA".equals(inscripcion.getEstado())) {
            throw new RuntimeException("Solo se puede cancelar una inscripción ACTIVA.");
        }
        inscripcion.setEstado("CANCELADA");
        return mapToInscripcionResponse(inscripcionRepository.save(inscripcion));
    }

    private ClaseDTO.Response mapToResponse(Clase c) {
        ClaseDTO.Response res = new ClaseDTO.Response();
        res.setClaseId(c.getClaseId());
        res.setServicioId(c.getServicio().getServicioId());
        res.setNombreServicio(c.getServicio().getNombre());
        res.setNombre(c.getNombre());
        res.setDescripcion(c.getDescripcion());
        res.setCupoMaximo(c.getCupoMaximo());
        res.setInscritos((int) inscripcionRepository.countByClaseClaseIdAndEstado(c.getClaseId(), "ACTIVA"));
        res.setEstado(c.getEstado());
        return res;
    }

    private ClaseDTO.InscripcionResponse mapToInscripcionResponse(InscripcionClase i) {
        ClaseDTO.InscripcionResponse res = new ClaseDTO.InscripcionResponse();
        res.setInscripcionId(i.getInscripcionId());
        res.setClaseId(i.getClase().getClaseId());
        res.setNombreClase(i.getClase().getNombre());
        res.setClienteId(i.getCliente().getClienteId());
        res.setNombreCliente(i.getCliente().getNombres() + " " + i.getCliente().getApellidos());
        res.setFechaInscripcion(i.getFechaInscripcion());
        res.setEstado(i.getEstado());
        return res;
    }
}
