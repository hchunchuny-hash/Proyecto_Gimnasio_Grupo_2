package com.gimnasio.gimnasio_backend.modulo4.service;

import com.gimnasio.gimnasio_backend.modulo2.entity.Entrenador;
import com.gimnasio.gimnasio_backend.modulo2.repository.EntrenadorRepository;
import com.gimnasio.gimnasio_backend.modulo4.dto.HorarioClaseDTO;
import com.gimnasio.gimnasio_backend.modulo4.entity.Clase;
import com.gimnasio.gimnasio_backend.modulo4.entity.HorarioClase;
import com.gimnasio.gimnasio_backend.modulo4.repository.ClaseRepository;
import com.gimnasio.gimnasio_backend.modulo4.repository.HorarioClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorarioClaseService {

    @Autowired
    private HorarioClaseRepository horarioRepository;

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    // Horarios desde hoy en adelante
    @Transactional(readOnly = true)
    public List<HorarioClaseDTO.Response> listarProximos() {
        return horarioRepository.findByFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(LocalDate.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HorarioClaseDTO.Response> listarPorClase(Integer claseId) {
        return horarioRepository.findByClaseClaseIdOrderByFechaAscHoraInicioAsc(claseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public HorarioClaseDTO.Response crear(HorarioClaseDTO.Request req) {
        if (req.getClaseId() == null || req.getEntrenadorId() == null) {
            throw new RuntimeException("Debe indicar el ID de la clase y el ID del entrenador.");
        }
        if (req.getFecha() == null || req.getHoraInicio() == null || req.getHoraFin() == null) {
            throw new RuntimeException("La fecha, la hora de inicio y la hora de fin son obligatorias.");
        }
        if (!req.getHoraFin().isAfter(req.getHoraInicio())) {
            throw new RuntimeException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        if (req.getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha del horario no puede ser anterior a hoy.");
        }

        Clase clase = claseRepository.findById(req.getClaseId())
                .orElseThrow(() -> new RuntimeException("Clase no encontrada."));
        Entrenador entrenador = entrenadorRepository.findById(req.getEntrenadorId())
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado."));

        if (!Boolean.TRUE.equals(clase.getEstado())) {
            throw new RuntimeException("La clase no está activa.");
        }

        HorarioClase horario = HorarioClase.builder()
                .clase(clase)
                .entrenador(entrenador)
                .fecha(req.getFecha())
                .horaInicio(req.getHoraInicio())
                .horaFin(req.getHoraFin())
                .build();

        return mapToResponse(horarioRepository.save(horario));
    }

    private HorarioClaseDTO.Response mapToResponse(HorarioClase h) {
        HorarioClaseDTO.Response res = new HorarioClaseDTO.Response();
        res.setHorarioId(h.getHorarioId());
        res.setClaseId(h.getClase().getClaseId());
        res.setNombreClase(h.getClase().getNombre());
        res.setEntrenadorId(h.getEntrenador().getEntrenadorId());
        res.setNombreEntrenador(h.getEntrenador().getNombres() + " " + h.getEntrenador().getApellidos());
        res.setFecha(h.getFecha());
        res.setHoraInicio(h.getHoraInicio());
        res.setHoraFin(h.getHoraFin());
        return res;
    }
}
