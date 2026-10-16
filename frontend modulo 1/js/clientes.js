proteger('clientes', 'Clientes');
const dlg = $('#dlg'), f = $('#f');
let editando = null, lista = [];
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };
soloDigitos(f.telefono); f.fechaNacimiento.max = ayerISO();

async function cargar() {
  try { lista = await api('/clientes'); pintar(); }
  catch (e) { toast(e.message, 'error'); }
}

function pintar() {
  const t = $('#buscar').value.toLowerCase();
  const v = lista.filter(x => (x.nombres + ' ' + x.apellidos + ' ' + (x.telefono || '') + ' ' + (x.especialidad || '')).toLowerCase().includes(t));
  $('#filas').innerHTML = v.length ? v.map(x =>
    `<tr><td><b>${esc(x.nombres)} ${esc(x.apellidos)}</b></td><td>${esc(x.telefono)}</td><td>${fecha(x.fechaNacimiento)}</td><td>${x.usuarioId}</td><td>${pill(x.estado ? 'ACTIVA' : 'INACTIVA')}</td>
     <td class="acc"><button class="btn sec solo-admin" onclick="abrir(${x.clienteId})">Editar</button></td></tr>`).join('')
    : '<tr><td colspan="6" class="vacio">No hay registros para mostrar.</td></tr>';
}

function abrir(id) {
  editando = id;
  const x = lista.find(e => e.clienteId === id) || {};
  $('#titulo').textContent = id ? 'Editar cliente' : 'Nuevo cliente';
  f.usuarioId.value = x.usuarioId || ''; f.usuarioId.disabled = !!id;
  f.nombres.value = x.nombres || ''; f.apellidos.value = x.apellidos || ''; f.telefono.value = x.telefono || '';
  f.fechaNacimiento.value = x.fechaNacimiento || '';
  dlg.showModal();
}

f.onsubmit = async e => {
  e.preventDefault();
  const d = { usuarioId: Number(f.usuarioId.value), nombres: f.nombres.value, apellidos: f.apellidos.value, telefono: f.telefono.value, fechaNacimiento: f.fechaNacimiento.value || null };
  try {
    await api(editando ? '/clientes/' + editando : '/clientes', editando ? 'PUT' : 'POST', d);
    dlg.close(); toast('Guardado correctamente'); cargar();
  } catch (err) { toast(err.message, 'error'); }
};
cargar();
