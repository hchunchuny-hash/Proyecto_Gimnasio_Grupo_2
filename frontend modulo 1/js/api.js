// Utilidades compartidas por todas las pantallas
// Mantiene el mismo hostname del frontend (localhost o 127.0.0.1) para que
// cookies SameSite funcionen también con puertos temporales del servidor estático.
const API = window.GYMADMIN_API || `${location.protocol}//${location.hostname}:8080/api`;
const $ = s => document.querySelector(s);
const esc = v => String(v ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
let csrfToken;

async function csrf() {
  if (csrfToken) return csrfToken;
  const r = await fetch(API + '/auth/csrf', { credentials: 'include' });
  if (!r.ok) throw new Error('No se pudo preparar la protección de la sesión.');
  csrfToken = (await r.json()).token;
  return csrfToken;
}

async function api(ruta, metodo = 'GET', cuerpo) {
  let r;
  try {
    const headers = {'Content-Type': 'application/json'};
    if (!['GET', 'HEAD', 'OPTIONS'].includes(metodo.toUpperCase())) headers['X-XSRF-TOKEN'] = await csrf();
    r = await fetch(API + ruta, { method: metodo, headers, credentials: 'include', body: cuerpo ? JSON.stringify(cuerpo) : undefined });
  } catch { throw new Error('No se pudo conectar con el backend. Verifica que esté activo en el puerto 8080 y que el origen del frontend esté permitido por CORS.'); }
  const t = await r.text(); let d = null;
  try { d = t ? JSON.parse(t) : null; } catch { d = t; }
  if (r.status === 401 && !['index.html', 'registro.html', 'recuperar.html'].includes(location.pathname.split('/').pop())) {
    sessionStorage.removeItem('usuario'); location.href = 'index.html';
  }
  if (!r.ok) throw new Error(mensajeError(d, r.status));
  return d;
}

const sesion = () => { try { return JSON.parse(sessionStorage.getItem('usuario') || 'null'); } catch { return null; } };

function toast(mensaje, tipo = 'ok') {
  let caja = $('#toasts');
  if (!caja) { caja = document.createElement('div'); caja.id = 'toasts'; document.body.append(caja); }
  const t = document.createElement('div');
  t.className = 'toast ' + tipo; t.textContent = mensaje; caja.append(t);
  setTimeout(() => t.remove(), 3800);
}

function pill(estado) {
  const t = estado.replace(/_/g, ' ').toLowerCase();
  return `<span class="pill ${esc(estado)}">${esc(t[0].toUpperCase() + t.slice(1))}</span>`;
}

const fecha = f => f ? String(f).split('T')[0].split('-').reverse().join('/') : '';

// El backend responde 400 con { mensaje } (regla de negocio) o { detalles: { campo: mensaje } } (validación)
function mensajeError(d, estado) {
  if (d && typeof d === 'object') {
    if (d.detalles && typeof d.detalles === 'object') return Object.values(d.detalles).join(' ');
    if (d.mensaje) return d.mensaje;
    if (d.message) return d.message;
  }
  return 'No se pudo completar la acción (error ' + estado + ').';
}

// Reglas de validación (las mismas del backend)
const CLAVE_OK = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&_#^()+=\-\[\]{}|;:',.<>\/~\\]).{8,16}$/;
const claveValida = c => CLAVE_OK.test(c || '');
const MSG_CLAVE = 'La contraseña debe tener entre 8 y 16 caracteres e incluir letras, números y al menos un carácter especial.';
const TEL_OK = /^$|^[0-9]{7,8}$/;
const MSG_TEL = 'El teléfono solo debe contener números (7 u 8 dígitos).';
const isoLocal = d => new Date(d.getTime() - d.getTimezoneOffset() * 60000).toISOString().slice(0, 10);
const hoyISO = () => isoLocal(new Date());
const ayerISO = () => isoLocal(new Date(Date.now() - 86400000));
const soloDigitos = input => { input.oninput = () => { input.value = input.value.replace(/\D/g, ''); }; };
