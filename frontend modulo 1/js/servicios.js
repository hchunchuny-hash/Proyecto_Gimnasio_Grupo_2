proteger('servicios', 'Servicios');
const dlg = $('#dlg'), f = $('#f');
let editando = null, lista = [];
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function cargar() {
  try { lista = await api('/servicios'); pintar(); } catch (e) { toast(e.message, 'error'); }
}

function pintar() {
  const t = $('#buscar').value.toLowerCase();
  const v = lista.filter(x => x.nombre.toLowerCase().includes(t));
  $('#filas').innerHTML = v.length ? v.map(x => `<tr><td><b>${esc(x.nombre)}</b></td><td>${esc(x.descripcion)}</td><td>${dinero(x.precio)}</td><td>${pill(x.estado ? "ACTIVA" : "INACTIVA")}</td><td class="acc"><button class="btn sec solo-admin" onclick="abrir(${x.servicioId})">Editar</button></td></tr>`).join('') : vacio(6, 'No hay registros para mostrar.');
}

function abrir(id) {
  editando = id;
  const x = lista.find(e => e.servicioId === id) || { estado: true };
  f.nombre.value = x.nombre || ''; f.descripcion.value = x.descripcion || ''; f.precio.value = x.precio ?? ''; f.estado.checked = x.estado;
  dlg.showModal();
}

f.onsubmit = async e => {
  e.preventDefault();
  const d = { nombre: f.nombre.value, descripcion: f.descripcion.value, precio: Number(f.precio.value), estado: f.estado.checked };
  try {
    await api(editando ? '/servicios/' + editando : '/servicios', editando ? 'PUT' : 'POST', d);
    dlg.close(); toast('Guardado correctamente'); cargar();
  } catch (err) { toast(err.message, 'error'); }
};
cargar();
