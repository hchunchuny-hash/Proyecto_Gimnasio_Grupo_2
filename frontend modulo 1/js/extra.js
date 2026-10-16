// Ayudas extra para los módulos 4 y 5
const hora = h => h ? String(h).slice(0, 5) : '';
const fechaHora = f => f ? fecha(f) + ' ' + (String(f).split('T')[1] || '').slice(0, 5) : '';
const dinero = n => n == null ? '' : Number(n).toFixed(2);
const opciones = (lista, valor, texto) => lista.map(x => `<option value="${x[valor]}">${esc(texto(x))}</option>`).join('');
const nombreCli = c => c.nombres + ' ' + c.apellidos;
const vacio = (n, t) => `<tr><td colspan="${n}" class="vacio">${t}</td></tr>`;

// Datos del cliente que inició sesión (se busca por su usuarioId en la lista de clientes)
const esCliente = () => String(sesion()?.rol || '').toUpperCase() === 'CLIENTE';
let _miClienteId = null;
async function miClienteId() {
  if (_miClienteId !== null) return _miClienteId;
  const m = await miCliente();
  _miClienteId = m.clienteId;
  if (!_miClienteId) toast('Tu cuenta no tiene perfil de cliente. Avisa al administrador.', 'error');
  return _miClienteId;
}

// Registro completo del cliente que inició sesión (lo usan las pantallas "Mis ...")
let _miCliente = null;
async function miCliente() {
  if (_miCliente) return _miCliente;
  if (esCliente()) return (_miCliente = await api('/mi-cuenta/perfil'));
  const m = (await api('/clientes')).find(x => x.usuarioId === sesion().usuarioId);
  if (!m) throw new Error('Tu cuenta no tiene perfil de cliente. Avisa al administrador.');
  return (_miCliente = m);
}

// Deja un selector con una sola opción: el propio cliente
function fijarCliente(select, c) {
  select.innerHTML = `<option value="${c.clienteId}">${esc(c.nombres)} ${esc(c.apellidos)}</option>`;
  select.value = c.clienteId;
}

// ¿Este registro (reserva, pago, factura...) es del cliente?
const esMio = (x, c) => x.clienteId === c.clienteId;
