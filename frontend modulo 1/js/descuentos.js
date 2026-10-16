proteger('descuentos', 'Descuentos');
const dlg = $('#dlg'), f = $('#f');
let editando = null, lista = [];
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function cargar() {
  try { lista = await api('/descuentos'); pintar(); } catch (e) { toast(e.message, 'error'); }
}

function pintar() {
  const t = $('#buscar').value.toLowerCase();
  const v = lista.filter(x => x.nombre.toLowerCase().includes(t));
  $('#filas').innerHTML = v.length ? v.map(x => `<tr><td><b>${esc(x.nombre)}</b></td><td>${x.porcentaje}%</td><td>${fecha(x.fechaInicio)}</td><td>${x.fechaFin ? fecha(x.fechaFin) : "-"}</td><td>${pill(x.estado ? "ACTIVA" : "INACTIVA")}</td><td class="acc"><button class="btn sec" onclick="abrir(${x.descuentoId})">Editar</button></td></tr>`).join('') : vacio(6, 'No hay registros para mostrar.');
}

function abrir(id) {
  editando = id;
  const x = lista.find(e => e.descuentoId === id) || { estado: true };
  f.nombre.value = x.nombre || ''; f.porcentaje.value = x.porcentaje ?? ''; f.fechaInicio.value = x.fechaInicio || ''; f.fechaFin.value = x.fechaFin || ''; f.estado.checked = x.estado;
  dlg.showModal();
}

f.onsubmit = async e => {
  e.preventDefault();
  const d = { nombre: f.nombre.value, porcentaje: Number(f.porcentaje.value), fechaInicio: f.fechaInicio.value, fechaFin: f.fechaFin.value || null, estado: f.estado.checked };
  try {
    await api(editando ? '/descuentos/' + editando : '/descuentos', editando ? 'PUT' : 'POST', d);
    dlg.close(); toast('Guardado correctamente'); cargar();
  } catch (err) { toast(err.message, 'error'); }
};
cargar();
