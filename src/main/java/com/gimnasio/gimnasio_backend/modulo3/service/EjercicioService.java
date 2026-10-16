package com.gimnasio.gimnasio_backend.modulo3.service;

import com.gimnasio.gimnasio_backend.modulo3.dto.EjercicioDTO;
import com.gimnasio.gimnasio_backend.modulo3.entity.Ejercicio;
import com.gimnasio.gimnasio_backend.modulo3.repository.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EjercicioService {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    public List<EjercicioDTO.Response> listarTodos() {
        return ejercicioRepository.findByEstadoTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EjercicioDTO.Response crear(EjercicioDTO.Request req) {
        Ejercicio ejercicio = Ejercicio.builder()
                .nombre(req.getNombre())
                .grupoMuscularId(req.getGrupoMuscularId())
                .nivelDificultadId(req.getNivelDificultadId())
                .equipamientoId(req.getEquipamientoId())
                .descripcion(req.getDescripcion())
                .estado(true)
                .build();

        return mapToResponse(ejercicioRepository.save(ejercicio));
    }

    private EjercicioDTO.Response mapToResponse(Ejercicio e) {
        EjercicioDTO.Response res = new EjercicioDTO.Response();
        res.setEjercicioId(e.getEjercicioId());
        res.setNombre(e.getNombre());
        res.setGrupoMuscularId(e.getGrupoMuscularId());
        res.setNivelDificultadId(e.getNivelDificultadId());
        res.setEquipamientoId(e.getEquipamientoId());
        res.setDescripcion(e.getDescripcion());
        res.setEstado(e.getEstado());
        return res;
    }
}