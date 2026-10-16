package com.gimnasio.gimnasio_backend.modulo4.service;

import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import com.gimnasio.gimnasio_backend.modulo4.dto.AsistenciaDTO;
import com.gimnasio.gimnasio_backend.modulo4.entity.Asistencia;
import com.gimnasio.gimnasio_backend.modulo4.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Marca la entrada usando el procedimiento sp_RegistrarAsistencia
    @Transactional
    public AsistenciaDTO.Response marcar(AsistenciaDTO.MarcarRequest req) {
        if (req.getClienteId() == null) {
            throw new RuntimeException("Debe indicar el ID del cliente.");
        }
        clienteRepository.findById(req.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        asistenciaRepository.registrarAsistencia(req.getClienteId());

        Asistencia ultima = asistenciaRepository
                .findTopByClienteClienteIdOrderByAsistenciaIdDesc(req.getClienteId())
                .orElseThrow(() -> new RuntimeException("No se pudo registrar la asistencia."));

        return mapToResponse(ultima);
    }

    @Transactional
    public AsistenciaDTO.Response marcarSalida(Integer asistenciaId) {
        Asistencia asistencia = asistenciaRepository.findById(asistenciaId)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + asistenciaId));

        if (!"VALIDADA".equals(asistencia.getEstado())) {
            throw new RuntimeException("No se puede registrar la salida de una asistencia rechazada.");
        }
        if (asistencia.getHoraSalida() != null) {
            throw new RuntimeException("La salida de esta asistencia ya fue registrada.");
        }

        LocalTime ahora = LocalTime.now();
        if (ahora.isBefore(asistencia.getHoraEntrada())) {
            throw new RuntimeException("La hora de salida no puede ser anterior a la hora de entrada.");
        }

        asistencia.setHoraSalida(ahora);
        return mapToResponse(asistenciaRepository.save(asistencia));
    }

    @Transactional(readOnly = true)
    public List<AsistenciaDTO.Response> listarPorCliente(Integer clienteId) {
        return asistenciaRepository.findByClienteClienteIdOrderByFechaDescHoraEntradaDesc(clienteId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AsistenciaDTO.Response mapToResponse(Asistencia a) {
        AsistenciaDTO.Response res = new AsistenciaDTO.Response();
        res.setAsistenciaId(a.getAsistenciaId());
        res.setClienteId(a.getCliente().getClienteId());
        res.setNombreCliente(a.getCliente().getNombres() + " " + a.getCliente().getApellidos());
        res.setFecha(a.getFecha());
        res.setHoraEntrada(a.getHoraEntrada());
        res.setHoraSalida(a.getHoraSalida());
        res.setEstado(a.getEstado());
        res.setMensaje("VALIDADA".equals(a.getEstado())
                ? "Acceso permitido."
                : "Acceso rechazado: el cliente no posee una membresía vigente.");
        return res;
    }
}
