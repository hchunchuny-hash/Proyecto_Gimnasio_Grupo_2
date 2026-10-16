package com.gimnasio.gimnasio_backend.modulo2.controller;

import com.gimnasio.gimnasio_backend.modulo2.entity.TipoMembresia;
import com.gimnasio.gimnasio_backend.modulo2.repository.TipoMembresiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-membresia")
public class TipoMembresiaController {

    @Autowired
    private TipoMembresiaRepository tipoMembresiaRepository;

    @GetMapping
    public ResponseEntity<List<TipoMembresia>> listar() {
        return ResponseEntity.ok(tipoMembresiaRepository.findByEstadoTrue());
    }
}