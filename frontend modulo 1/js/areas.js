proteger('areas', 'Áreas');
const dlg = $('#dlg'), f = $('#f');
let editando = null, lista = [];
const idDe = a => a.areaGimnasioId ?? a.areaId;
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function cargar() {
  try { lista = await api('/areas'); pintar(); }
  catch (e) { toast(e.message, 'error'); }
}

function pintar() {
  const t = $('#buscar').value.toLowerCase();
  const v = lista.filter(a => (a.nombre + ' ' + (a.descripcion || '')).toLowerCase().includes(t));
  $('#filas').innerHTML = v.length ? v.map(a =>
    `<tr><td><b>${esc(a.nombre)}</b></td><td>${esc(a.descripcion)}</td><td>${pill(a.estado ? 'ACTIVA' : 'INACTIVA')}</td>
     <td class="acc"><button class="btn sec solo-admin" onclick="abrir(${idDe(a)})">Editar</button></td></tr>`).join('')
    : '<tr><td colspan="4" class="vacio">No hay áreas para mostrar.</td></tr>';
}

function abrir(id) {
  editando = id;
  const a = lista.find(x => idDe(x) === id) || { nombre: '', descripcion: '', estado: true };
  $('#titulo').textContent = id ? 'Editar área' : 'Nueva área';
  f.nombre.value = a.nombre; f.descripcion.value = a.descripcion || ''; f.estado.checked = a.estado;
  dlg.showModal();
}

f.onsubmit = async e => {
  e.preventDefault();
  const d = { nombre: f.nombre.value, descripcion: f.descripcion.value, estado: f.estado.checked };
  try {
    await api(editando ? '/areas/' + editando : '/areas', editando ? 'PUT' : 'POST', d);
    dlg.close(); toast('Área guardada'); cargar();
  } catch (err) { toast(err.message, 'error'); }
};
cargar();
