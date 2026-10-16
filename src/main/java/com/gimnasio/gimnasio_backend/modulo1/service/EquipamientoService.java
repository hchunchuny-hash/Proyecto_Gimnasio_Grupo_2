package com.gimnasio.gimnasio_backend.modulo1.service;

import com.gimnasio.gimnasio_backend.modulo1.dto.EquipamientoDTO;
import com.gimnasio.gimnasio_backend.modulo1.entity.AreaGimnasio;
import com.gimnasio.gimnasio_backend.modulo1.entity.Equipamiento;
import com.gimnasio.gimnasio_backend.modulo1.repository.AreaGimnasioRepository;
import com.gimnasio.gimnasio_backend.modulo1.repository.EquipamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipamientoService {

    @Autowired
    private EquipamientoRepository equipamientoRepository;

    @Autowired
    private AreaGimnasioRepository areaGimnasioRepository;

    @Transactional(readOnly = true)
    public List<EquipamientoDTO.Response> listarTodos() {
        return equipamientoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EquipamientoDTO.Response obtenerPorId(Integer id) {
        Equipamiento eq = equipamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado con ID: " + id));
        return mapToResponse(eq);
    }

    @Transactional
    public EquipamientoDTO.Response crear(EquipamientoDTO.Request req) {
        AreaGimnasio area = areaGimnasioRepository.findById(req.getAreaGimnasioId())
                .orElseThrow(() -> new RuntimeException("El área asignada no existe."));

        if (!Boolean.TRUE.equals(area.getEstado())) {
            throw new RuntimeException("No se puede asignar un equipamiento a un área inactiva.");
        }

        Equipamiento eq = Equipamiento.builder()
                .nombre(req.getNombre())
                .numeroSerie(req.getNumeroSerie())
                .fechaAdquisicion(req.getFechaAdquisicion())
                .estado(req.getEstado())
                .areaGimnasio(area)
                .build();

        return mapToResponse(equipamientoRepository.save(eq));
    }

    @Transactional
    public EquipamientoDTO.Response actualizar(Integer id, EquipamientoDTO.Request req) {
        Equipamiento eq = equipamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado con ID: " + id));

        AreaGimnasio area = areaGimnasioRepository.findById(req.getAreaGimnasioId())
                .orElseThrow(() -> new RuntimeException("El área asignada no existe."));

        if (!Boolean.TRUE.equals(area.getEstado())) {
            throw new RuntimeException("No se puede asignar un equipamiento a un área inactiva.");
        }

        eq.setNombre(req.getNombre());
        eq.setNumeroSerie(req.getNumeroSerie());
        eq.setFechaAdquisicion(req.getFechaAdquisicion());
        eq.setEstado(req.getEstado());
        eq.setAreaGimnasio(area);

        return mapToResponse(equipamientoRepository.save(eq));
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!equipamientoRepository.existsById(id)) {
            throw new RuntimeException("No existe el equipamiento con ID: " + id);
        }
        equipamientoRepository.deleteById(id);
    }

    private EquipamientoDTO.Response mapToResponse(Equipamiento eq) {
        EquipamientoDTO.Response res = new EquipamientoDTO.Response();
        res.setEquipamientoId(eq.getEquipamientoId());
        res.setNombre(eq.getNombre());
        res.setNumeroSerie(eq.getNumeroSerie());
        res.setFechaAdquisicion(eq.getFechaAdquisicion());
        res.setEstado(eq.getEstado());
        res.setAreaGimnasioId(eq.getAreaGimnasio().getAreaGimnasioId());
        res.setNombreArea(eq.getAreaGimnasio().getNombre());
        return res;
    }
}