proteger('equipamiento', 'Equipamiento');
const dlg = $('#dlg'), f = $('#f');
let editando = null, lista = [];
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };
f.fechaAdquisicion.max = hoyISO();

async function cargar() {
  try {
    const [q, a] = await Promise.all([api('/equipamiento'), api('/areas')]);
    lista = q;
    f.areaGimnasioId.innerHTML = a.map(x => `<option value="${x.areaGimnasioId ?? x.areaId}">${esc(x.nombre)}</option>`).join('');
    pintar();
  } catch (e) { toast(e.message, 'error'); }
}

function pintar() {
  const t = $('#buscar').value.toLowerCase(), s = $('#estado').value;
  const v = lista.filter(x => (!s || x.estado === s) && (x.nombre + (x.numeroSerie || '') + (x.nombreArea || '')).toLowerCase().includes(t));
  $('#filas').innerHTML = v.length ? v.map(x =>
    `<tr><td><b>${esc(x.nombre)}</b></td><td>${esc(x.numeroSerie)}</td><td>${fecha(x.fechaAdquisicion)}</td><td>${esc(x.nombreArea)}</td><td>${pill(x.estado)}</td>
     <td class="acc"><button class="btn sec solo-admin" onclick="abrir(${x.equipamientoId})">Editar</button> <button class="btn del solo-admin" onclick="eliminar(${x.equipamientoId})">Eliminar</button></td></tr>`).join('')
    : '<tr><td colspan="6" class="vacio">No hay equipos que coincidan.</td></tr>';
}

function abrir(id) {
  editando = id;
  const x = lista.find(e => e.equipamientoId === id) || { nombre: '', numeroSerie: '', fechaAdquisicion: '', estado: 'OPERATIVO' };
  $('#titulo').textContent = id ? 'Editar equipo' : 'Nuevo equipo';
  f.nombre.value = x.nombre; f.numeroSerie.value = x.numeroSerie || ''; f.fechaAdquisicion.value = x.fechaAdquisicion || '';
  f.estado.value = x.estado; if (x.areaGimnasioId) f.areaGimnasioId.value = x.areaGimnasioId;
  dlg.showModal();
}

f.onsubmit = async e => {
  e.preventDefault();
  const d = { nombre: f.nombre.value, numeroSerie: f.numeroSerie.value, fechaAdquisicion: f.fechaAdquisicion.value || null, estado: f.estado.value, areaGimnasioId: Number(f.areaGimnasioId.value) };
  try {
    await api(editando ? '/equipamiento/' + editando : '/equipamiento', editando ? 'PUT' : 'POST', d);
    dlg.close(); toast('Equipo guardado'); cargar();
  } catch (err) { toast(err.message, 'error'); }
};

async function eliminar(id) {
  if (!confirm('¿Eliminar este equipo? No se puede deshacer.')) return;
  try { await api('/equipamiento/' + id, 'DELETE'); toast('Equipo eliminado'); cargar(); }
  catch (err) { toast(err.message, 'error'); }
}
cargar();
