proteger('progreso', 'Progreso');
const dlg = $('#dlg'), f = $('#f');
const hoy = () => new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 10);
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function iniciar() {
  try {
    if (esCliente()) {
      const m = await miCliente();
      fijarCliente($('#cliente'), m); fijarCliente(f.clienteId, m); f.clienteId.closest('label').hidden = true;
      cargarProgreso(); return;
    }
    const c = await api('/clientes');
    const opc = c.map(x => `<option value="${x.clienteId}">${esc(x.nombres)} ${esc(x.apellidos)}</option>`).join('');
    $('#cliente').innerHTML = '<option value="">Elige un cliente...</option>' + opc;
    f.clienteId.innerHTML = opc;
  } catch (e) { toast(e.message, 'error'); }
}

async function cargarProgreso() {
  const id = $('#cliente').value;
  if (!id) { $('#filas').innerHTML = '<tr><td colspan="3" class="vacio">Elige un cliente para ver su progreso.</td></tr>'; return; }
  try {
    const r = await api('/progresos/cliente/' + id);
    $('#filas').innerHTML = r.length ? r.map(p => `<tr><td>${fecha(p.fecha)}</td><td><b>${Number(p.peso).toFixed(2)}</b></td><td>${esc(p.observaciones)}</td></tr>`).join('')
      : `<tr><td colspan="3" class="vacio">${esCliente() ? 'Aún no tienes mediciones.' : 'Este cliente aún no tiene mediciones.'}</td></tr>`;
  } catch (e) { toast(e.message, 'error'); }
}

function abrir() {
  f.reset(); f.fecha.value = hoy();
  if ($('#cliente').value) f.clienteId.value = $('#cliente').value;
  dlg.showModal();
}

f.onsubmit = async e => {
  e.preventDefault();
  const d = { clienteId: Number(f.clienteId.value), fecha: f.fecha.value, peso: Number(f.peso.value), observaciones: f.observaciones.value };
  try { await api('/progresos', 'POST', d); dlg.close(); toast('Medición registrada'); $('#cliente').value = d.clienteId; cargarProgreso(); }
  catch (err) { toast(err.message, 'error'); }
};
iniciar();
