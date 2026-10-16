proteger('rutinas', 'Rutinas');
const dlg = $('#dlg'), f = $('#f');
const DIAS = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];
const hoy = () => new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 10);
let ejercicios = [];
dlg.onclick = e => { if (e.target === dlg) dlg.close(); };

async function iniciar() {
  try {
    if (esCliente()) { fijarCliente($('#cliente'), await miCliente()); cargarRutinas(); return; }
    const [c, en, ej] = await Promise.all([api('/clientes'), api('/entrenadores'), api('/ejercicios')]);
    ejercicios = ej;
    const oc = c.map(x => `<option value="${x.clienteId}">${esc(x.nombres)} ${esc(x.apellidos)}</option>`).join('');
    $('#cliente').innerHTML = '<option value="">Elige un cliente...</option>' + oc;
    f.clienteId.innerHTML = oc;
    f.entrenadorId.innerHTML = en.map(x => `<option value="${x.entrenadorId}">${esc(x.nombres)} ${esc(x.apellidos)}</option>`).join('');
  } catch (e) { toast(e.message, 'error'); }
}

const tarjeta = r => `<section class="panel"><h3>${esc(r.nombre)} ${pill(r.estado ? 'ACTIVA' : 'INACTIVA')}</h3>
  <p class="sub">Objetivo: ${esc(r.objetivo)}<br>Entrenador: ${esc(r.nombreEntrenador)}<br>Del ${fecha(r.fechaInicio)} al ${r.fechaFin ? fecha(r.fechaFin) : 'sin fecha de fin'}</p>
  ${[...(r.dias || [])].sort((a, b) => a.ordenDia - b.ordenDia).map(d => `<details><summary><b>${esc(d.diaSemana)}</b> (${(d.ejercicios || []).length} ejercicios)</summary>
    <ul class="lista">${(d.ejercicios || []).map(e => `<li><span><b>${esc(e.nombreEjercicio)}</b><small>${esc(e.observaciones)}</small></span><span>${e.series} x ${e.repeticiones}, descanso ${e.descansoSegundos} s</span></li>`).join('')}</ul></details>`).join('')}</section>`;

async function cargarRutinas() {
  const id = $('#cliente').value;
  if (!id) { $('#lista').innerHTML = '<p class="vacio">Elige un cliente para ver sus rutinas.</p>'; return; }
  try {
    const r = await api('/rutinas/cliente/' + id);
    $('#lista').innerHTML = r.length ? r.map(tarjeta).join('') : `<p class="vacio">${esCliente() ? 'Aún no tienes rutinas activas.' : 'Este cliente no tiene rutinas activas.'}</p>`;
  } catch (e) { toast(e.message, 'error'); }
}

function agregarDia() {
  const d = document.createElement('div'); d.className = 'dia';
  d.innerHTML = `<div class="dia-cab"><select class="d-nombre">${DIAS.map(x => `<option>${x}</option>`).join('')}</select>
    <button type="button" class="btn sec" onclick="agregarEj(this.closest('.dia'))">+ Ejercicio</button>
    <button type="button" class="btn del" onclick="this.closest('.dia').remove()">Quitar día</button></div><div class="ejs"></div>`;
  $('#dias').append(d); agregarEj(d);
}

function agregarEj(dia) {
  const r = document.createElement('div'); r.className = 'ej-fila';
  r.innerHTML = `<select class="e-id">${ejercicios.map(x => `<option value="${x.ejercicioId}">${esc(x.nombre)}</option>`).join('')}</select>
    <input class="e-ser" type="number" min="1" value="3" title="Series"><input class="e-rep" type="number" min="1" value="10" title="Repeticiones">
    <input class="e-des" type="number" min="0" value="60" title="Descanso en segundos"><button type="button" class="btn del" onclick="this.parentElement.remove()">x</button>`;
  dia.querySelector('.ejs').append(r);
}

function abrir() {
  if (!ejercicios.length) { toast('Primero crea al menos un ejercicio.', 'error'); return; }
  if (!f.clienteId.options.length || !f.entrenadorId.options.length) { toast('Se necesita al menos un cliente y un entrenador.', 'error'); return; }
  f.reset(); $('#dias').innerHTML = ''; f.fechaInicio.value = hoy();
  if ($('#cliente').value) f.clienteId.value = $('#cliente').value;
  agregarDia(); dlg.showModal();
}

f.onsubmit = async e => {
  e.preventDefault();
  const dias = [...document.querySelectorAll('#dias .dia')].map((d, i) => ({
    diaSemana: d.querySelector('.d-nombre').value, ordenDia: i + 1,
    ejercicios: [...d.querySelectorAll('.ej-fila')].map(r => ({ ejercicioId: Number(r.querySelector('.e-id').value), series: Number(r.querySelector('.e-ser').value),
      repeticiones: Number(r.querySelector('.e-rep').value), descansoSegundos: Number(r.querySelector('.e-des').value) })) }));
  if (!dias.length) { toast('Agrega al menos un día.', 'error'); return; }
  const d = { clienteId: Number(f.clienteId.value), entrenadorId: Number(f.entrenadorId.value), nombre: f.nombre.value, objetivo: f.objetivo.value,
              fechaInicio: f.fechaInicio.value || hoy(), fechaFin: f.fechaFin.value || null, dias };
  try { await api('/rutinas', 'POST', d); dlg.close(); toast('Rutina creada'); $('#cliente').value = d.clienteId; cargarRutinas(); }
  catch (err) { toast(err.message, 'error'); }
};
iniciar();
