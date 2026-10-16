proteger('inicio', 'Inicio');
$('#hola').textContent = 'Hola, ' + (sesion()?.nombreUsuario || '');
// El cliente ve su propio resumen, sin datos internos del gimnasio (como el estado del equipamiento)
async function inicioCliente() {
  const yo = await miCliente();
  const [a, c, mias] = await Promise.all([api('/areas'), api('/clases'), api('/membresias/cliente/' + yo.clienteId)]);
  const mem = mias.find(x => x.estado === 'ACTIVA') || mias[mias.length - 1];
  const datos = [['Áreas', a.filter(x => x.estado).length, '#3d5afe'], ['Clases disponibles', c.filter(x => x.estado).length, '#ff5a36'],
                 ['Mi membresía', mem ? mem.nombreTipoMembresia : 'Sin membresía', mem && mem.estado === 'ACTIVA' ? '#12b76a' : '#f79009']];
  if (mem) datos.push([mem.estado === 'ACTIVA' ? 'Vence el' : 'Venció el', fecha(mem.fechaVencimiento), '#f79009']);
  $('#cifras').innerHTML = datos.map(([t, v, col]) => `<div class="tarjeta" style="--c:${col}"><b>${esc(v)}</b><span>${t}</span></div>`).join('');
  $('.banner p').textContent = 'Este es tu resumen.';
  document.querySelectorAll('.panel').forEach(p => p.hidden = true);
}

(async () => {
  try {
    if (esCliente()) return await inicioCliente();
    const [a, q] = await Promise.all([api('/areas'), api('/equipamiento')]);
    const n = s => q.filter(x => x.estado === s).length;
    const datos = [['Áreas', a.length, '#3d5afe'], ['Equipos', q.length, '#ff5a36'], ['Operativos', n('OPERATIVO'), '#12b76a'],
                   ['En mantenimiento', n('MANTENIMIENTO'), '#f79009'], ['Fuera de servicio', n('FUERA_DE_SERVICIO'), '#f04438']];
    $('#cifras').innerHTML = datos.map(([t, v, c]) => `<div class="tarjeta" style="--c:${c}"><b>${v}</b><span>${t}</span></div>`).join('');
    const total = q.length || 1;
    $('#barra').innerHTML = [['OPERATIVO', '#12b76a'], ['MANTENIMIENTO', '#f79009'], ['FUERA_DE_SERVICIO', '#f04438']]
      .map(([s, c]) => `<i style="width:${n(s) / total * 100}%;background:${c}"></i>`).join('');
    const aten = q.filter(x => x.estado !== 'OPERATIVO').slice(0, 5);
    $('#atencion').innerHTML = aten.length
      ? aten.map(x => `<li><span><b>${esc(x.nombre)}</b><small>${esc(x.nombreArea)}</small></span>${pill(x.estado)}</li>`).join('')
      : '<li class="vacio">Todo el equipamiento está operativo.</li>';
  } catch (e) { toast(e.message, 'error'); }
})();
