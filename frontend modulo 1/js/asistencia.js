proteger('asistencia', 'Asistencia');
async function iniciar() {
  try {
    if (esCliente()) { fijarCliente($('#cliente'), await miCliente()); return; }
    const c = await api('/clientes');
    $('#cliente').innerHTML = '<option value="">Elige un cliente...</option>' + opciones(c, 'clienteId', nombreCli);
  } catch (e) { toast(e.message, 'error'); }
}

async function cargarAsistencias() {
  const id = $('#cliente').value;
  if (!id) { $('#filas').innerHTML = vacio(5, 'Elige un cliente para ver su asistencia.'); return; }
  try {
    const r = await api('/asistencia/cliente/' + id);
    $('#filas').innerHTML = r.length ? r.map(a => `<tr><td><b>${fecha(a.fecha)}</b></td><td>${hora(a.horaEntrada)}</td><td>${hora(a.horaSalida) || '-'}</td><td>${pill(a.estado)}</td>
      <td class="acc">${!esCliente() && !a.horaSalida && a.estado === 'VALIDADA' ? `<button class="btn sec" onclick="salida(${a.asistenciaId})">Marcar salida</button>` : ''}</td></tr>`).join('')
      : vacio(5, esCliente() ? 'Aún no tienes asistencias.' : 'Este cliente aún no tiene asistencias.');
  } catch (e) { toast(e.message, 'error'); }
}

async function marcar() {
  const id = $('#cliente').value;
  if (!id) { toast('Elige un cliente primero.', 'error'); return; }
  try {
    const r = await api('/asistencia/marcar', 'POST', { clienteId: Number(id) });
    toast(r.mensaje || 'Entrada registrada', r.estado === 'RECHAZADA' ? 'error' : 'ok'); cargarAsistencias();
  } catch (err) { toast(err.message, 'error'); }
}

async function salida(id) {
  try { await api('/asistencia/' + id + '/salida', 'PUT'); toast('Salida registrada'); cargarAsistencias(); } catch (err) { toast(err.message, 'error'); }
}
iniciar().then(cargarAsistencias);
