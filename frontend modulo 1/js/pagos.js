proteger('pagos', 'Pagos');
const dlg = $('#dlg'), f = $('#f');
let lista = [];
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function cargar() {
  try {
    if (esCliente()) {
      const yo = await miCliente();
      lista = await api('/pagos/cliente/' + yo.clienteId);
      pintar(); return;
    }
    const [p, m, d, i] = await Promise.all([api('/pagos'), api('/membresias'), api('/descuentos'), api('/pagos/ingresos-mensuales')]);
    lista = p;
    f.membresiaId.innerHTML = opciones(m.filter(x => x.estado === 'ACTIVA'), 'membresiaId', x => x.nombreCliente + ' - ' + x.nombreTipoMembresia + ' (' + dinero(x.precio) + ')');
    f.descuentoId.innerHTML = '<option value="">Sin descuento</option>' + opciones(d.filter(x => x.estado), 'descuentoId', x => x.nombre + ' (' + x.porcentaje + '%)');
    ingresos(i); pintar();
  } catch (e) { toast(e.message, 'error'); }
}

function ingresos(i) {
  $('#ingresos').innerHTML = i.length ? '<table><thead><tr><th>Año</th><th>Mes</th><th>Tipo de membresía</th><th>Pagos</th><th>Ingresos</th></tr></thead><tbody>' +
    i.map(x => `<tr><td>${x.anio}</td><td>${x.mes}</td><td>${esc(x.tipoMembresia)}</td><td>${x.cantidadPagos}</td><td><b>${dinero(x.ingresos)}</b></td></tr>`).join('') + '</tbody></table>'
    : '<p class="vacio">Aún no hay ingresos registrados.</p>';
}

function pintar() {
  const t = $('#buscar').value.toLowerCase();
  const v = lista.filter(x => ((x.nombreCliente || '') + ' ' + (x.numeroFactura || '')).toLowerCase().includes(t));
  $('#filas').innerHTML = v.length ? v.map(x => `<tr><td>${fechaHora(x.fechaPago)}</td><td><b>${esc(x.nombreCliente)}</b></td><td>${dinero(x.montoOriginal)}</td><td>${dinero(x.montoDescuento)}</td><td><b>${dinero(x.montoPagado)}</b></td><td>${esc(x.metodoPago)}</td><td>${pill(x.estado)}</td><td>${esc(x.numeroFactura)}</td>
    <td class="acc">${!esCliente() && x.estado === 'PAGADO' ? `<button class="btn del" onclick="anular(${x.pagoId})">Anular</button>` : ''}</td></tr>`).join('') : vacio(9, 'No hay pagos para mostrar.');
}

function abrir() { f.reset(); dlg.showModal(); }

f.onsubmit = async e => {
  e.preventDefault();
  const d = { membresiaId: Number(f.membresiaId.value), descuentoId: f.descuentoId.value ? Number(f.descuentoId.value) : null, metodoPago: f.metodoPago.value };
  try { await api('/pagos/procesar', 'POST', d); dlg.close(); toast('Pago procesado y factura generada'); cargar(); } catch (err) { toast(err.message, 'error'); }
};

async function anular(id) {
  if (!confirm('¿Anular este pago?')) return;
  try { await api('/pagos/' + id + '/anular', 'PUT'); toast('Pago anulado'); cargar(); } catch (err) { toast(err.message, 'error'); }
}
cargar();
