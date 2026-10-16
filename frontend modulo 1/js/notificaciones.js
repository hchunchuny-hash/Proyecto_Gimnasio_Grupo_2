proteger('notificaciones', 'Notificaciones');
const dlg = $('#dlg'), f = $('#f');
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function iniciar() {
  try {
    if (esCliente()) { fijarCliente($('#cliente'), await miCliente()); cargar(); return; }
    const c = await api('/clientes');
    $('#cliente').innerHTML = '<option value="">Todos los clientes</option>' + opciones(c, 'clienteId', nombreCli);
    f.clienteId.innerHTML = opciones(c, 'clienteId', nombreCli);
    cargar();
  } catch (e) { toast(e.message, 'error'); }
}

async function cargar() {
  const c = $('#cliente').value;
  try {
    const r = await api('/notificaciones' + (c ? '?clienteId=' + c : ''));
    $('#filas').innerHTML = r.length ? r.map(n => `<tr><td>${fechaHora(n.fechaEnvio)}</td><td>${esc(n.nombreCliente)}</td><td><b>${esc(n.titulo)}</b><br><small>${esc(n.mensaje)}</small></td>
      <td>${n.leida ? '<span class="pill">Leída</span>' : '<span class="pill PENDIENTE">No leída</span>'}</td>
      <td class="acc">${n.leida ? '' : `<button class="btn sec" onclick="leida(${n.notificacionId})">Marcar leída</button>`}</td></tr>`).join('') : vacio(5, 'No hay notificaciones.');
  } catch (e) { toast(e.message, 'error'); }
}

function abrir() { f.reset(); if ($('#cliente').value) f.clienteId.value = $('#cliente').value; dlg.showModal(); }

f.onsubmit = async e => {
  e.preventDefault();
  try { await api('/notificaciones', 'POST', { clienteId: Number(f.clienteId.value), titulo: f.titulo.value, mensaje: f.mensaje.value }); dlg.close(); toast('Notificación enviada'); cargar(); }
  catch (err) { toast(err.message, 'error'); }
};

async function leida(id) {
  try { await api('/notificaciones/' + id + '/leida', 'PUT'); cargar(); } catch (err) { toast(err.message, 'error'); }
}
iniciar();
