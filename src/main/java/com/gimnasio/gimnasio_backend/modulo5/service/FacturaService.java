package com.gimnasio.gimnasio_backend.modulo5.service;

import com.gimnasio.gimnasio_backend.modulo5.dto.FacturaDTO;
import com.gimnasio.gimnasio_backend.modulo5.entity.Factura;
import com.gimnasio.gimnasio_backend.modulo5.entity.Pago;
import com.gimnasio.gimnasio_backend.modulo5.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Transactional(readOnly = true)
    public List<FacturaDTO.Response> listarTodas() {
        return facturaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FacturaDTO.Response> listarPorCliente(Integer clienteId) {
        return facturaRepository.findByPagoMembresiaClienteClienteId(clienteId).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FacturaDTO.Response obtenerPorId(Integer id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
        return mapToResponse(factura);
    }

    @Transactional(readOnly = true)
    public FacturaDTO.Response obtenerPorPago(Integer pagoId) {
        Factura factura = facturaRepository.findByPagoPagoId(pagoId)
                .orElseThrow(() -> new RuntimeException("No existe factura para el pago con ID: " + pagoId));
        return mapToResponse(factura);
    }

    private FacturaDTO.Response mapToResponse(Factura f) {
        Pago p = f.getPago();
        FacturaDTO.Response res = new FacturaDTO.Response();
        res.setFacturaId(f.getFacturaId());
        res.setNumeroFactura(f.getNumeroFactura());
        res.setFechaEmision(f.getFechaEmision());
        res.setTotal(f.getTotal());
        res.setPagoId(p.getPagoId());
        res.setMembresiaId(p.getMembresia().getMembresiaId());
        res.setTipoMembresia(p.getMembresia().getTipoMembresia().getNombre());
        res.setClienteId(p.getMembresia().getCliente().getClienteId());
        res.setNombreCliente(p.getMembresia().getCliente().getNombres() + " "
                + p.getMembresia().getCliente().getApellidos());
        res.setMontoOriginal(p.getMontoOriginal());
        res.setMontoDescuento(p.getMontoDescuento());
        res.setMontoPagado(p.getMontoPagado());
        res.setMetodoPago(p.getMetodoPago());
        res.setEstadoPago(p.getEstado());
        return res;
    }
}
