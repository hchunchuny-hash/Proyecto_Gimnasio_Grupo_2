package com.gimnasio.gimnasio_backend.modulo4.service;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import com.gimnasio.gimnasio_backend.modulo4.dto.ReservaDTO;
import com.gimnasio.gimnasio_backend.modulo4.entity.Reserva;
import com.gimnasio.gimnasio_backend.modulo4.repository.ReservaRepository;
import com.gimnasio.gimnasio_backend.modulo5.entity.Servicio;
import com.gimnasio.gimnasio_backend.modulo5.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private static final List<String> ESTADOS_VALIDOS =
            Arrays.asList("PENDIENTE", "CONFIRMADA", "FINALIZADA", "CANCELADA");

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<ReservaDTO.Response> listarTodas() {
        return reservaRepository.findAllByOrderByFechaDescHoraInicioDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservaDTO.Response> listarPorCliente(Integer clienteId) {
        return reservaRepository.findByClienteClienteIdOrderByFechaDescHoraInicioDesc(clienteId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservaDTO.Response crear(ReservaDTO.Request req) {
        if (req.getClienteId() == null || req.getServicioId() == null) {
            throw new RuntimeException("Debe indicar el ID del cliente y el ID del servicio.");
        }
        if (req.getFecha() == null || req.getHoraInicio() == null) {
            throw new RuntimeException("La fecha y la hora de inicio son obligatorias.");
        }
        if (req.getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de la reserva no puede ser anterior a hoy.");
        }
        if (req.getHoraFin() != null && !req.getHoraFin().isAfter(req.getHoraInicio())) {
            throw new RuntimeException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        if (req.getObservaciones() != null && req.getObservaciones().length() > 500) {
            throw new RuntimeException("Las observaciones no pueden superar 500 caracteres.");
        }

        Cliente cliente = clienteRepository.findById(req.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
        Servicio servicio = servicioRepository.findById(req.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado."));

        if (!Boolean.TRUE.equals(servicio.getEstado())) {
            throw new RuntimeException("El servicio no está disponible.");
        }

        Reserva reserva = Reserva.builder()
                .cliente(cliente)
                .servicio(servicio)
                .fecha(req.getFecha())
                .horaInicio(req.getHoraInicio())
                .horaFin(req.getHoraFin())
                .estado("PENDIENTE")
                .observaciones(req.getObservaciones())
                .build();

        return mapToResponse(reservaRepository.save(reserva));
    }

    @Transactional
    public ReservaDTO.Response cambiarEstado(Integer id, ReservaDTO.EstadoRequest req) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));

        if (req.getEstado() == null || !ESTADOS_VALIDOS.contains(req.getEstado().trim().toUpperCase())) {
            throw new RuntimeException("Estado inválido. Use PENDIENTE, CONFIRMADA, FINALIZADA o CANCELADA.");
        }
        if ("CANCELADA".equals(reserva.getEstado()) || "FINALIZADA".equals(reserva.getEstado())) {
            throw new RuntimeException("No se puede cambiar el estado de una reserva " + reserva.getEstado() + ".");
        }

        reserva.setEstado(req.getEstado().trim().toUpperCase());
        return mapToResponse(reservaRepository.save(reserva));
    }

    private ReservaDTO.Response mapToResponse(Reserva r) {
        ReservaDTO.Response res = new ReservaDTO.Response();
        res.setReservaId(r.getReservaId());
        res.setClienteId(r.getCliente().getClienteId());
        res.setNombreCliente(r.getCliente().getNombres() + " " + r.getCliente().getApellidos());
        res.setServicioId(r.getServicio().getServicioId());
        res.setNombreServicio(r.getServicio().getNombre());
        res.setFecha(r.getFecha());
        res.setHoraInicio(r.getHoraInicio());
        res.setHoraFin(r.getHoraFin());
        res.setEstado(r.getEstado());
        res.setObservaciones(r.getObservaciones());
        return res;
    }
}
