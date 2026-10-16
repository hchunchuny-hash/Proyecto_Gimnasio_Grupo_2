proteger('entrenadores', 'Entrenadores');
const dlg = $('#dlg'), f = $('#f');
let editando = null, lista = [];
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };
soloDigitos(f.telefono);

async function cargar() {
  try { lista = await api('/entrenadores'); pintar(); }
  catch (e) { toast(e.message, 'error'); }
}

function pintar() {
  const t = $('#buscar').value.toLowerCase();
  const v = lista.filter(x => (x.nombres + ' ' + x.apellidos + ' ' + (x.telefono || '') + ' ' + (x.especialidad || '')).toLowerCase().includes(t));
  $('#filas').innerHTML = v.length ? v.map(x =>
    `<tr><td><b>${esc(x.nombres)} ${esc(x.apellidos)}</b></td><td>${esc(x.especialidad)}</td><td>${esc(x.telefono)}</td><td>${x.usuarioId}</td><td>${pill(x.estado ? 'ACTIVA' : 'INACTIVA')}</td>
     <td class="acc"><button class="btn sec" onclick="abrir(${x.entrenadorId})">Editar</button></td></tr>`).join('')
    : '<tr><td colspan="6" class="vacio">No hay registros para mostrar.</td></tr>';
}

function abrir(id) {
  editando = id;
  const x = lista.find(e => e.entrenadorId === id) || {};
  $('#titulo').textContent = id ? 'Editar entrenador' : 'Nuevo entrenador';
  f.usuarioId.value = x.usuarioId || ''; f.usuarioId.disabled = !!id;
  f.nombres.value = x.nombres || ''; f.apellidos.value = x.apellidos || ''; f.telefono.value = x.telefono || '';
  f.especialidad.value = x.especialidad || '';
  dlg.showModal();
}

f.onsubmit = async e => {
  e.preventDefault();
  const d = { usuarioId: Number(f.usuarioId.value), nombres: f.nombres.value, apellidos: f.apellidos.value, telefono: f.telefono.value, especialidad: f.especialidad.value };
  try {
    await api(editando ? '/entrenadores/' + editando : '/entrenadores', editando ? 'PUT' : 'POST', d);
    dlg.close(); toast('Guardado correctamente'); cargar();
  } catch (err) { toast(err.message, 'error'); }
};
cargar();
