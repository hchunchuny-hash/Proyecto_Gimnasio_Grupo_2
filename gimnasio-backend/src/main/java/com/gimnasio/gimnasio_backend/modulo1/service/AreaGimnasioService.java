package com.gimnasio.gimnasio_backend.modulo1.service;

import com.gimnasio.gimnasio_backend.modulo1.entity.AreaGimnasio;
import com.gimnasio.gimnasio_backend.modulo1.repository.AreaGimnasioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AreaGimnasioService {

    @Autowired
    private AreaGimnasioRepository areaRepository;

    @Transactional(readOnly = true)
    public List<AreaGimnasio> listarTodas() {
        return areaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AreaGimnasio obtenerPorId(Integer id) {
        return areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada con ID: " + id));
    }

    @Transactional
    public AreaGimnasio crear(AreaGimnasio area) {
        if (area.getNombre() == null || area.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del área es obligatorio.");
        }
        return areaRepository.save(area);
    }

    @Transactional
    public AreaGimnasio actualizar(Integer id, AreaGimnasio areaDetails) {
        AreaGimnasio area = obtenerPorId(id);
        
        if (areaDetails.getNombre() == null || areaDetails.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del área es obligatorio.");
        }

        area.setNombre(areaDetails.getNombre());
        area.setDescripcion(areaDetails.getDescripcion());
        area.setEstado(areaDetails.getEstado());

        return areaRepository.save(area);
    }
}