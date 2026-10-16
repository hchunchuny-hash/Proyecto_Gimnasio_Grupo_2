proteger('facturas', 'Facturas');
let lista = [];
async function cargar() {
  try {
    lista = esCliente() ? await api('/facturas/cliente/' + (await miCliente()).clienteId) : await api('/facturas');
    pintar();
  } catch (e) { toast(e.message, 'error'); }
}
function pintar() {
  const t = $('#buscar').value.toLowerCase();
  const v = lista.filter(x => ((x.numeroFactura || '') + ' ' + (x.nombreCliente || '')).toLowerCase().includes(t));
  $('#filas').innerHTML = v.length ? v.map(x => `<tr><td><b>${esc(x.numeroFactura)}</b></td><td>${fechaHora(x.fechaEmision)}</td><td>${esc(x.nombreCliente)}</td><td>${esc(x.tipoMembresia)}</td><td>${dinero(x.montoOriginal)}</td><td>${dinero(x.montoDescuento)}</td><td><b>${dinero(x.total)}</b></td><td>${esc(x.metodoPago)}</td><td>${pill(x.estadoPago || 'PAGADO')}</td></tr>`).join('')
    : vacio(9, 'No hay facturas para mostrar.');
}
cargar();
