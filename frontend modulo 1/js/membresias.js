proteger('membresias', 'Membresías');
const dlg = $('#dlg'), f = $('#f');
let lista = [];
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function cargar() {
  try {
    let m, c, t;
    if (esCliente()) {
      const yo = await miCliente();
      [m, t] = await Promise.all([api('/membresias/cliente/' + yo.clienteId), api('/tipos-membresia')]); c = [yo]; lista = m;
    } else { [m, c, t] = await Promise.all([api('/membresias'), api('/clientes'), api('/tipos-membresia')]); lista = m; }
    const opc = c.map(x => `<option value="${x.clienteId}">${esc(x.nombres)} ${esc(x.apellidos)}</option>`).join('');
    f.clienteId.innerHTML = c.filter(x => x.estado).map(x => `<option value="${x.clienteId}">${esc(x.nombres)} ${esc(x.apellidos)}</option>`).join('');
    const sel = $('#cliente').value;
    $('#cliente').innerHTML = '<option value="">Todos los clientes</option>' + opc; $('#cliente').value = sel;
    f.tipoMembresiaId.innerHTML = t.map(x => `<option value="${x.tipoMembresiaId}">${esc(x.nombre)} - ${x.duracionMeses} mes(es) - ${Number(x.precio).toFixed(2)}</option>`).join('');
    pintar();
  } catch (e) { toast(e.message, 'error'); }
}

function pintar() {
  const c = Number($('#cliente').value), s = $('#estado').value;
  const v = lista.filter(m => (!c || m.clienteId === c) && (!s || m.estado === s));
  $('#filas').innerHTML = v.length ? v.map(m =>
    `<tr><td><b>${esc(m.nombreCliente)}</b></td><td>${esc(m.nombreTipoMembresia)}</td><td>${fecha(m.fechaInicio)}</td><td>${fecha(m.fechaVencimiento)}</td><td>${Number(m.precio).toFixed(2)}</td><td>${pill(m.estado)}</td></tr>`).join('')
    : '<tr><td colspan="6" class="vacio">No hay membresías para mostrar.</td></tr>';
}

function abrir() { f.fechaInicio.value = ''; dlg.showModal(); }

f.onsubmit = async e => {
  e.preventDefault();
  const d = { clienteId: Number(f.clienteId.value), tipoMembresiaId: Number(f.tipoMembresiaId.value), fechaInicio: f.fechaInicio.value || null };
  try { await api('/membresias/asignar', 'POST', d); dlg.close(); toast('Membresía asignada'); cargar(); }
  catch (err) { toast(err.message, 'error'); }
};

async function actualizarVencidas() {
  try { await api('/membresias/actualizar-vencidas', 'POST'); toast('Membresías vencidas actualizadas'); cargar(); }
  catch (err) { toast(err.message, 'error'); }
}
cargar();
