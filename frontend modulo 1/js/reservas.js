proteger('reservas', 'Reservas');
const dlg = $('#dlg'), f = $('#f');
let lista = [];
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function cargar() {
  try {
    const yo = esCliente() ? await miCliente() : null;
    const [r, c, s] = await Promise.all([yo ? api('/reservas/cliente/' + yo.clienteId) : api('/reservas'), yo ? [] : api('/clientes'), api('/servicios')]);
    if (yo) { lista = r.filter(x => esMio(x, yo)); fijarCliente(f.clienteId, yo); f.clienteId.closest('label').hidden = true; }
    else { lista = r; f.clienteId.innerHTML = opciones(c, 'clienteId', nombreCli); }
    f.servicioId.innerHTML = opciones(s.filter(x => x.estado), 'servicioId', x => x.nombre);
    pintar();
  } catch (e) { toast(e.message, 'error'); }
}

function acciones(x) {
  if (esCliente()) return '';
  const b = (est, t, cl) => `<button class="btn ${cl}" onclick="cambiar(${x.reservaId}, '${est}')">${t}</button>`;
  if (x.estado === 'PENDIENTE') return b('CONFIRMADA', 'Confirmar', 'sec') + ' ' + b('CANCELADA', 'Cancelar', 'del');
  if (x.estado === 'CONFIRMADA') return b('FINALIZADA', 'Finalizar', 'sec') + ' ' + b('CANCELADA', 'Cancelar', 'del');
  return '';
}

function pintar() {
  const s = $('#estado').value;
  const v = lista.filter(x => !s || x.estado === s);
  $('#filas').innerHTML = v.length ? v.map(x => `<tr><td><b>${fecha(x.fecha)}</b></td><td>${hora(x.horaInicio)}${x.horaFin ? ' a ' + hora(x.horaFin) : ''}</td><td>${esc(x.nombreCliente)}</td><td>${esc(x.nombreServicio)}<br><small>${esc(x.observaciones)}</small></td><td>${pill(x.estado)}</td><td class="acc">${acciones(x)}</td></tr>`).join('')
    : vacio(6, 'No hay reservas para mostrar.');
}

function abrir() { f.reset(); dlg.showModal(); }

f.onsubmit = async e => {
  e.preventDefault();
  const d = { clienteId: Number(f.clienteId.value), servicioId: Number(f.servicioId.value), fecha: f.fecha.value, horaInicio: f.horaInicio.value, horaFin: f.horaFin.value || null, observaciones: f.observaciones.value || null };
  try { await api('/reservas', 'POST', d); dlg.close(); toast('Reserva creada'); cargar(); } catch (err) { toast(err.message, 'error'); }
};

async function cambiar(id, estado) {
  try { await api('/reservas/' + id + '/estado', 'PUT', { estado }); toast('Reserva ' + estado.toLowerCase()); cargar(); } catch (err) { toast(err.message, 'error'); }
}
cargar();
