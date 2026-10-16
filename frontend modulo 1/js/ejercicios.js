proteger('ejercicios', 'Ejercicios');
const dlg = $('#dlg'), f = $('#f');
let lista = [], equipos = {};
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function cargar() {
  try {
    const [e, q] = await Promise.all([api('/ejercicios'), api('/equipamiento')]);
    lista = e; equipos = Object.fromEntries(q.map(x => [x.equipamientoId, x.nombre]));
    f.equipamientoId.innerHTML = '<option value="">Sin equipamiento</option>' + q.map(x => `<option value="${x.equipamientoId}">${esc(x.nombre)}</option>`).join('');
    pintar();
  } catch (err) { toast(err.message, 'error'); }
}

function pintar() {
  const t = $('#buscar').value.toLowerCase();
  const v = lista.filter(x => (x.nombre + ' ' + (x.descripcion || '')).toLowerCase().includes(t));
  $('#filas').innerHTML = v.length ? v.map(x =>
    `<tr><td><b>${esc(x.nombre)}</b></td><td>${esc(x.descripcion)}</td><td>${x.grupoMuscularId}</td><td>${x.nivelDificultadId}</td><td>${esc(equipos[x.equipamientoId] || '-')}</td><td>${pill(x.estado ? 'ACTIVA' : 'INACTIVA')}</td></tr>`).join('')
    : '<tr><td colspan="6" class="vacio">No hay ejercicios para mostrar.</td></tr>';
}

function abrir() { f.reset(); dlg.showModal(); }

f.onsubmit = async e => {
  e.preventDefault();
  const d = { nombre: f.nombre.value, descripcion: f.descripcion.value, grupoMuscularId: Number(f.grupoMuscularId.value),
              nivelDificultadId: Number(f.nivelDificultadId.value), equipamientoId: f.equipamientoId.value ? Number(f.equipamientoId.value) : null };
  try { await api('/ejercicios', 'POST', d); dlg.close(); toast('Ejercicio guardado'); cargar(); }
  catch (err) { toast(err.message, 'error'); }
};
cargar();
