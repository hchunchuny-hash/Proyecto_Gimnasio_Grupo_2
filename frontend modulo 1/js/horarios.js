proteger('horarios', 'Horarios');
const dlg = $('#dlg'), f = $('#f');
let lista = [];
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function cargar() {
  try {
    const [h, c, en] = await Promise.all([api('/clases/horarios'), api('/clases'), esCliente() ? [] : api('/entrenadores')]);
    lista = h;
    f.claseId.innerHTML = opciones(c.filter(x => x.estado), 'claseId', x => x.nombre);
    const sel = $('#clase').value;
    $('#clase').innerHTML = '<option value="">Todas las clases</option>' + opciones(c, 'claseId', x => x.nombre); $('#clase').value = sel;
    if (!esCliente()) f.entrenadorId.innerHTML = opciones(en, 'entrenadorId', nombreCli);
    pintar();
  } catch (e) { toast(e.message, 'error'); }
}

function pintar() {
  const c = Number($('#clase').value);
  const v = lista.filter(x => !c || x.claseId === c);
  $('#filas').innerHTML = v.length ? v.map(x => `<tr><td><b>${fecha(x.fecha)}</b></td><td>${hora(x.horaInicio)} a ${hora(x.horaFin)}</td><td>${esc(x.nombreClase)}</td><td>${esc(x.nombreEntrenador)}</td></tr>`).join('')
    : vacio(4, 'No hay horarios próximos.');
}

function abrir() { f.reset(); dlg.showModal(); }

f.onsubmit = async e => {
  e.preventDefault();
  const d = { claseId: Number(f.claseId.value), entrenadorId: Number(f.entrenadorId.value), fecha: f.fecha.value, horaInicio: f.horaInicio.value, horaFin: f.horaFin.value };
  try { await api('/clases/horarios', 'POST', d); dlg.close(); toast('Horario creado'); cargar(); } catch (err) { toast(err.message, 'error'); }
};
cargar();
