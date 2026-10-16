package com.gimnasio.gimnasio_backend.modulo3.service;

import com.gimnasio.gimnasio_backend.modulo2.entity.Cliente;
import com.gimnasio.gimnasio_backend.modulo2.entity.Entrenador;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import com.gimnasio.gimnasio_backend.modulo2.repository.EntrenadorRepository;
import com.gimnasio.gimnasio_backend.modulo3.dto.RutinaDTO;
import com.gimnasio.gimnasio_backend.modulo3.entity.DiaRutina;
import com.gimnasio.gimnasio_backend.modulo3.entity.Ejercicio;
import com.gimnasio.gimnasio_backend.modulo3.entity.Rutina;
import com.gimnasio.gimnasio_backend.modulo3.entity.RutinaEjercicio;
import com.gimnasio.gimnasio_backend.modulo3.repository.EjercicioRepository;
import com.gimnasio.gimnasio_backend.modulo3.repository.RutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RutinaService {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    public List<RutinaDTO.Response> listarPorCliente(Integer clienteId) {
        return rutinaRepository.findByClienteClienteIdAndEstadoTrue(clienteId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RutinaDTO.Response crearRutina(RutinaDTO.Request req) {
        Cliente cliente = clienteRepository.findById(req.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        Entrenador entrenador = entrenadorRepository.findById(req.getEntrenadorId())
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado."));

        Rutina rutina = Rutina.builder()
                .cliente(cliente)
                .entrenador(entrenador)
                .nombre(req.getNombre())
                .objetivo(req.getObjetivo())
                .fechaInicio((req.getFechaInicio() != null) ? req.getFechaInicio() : LocalDate.now())
                .fechaFin(req.getFechaFin())
                .estado(true)
                .build();

        if (req.getDias() != null) {
            List<DiaRutina> dias = req.getDias().stream().map(dReq -> {
                DiaRutina diaRutina = DiaRutina.builder()
                        .rutina(rutina)
                        .diaSemana(dReq.getDiaSemana())
                        .ordenDia(dReq.getOrdenDia())
                        .build();

                if (dReq.getEjercicios() != null) {
                    List<RutinaEjercicio> ejercicios = dReq.getEjercicios().stream().map(eReq -> {
                        Ejercicio ejercicio = ejercicioRepository.findById(eReq.getEjercicioId())
                                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado: " + eReq.getEjercicioId()));

                        return RutinaEjercicio.builder()
                                .diaRutina(diaRutina)
                                .ejercicio(ejercicio)
                                .series(eReq.getSeries())
                                .repeticiones(eReq.getRepeticiones())
                                .descansoSegundos((eReq.getDescansoSegundos() != null) ? eReq.getDescansoSegundos() : 60)
                                .observaciones(eReq.getObservaciones())
                                .build();
                    }).collect(Collectors.toList());

                    diaRutina.setEjercicios(ejercicios);
                }
                return diaRutina;
            }).collect(Collectors.toList());

            rutina.setDias(dias);
        }

        return mapToResponse(rutinaRepository.save(rutina));
    }

    private RutinaDTO.Response mapToResponse(Rutina r) {
        RutinaDTO.Response res = new RutinaDTO.Response();
        res.setRutinaId(r.getRutinaId());
        res.setClienteId(r.getCliente().getClienteId());
        res.setNombreCliente(r.getCliente().getNombres() + " " + r.getCliente().getApellidos());
        res.setEntrenadorId(r.getEntrenador().getEntrenadorId());
        res.setNombreEntrenador(r.getEntrenador().getNombres() + " " + r.getEntrenador().getApellidos());
        res.setNombre(r.getNombre());
        res.setObjetivo(r.getObjetivo());
        res.setFechaInicio(r.getFechaInicio());
        res.setFechaFin(r.getFechaFin());
        res.setEstado(r.getEstado());

        if (r.getDias() != null) {
            res.setDias(r.getDias().stream().map(d -> {
                RutinaDTO.DiaResponse dRes = new RutinaDTO.DiaResponse();
                dRes.setDiaRutinaId(d.getDiaRutinaId());
                dRes.setDiaSemana(d.getDiaSemana());
                dRes.setOrdenDia(d.getOrdenDia());

                if (d.getEjercicios() != null) {
                    dRes.setEjercicios(d.getEjercicios().stream().map(e -> {
                        RutinaDTO.EjercicioResponse eRes = new RutinaDTO.EjercicioResponse();
                        eRes.setRutinaEjercicioId(e.getRutinaEjercicioId());
                        eRes.setEjercicioId(e.getEjercicio().getEjercicioId());
                        eRes.setNombreEjercicio(e.getEjercicio().getNombre());
                        eRes.setSeries(e.getSeries());
                        eRes.setRepeticiones(e.getRepeticiones());
                        eRes.setDescansoSegundos(e.getDescansoSegundos());
                        eRes.setObservaciones(e.getObservaciones());
                        return eRes;
                    }).collect(Collectors.toList()));
                }
                return dRes;
            }).collect(Collectors.toList()));
        }

        return res;
    }
}