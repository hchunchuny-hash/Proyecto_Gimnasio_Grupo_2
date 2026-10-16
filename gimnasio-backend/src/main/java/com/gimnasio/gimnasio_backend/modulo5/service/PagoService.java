package com.gimnasio.gimnasio_backend.modulo5.service;

import com.gimnasio.gimnasio_backend.modulo2.entity.Membresia;
import com.gimnasio.gimnasio_backend.modulo2.repository.MembresiaRepository;
import com.gimnasio.gimnasio_backend.modulo5.dto.PagoDTO;
import com.gimnasio.gimnasio_backend.modulo5.entity.Descuento;
import com.gimnasio.gimnasio_backend.modulo5.entity.Factura;
import com.gimnasio.gimnasio_backend.modulo5.entity.Pago;
import com.gimnasio.gimnasio_backend.modulo5.repository.DescuentoRepository;
import com.gimnasio.gimnasio_backend.modulo5.repository.FacturaRepository;
import com.gimnasio.gimnasio_backend.modulo5.repository.IngresoMensualProjection;
import com.gimnasio.gimnasio_backend.modulo5.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private static final List<String> METODOS_VALIDOS = Arrays.asList("EFECTIVO", "TARJETA", "TRANSFERENCIA");

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private MembresiaRepository membresiaRepository;

    @Autowired
    private DescuentoRepository descuentoRepository;

    @Transactional(readOnly = true)
    public List<PagoDTO.Response> listarTodos() {
        return pagoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO.Response> listarPorCliente(Integer clienteId) {
        return pagoRepository.findByMembresiaClienteClienteId(clienteId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IngresoMensualProjection> obtenerIngresosMensuales() {
        return pagoRepository.obtenerIngresosMensuales();
    }

    @Transactional
    public PagoDTO.Response procesar(PagoDTO.ProcesarRequest req) {
        if (req.getMembresiaId() == null) {
            throw new RuntimeException("Debe indicar el ID de la membresía.");
        }
        if (req.getMetodoPago() == null || req.getMetodoPago().trim().isEmpty()) {
            throw new RuntimeException("Debe indicar el método de pago (EFECTIVO, TARJETA o TRANSFERENCIA).");
        }
        String metodo = req.getMetodoPago().trim().toUpperCase();
        if (!METODOS_VALIDOS.contains(metodo)) {
            throw new RuntimeException("Método de pago inválido. Use EFECTIVO, TARJETA o TRANSFERENCIA.");
        }

        Membresia membresia = membresiaRepository.findById(req.getMembresiaId())
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada."));

        if ("CANCELADA".equals(membresia.getEstado())) {
            throw new RuntimeException("No se puede registrar un pago para una membresía cancelada.");
        }
        if (pagoRepository.existsByMembresiaMembresiaIdAndEstado(membresia.getMembresiaId(), "PAGADO")) {
            throw new RuntimeException("Esta membresía ya tiene un pago registrado.");
        }

        BigDecimal montoOriginal = membresia.getPrecio().setScale(2, RoundingMode.HALF_UP);
        BigDecimal montoDescuento = BigDecimal.ZERO.setScale(2);
        Descuento descuento = null;

        if (req.getDescuentoId() != null) {
            descuento = descuentoRepository.findById(req.getDescuentoId())
                    .orElseThrow(() -> new RuntimeException("Descuento no encontrado."));

            LocalDate hoy = LocalDate.now();
            boolean vigente = Boolean.TRUE.equals(descuento.getEstado())
                    && !hoy.isBefore(descuento.getFechaInicio())
                    && (descuento.getFechaFin() == null || !hoy.isAfter(descuento.getFechaFin()));
            if (!vigente) {
                throw new RuntimeException("El descuento no está vigente.");
            }

            montoDescuento = montoOriginal
                    .multiply(descuento.getPorcentaje())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }

        BigDecimal montoPagado = montoOriginal.subtract(montoDescuento);

        Pago pago = Pago.builder()
                .membresia(membresia)
                .descuento(descuento)
                .fechaPago(LocalDateTime.now())
                .montoOriginal(montoOriginal)
                .montoDescuento(montoDescuento)
                .montoPagado(montoPagado)
                .metodoPago(metodo)
                .estado("PAGADO")
                .build();
        Pago guardado = pagoRepository.save(pago);

        // Cada pago genera su factura automáticamente
        Factura factura = Factura.builder()
                .pago(guardado)
                .numeroFactura("FAC-" + LocalDate.now().getYear() + "-" + String.format("%06d", guardado.getPagoId()))
                .fechaEmision(LocalDateTime.now())
                .total(montoPagado)
                .build();
        Factura facturaGuardada = facturaRepository.save(factura);

        return mapToResponse(guardado, facturaGuardada);
    }

    @Transactional
    public PagoDTO.Response anular(Integer id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        if ("ANULADO".equals(pago.getEstado())) {
            throw new RuntimeException("El pago ya está anulado.");
        }
        pago.setEstado("ANULADO");
        return mapToResponse(pagoRepository.save(pago));
    }

    private PagoDTO.Response mapToResponse(Pago p) {
        Factura factura = facturaRepository.findByPagoPagoId(p.getPagoId()).orElse(null);
        return mapToResponse(p, factura);
    }

    private PagoDTO.Response mapToResponse(Pago p, Factura f) {
        PagoDTO.Response res = new PagoDTO.Response();
        res.setPagoId(p.getPagoId());
        res.setMembresiaId(p.getMembresia().getMembresiaId());
        res.setClienteId(p.getMembresia().getCliente().getClienteId());
        res.setNombreCliente(p.getMembresia().getCliente().getNombres() + " "
                + p.getMembresia().getCliente().getApellidos());
        if (p.getDescuento() != null) {
            res.setDescuentoId(p.getDescuento().getDescuentoId());
            res.setNombreDescuento(p.getDescuento().getNombre());
        }
        res.setFechaPago(p.getFechaPago());
        res.setMontoOriginal(p.getMontoOriginal());
        res.setMontoDescuento(p.getMontoDescuento());
        res.setMontoPagado(p.getMontoPagado());
        res.setMetodoPago(p.getMetodoPago());
        res.setEstado(p.getEstado());
        if (f != null) {
            res.setFacturaId(f.getFacturaId());
            res.setNumeroFactura(f.getNumeroFactura());
        }
        return res;
    }
}
