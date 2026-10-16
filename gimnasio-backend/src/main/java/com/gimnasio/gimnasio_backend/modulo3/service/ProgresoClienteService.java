package com.gimnasio.gimnasio_backend.modulo3.service;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import com.gimnasio.gimnasio_backend.modulo3.dto.ProgresoClienteDTO;
import com.gimnasio.gimnasio_backend.modulo3.entity.ProgresoCliente;
import com.gimnasio.gimnasio_backend.modulo3.repository.ProgresoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgresoClienteService {

    @Autowired
    private ProgresoClienteRepository progresoClienteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ProgresoClienteDTO.Response> listarPorCliente(Integer clienteId) {
        return progresoClienteRepository.findByClienteClienteIdOrderByFechaDesc(clienteId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProgresoClienteDTO.Response registrar(ProgresoClienteDTO.Request req) {
        Cliente cliente = clienteRepository.findById(req.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        ProgresoCliente progreso = ProgresoCliente.builder()
                .cliente(cliente)
                .fecha((req.getFecha() != null) ? req.getFecha() : LocalDate.now())
                .peso(req.getPeso())
                .observaciones(req.getObservaciones())
                .build();

        return mapToResponse(progresoClienteRepository.save(progreso));
    }

    private ProgresoClienteDTO.Response mapToResponse(ProgresoCliente p) {
        ProgresoClienteDTO.Response res = new ProgresoClienteDTO.Response();
        res.setProgresoId(p.getProgresoId());
        res.setClienteId(p.getCliente().getClienteId());
        res.setFecha(p.getFecha());
        res.setPeso(p.getPeso());
        res.setObservaciones(p.getObservaciones());
        return res;
    }
}