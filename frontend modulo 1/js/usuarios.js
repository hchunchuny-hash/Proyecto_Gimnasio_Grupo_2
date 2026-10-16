proteger('usuarios', 'Usuarios y roles');
const ROLES = ['ADMINISTRADOR', 'EMPLEADO', 'RECEPCION', 'ENTRENADOR', 'CLIENTE'];
const fechaAuditoria = v => v ? new Date(v).toLocaleString() : '';

async function cargarUsuarios(q = '') {
  const filas = await api('/usuarios' + (q ? '?q=' + encodeURIComponent(q) : ''));
  $('#usuarios').innerHTML = filas.length ? filas.map(u => {
    const propio = u.usuarioId === sesion().usuarioId;
    return `<tr><td>${u.usuarioId}</td><td>${esc(u.nombreUsuario)}</td><td>${esc(u.correo)}</td>
      <td><select aria-label="Rol para ${esc(u.nombreUsuario)}" id="rol-${u.usuarioId}" ${propio ? 'disabled' : ''}>${ROLES.map(r => `<option ${r === u.rol ? 'selected' : ''}>${r}</option>`).join('')}</select></td>
      <td>${u.estado ? 'Activo' : 'Inactivo'}</td><td class="acc"><button class="btn sec" data-role="${u.usuarioId}" ${propio ? 'disabled' : ''}>Guardar rol</button>
      <button class="btn sec" data-state="${u.usuarioId}" ${propio ? 'disabled' : ''}>${u.estado ? 'Desactivar' : 'Activar'}</button></td></tr>`;
  }).join('') : '<tr><td colspan="6" class="vacio">No se encontraron usuarios.</td></tr>';
}

async function cargarAuditoria() {
  const rows = await api('/usuarios/auditoria-roles');
  $('#auditoria').innerHTML = rows.length ? rows.map(x => `<tr><td>${esc(fechaAuditoria(x.fecha))}</td><td>${x.administradorId}</td><td>${x.usuarioAfectadoId}</td><td>${esc(x.rolAnterior)}</td><td>${esc(x.rolNuevo)}</td><td>${esc(x.resultado)}</td></tr>`).join('') : '<tr><td colspan="6" class="vacio">Todavía no hay cambios registrados.</td></tr>';
}

$('#buscar').onsubmit = async e => { e.preventDefault(); try { await cargarUsuarios($('#q').value.trim()); } catch (err) { toast(err.message, 'error'); } };
$('#usuarios').onclick = async e => {
  const roleButton = e.target.closest('[data-role]'), stateButton = e.target.closest('[data-state]');
  try {
    if (roleButton) {
      const id = Number(roleButton.dataset.role), rol = $('#rol-' + id).value;
      if (!confirm(`¿Cambiar el rol de este usuario a ${rol}? Sus sesiones actuales se invalidarán.`)) return;
      await api('/usuarios/' + id + '/rol', 'PUT', { rol }); toast('Rol actualizado.');
      await cargarUsuarios($('#q').value.trim()); await cargarAuditoria();
    } else if (stateButton) {
      const id = Number(stateButton.dataset.state), activo = stateButton.textContent.trim() === 'Activar';
      if (!confirm(`¿${activo ? 'Activar' : 'Desactivar'} esta cuenta?`)) return;
      await api('/usuarios/' + id + '/estado', 'PUT', { estado: activo }); toast('Estado actualizado.');
      await cargarUsuarios($('#q').value.trim()); await cargarAuditoria();
    }
  } catch (err) { toast(err.message, 'error'); }
};

Promise.all([cargarUsuarios(), cargarAuditoria()]).catch(err => toast(err.message, 'error'));
