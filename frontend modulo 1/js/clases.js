proteger('clases', 'Clases');
const dlg = $('#dlg'), f = $('#f'), dlgI = $('#dlgI'), fI = $('#fI');
let lista = [];
[dlg, dlgI].forEach(d => d.onclick = e => { if (e.target === d) d.close(); });

async function cargar() {
  try {
    const [c, s, cl] = await Promise.all([api('/clases'), api('/servicios'), esCliente() ? [] : api('/clientes')]);
    lista = c;
    f.servicioId.innerHTML = opciones(s.filter(x => x.estado), 'servicioId', x => x.nombre);
    fI.clienteId.innerHTML = opciones(cl, 'clienteId', nombreCli);
    pintar();
  } catch (e) { toast(e.message, 'error'); }
}

function pintar() {
  const t = $('#buscar').value.toLowerCase();
  const v = lista.filter(x => (x.nombre + ' ' + (x.nombreServicio || '')).toLowerCase().includes(t));
  $('#filas').innerHTML = v.length ? v.map(x =>
    `<tr><td><b>${esc(x.nombre)}</b><br><small>${esc(x.descripcion)}</small></td><td>${esc(x.nombreServicio)}</td><td>${x.inscritos} / ${x.cupoMaximo}</td><td>${pill(x.estado ? 'ACTIVA' : 'INACTIVA')}</td>
     <td class="acc"><button class="btn sec solo-staff" onclick="inscribir(${x.claseId})">Inscribir cliente</button>${x.estado && x.inscritos < x.cupoMaximo ? `<button class="btn sec solo-cli" onclick="inscribirme(${x.claseId})">Inscribirme</button>` : '<small class="solo-cli">Sin cupo</small>'}</td></tr>`).join('') : vacio(5, 'No hay clases para mostrar.');
}

function abrir() { f.reset(); dlg.showModal(); }
async function inscribirme(id) {
  if (!confirm('¿Inscribirte en esta clase?')) return;
  try { const yo = await miCliente(); await api('/clases/inscripciones', 'POST', { claseId: id, clienteId: yo.clienteId }); toast('Quedaste inscrito'); cargar(); }
  catch (err) { toast(err.message, 'error'); }
}
function inscribir(id) { fI.claseId.value = id; dlgI.showModal(); }

f.onsubmit = async e => {
  e.preventDefault();
  const d = { servicioId: Number(f.servicioId.value), nombre: f.nombre.value, descripcion: f.descripcion.value, cupoMaximo: Number(f.cupoMaximo.value), estado: true };
  try { await api('/clases', 'POST', d); dlg.close(); toast('Clase creada'); cargar(); } catch (err) { toast(err.message, 'error'); }
};

fI.onsubmit = async e => {
  e.preventDefault();
  try { await api('/clases/inscripciones', 'POST', { claseId: Number(fI.claseId.value), clienteId: Number(fI.clienteId.value) }); dlgI.close(); toast('Cliente inscrito'); cargar(); }
  catch (err) { toast(err.message, 'error'); }
};
cargar();
