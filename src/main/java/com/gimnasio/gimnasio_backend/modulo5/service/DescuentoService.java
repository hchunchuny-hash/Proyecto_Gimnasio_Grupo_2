package com.gimnasio.gimnasio_backend.modulo5.service;

import com.gimnasio.gimnasio_backend.modulo5.dto.DescuentoDTO;
import com.gimnasio.gimnasio_backend.modulo5.entity.Descuento;
import com.gimnasio.gimnasio_backend.modulo5.repository.DescuentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DescuentoService {

    @Autowired
    private DescuentoRepository descuentoRepository;

    @Transactional(readOnly = true)
    public List<DescuentoDTO.Response> listarTodos() {
        return descuentoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DescuentoDTO.Response crear(DescuentoDTO.Request req) {
        validar(req);

        Descuento descuento = Descuento.builder()
                .nombre(req.getNombre().trim())
                .porcentaje(req.getPorcentaje())
                .fechaInicio(req.getFechaInicio())
                .fechaFin(req.getFechaFin())
                .estado(req.getEstado() != null ? req.getEstado() : true)
                .build();

        return mapToResponse(descuentoRepository.save(descuento));
    }

    @Transactional
    public DescuentoDTO.Response actualizar(Integer id, DescuentoDTO.Request req) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado con ID: " + id));
        validar(req);

        descuento.setNombre(req.getNombre().trim());
        descuento.setPorcentaje(req.getPorcentaje());
        descuento.setFechaInicio(req.getFechaInicio());
        descuento.setFechaFin(req.getFechaFin());
        if (req.getEstado() != null) {
            descuento.setEstado(req.getEstado());
        }

        return mapToResponse(descuentoRepository.save(descuento));
    }

    private void validar(DescuentoDTO.Request req) {
        if (req.getNombre() == null || req.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del descuento es obligatorio.");
        }
        if (req.getNombre().trim().length() > 100) {
            throw new RuntimeException("El nombre del descuento no puede superar 100 caracteres.");
        }
        if (req.getPorcentaje() == null
                || req.getPorcentaje().compareTo(BigDecimal.ZERO) < 0
                || req.getPorcentaje().compareTo(new BigDecimal("100")) > 0) {
            throw new RuntimeException("El porcentaje debe estar entre 0 y 100.");
        }
        if (req.getFechaInicio() == null) {
            throw new RuntimeException("La fecha de inicio es obligatoria.");
        }
        if (req.getFechaFin() != null && req.getFechaFin().isBefore(req.getFechaInicio())) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
    }

    private DescuentoDTO.Response mapToResponse(Descuento d) {
        DescuentoDTO.Response res = new DescuentoDTO.Response();
        res.setDescuentoId(d.getDescuentoId());
        res.setNombre(d.getNombre());
        res.setPorcentaje(d.getPorcentaje());
        res.setFechaInicio(d.getFechaInicio());
        res.setFechaFin(d.getFechaFin());
        res.setEstado(d.getEstado());
        return res;
    }
}
