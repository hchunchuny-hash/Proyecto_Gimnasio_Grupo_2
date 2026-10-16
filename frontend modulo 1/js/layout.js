// Menú lateral y barra superior, con permisos por rol.
// Para cambiar quién ve qué, solo se edita la lista "roles" de cada pantalla.
const ADMIN = 'ADMINISTRADOR', ENTR = 'ENTRENADOR', CLI = 'CLIENTE', STAFF = ['ADMINISTRADOR', 'ADMIN', 'EMPLEADO', 'RECEPCION', 'ENTRENADOR'];

const MODULOS = [
  { titulo: 'General', items: [{ id: 'inicio', texto: 'Inicio', href: 'inicio.html', roles: STAFF.concat(CLI) }] },
  { titulo: 'Administración', items: [{ id: 'usuarios', texto: 'Usuarios y roles', href: 'usuarios.html', roles: [ADMIN] }] },
  { titulo: 'Módulo 1: Administración', tituloCli: 'Gimnasio', items: [
    { id: 'areas', texto: 'Áreas', href: 'areas.html', roles: STAFF.concat(CLI) },
    { id: 'equipamiento', texto: 'Equipamiento', href: 'equipamiento.html', roles: STAFF }] },
  { titulo: 'Módulo 2: Clientes y membresías', tituloCli: 'Mi cuenta', items: [
    { id: 'clientes', texto: 'Clientes', href: 'clientes.html', roles: STAFF },
    { id: 'entrenadores', texto: 'Entrenadores', href: 'entrenadores.html', roles: [ADMIN] },
    { id: 'membresias', texto: 'Membresías', href: 'membresias.html', roles: STAFF.concat(CLI), textoCli: 'Mi membresía' }] },
  { titulo: 'Módulo 3: Entrenamiento', tituloCli: 'Entrenamiento', items: [
    { id: 'ejercicios', texto: 'Ejercicios', href: 'ejercicios.html', roles: STAFF.concat(CLI) },
    { id: 'rutinas', texto: 'Rutinas', href: 'rutinas.html', roles: STAFF.concat(CLI), textoCli: 'Mis rutinas' },
    { id: 'progreso', texto: 'Progreso', href: 'progreso.html', roles: STAFF.concat(CLI), textoCli: 'Mi progreso' }] },
  { titulo: 'Módulo 4: Clases y asistencia', tituloCli: 'Clases y asistencia', items: [
    { id: 'clases', texto: 'Clases', href: 'clases.html', roles: STAFF.concat(CLI) },
    { id: 'horarios', texto: 'Horarios', href: 'horarios.html', roles: STAFF.concat(CLI) },
    { id: 'reservas', texto: 'Reservas', href: 'reservas.html', roles: STAFF.concat(CLI), textoCli: 'Mis reservas' },
    { id: 'asistencia', texto: 'Asistencia', href: 'asistencia.html', roles: STAFF.concat(CLI), textoCli: 'Mi asistencia' }] },
  { titulo: 'Módulo 5: Pagos y servicios', tituloCli: 'Pagos y servicios', items: [
    { id: 'servicios', texto: 'Servicios', href: 'servicios.html', roles: STAFF.concat(CLI) },
    { id: 'descuentos', texto: 'Descuentos', href: 'descuentos.html', roles: [ADMIN] },
    { id: 'pagos', texto: 'Pagos', href: 'pagos.html', roles: STAFF.concat(CLI), textoCli: 'Mis pagos' },
    { id: 'facturas', texto: 'Facturas', href: 'facturas.html', roles: STAFF.concat(CLI), textoCli: 'Mis facturas' },
    { id: 'notificaciones', texto: 'Notificaciones', href: 'notificaciones.html', roles: STAFF.concat(CLI), textoCli: 'Mis notificaciones' }] },
];

function proteger(activa, titulo) {
  const u = sesion();
  if (!u) { location.href = 'index.html'; return; }
  const rolOriginal = String(u.rol || '').toUpperCase();
  const rol = rolOriginal === 'ADMIN' ? ADMIN : rolOriginal;
  document.body.dataset.rol = rol.toLowerCase();
  const pantalla = MODULOS.flatMap(g => g.items).find(i => i.id === activa);
  if (pantalla && activa !== 'inicio' && !pantalla.roles.includes(rol)) { location.href = 'inicio.html'; return; }
  const cli = rol === CLI;
  if (cli && pantalla && pantalla.textoCli) titulo = pantalla.textoCli;
  const nombre = u.nombreUsuario || 'Usuario';
  const grupos = MODULOS.map(g => ({ ...g, items: g.items.filter(i => i.id === 'inicio' || i.roles.includes(rol)) })).filter(g => g.items.length);
  $('#menu').innerHTML = '<div class="marca">Gym<span>Admin</span></div>' +
    grupos.map(g => `<p class="grupo">${cli && g.tituloCli ? g.tituloCli : g.titulo}</p>` +
      g.items.map(i => `<a href="${i.href}" class="${i.id === activa ? 'on' : ''}">${cli && i.textoCli ? i.textoCli : i.texto}</a>`).join('')).join('') +
    '<button class="salir" id="salir">Cerrar sesión</button>';
  $('#topbar').innerHTML = `<button id="burger" aria-label="Menú">☰</button><h1>${esc(titulo)}</h1>
    <div class="usuario"><span class="avatar">${esc(nombre[0].toUpperCase())}</span><span><b>${esc(nombre)}</b><small>${esc(u.rol)}</small></span></div>`;
  $('#burger').onclick = () => document.body.classList.toggle('abierto');
  $('#salir').onclick = async () => { try { await api('/auth/logout', 'POST'); } catch {} finally { sessionStorage.removeItem('usuario'); location.href = 'index.html'; } };
}
