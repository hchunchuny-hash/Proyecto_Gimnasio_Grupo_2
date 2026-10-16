const f = $('form'), boton = f.querySelector('button');
const registro = f.id === 'registro';

// En el registro, los campos de perfil cambian según el rol elegido
if (registro) {
  f.fechaNacimiento.max = ayerISO(); soloDigitos(f.telefono);
  f.nombres.required = f.apellidos.required = true;
}

// El backend crea el usuario y su perfil (cliente o entrenador) en una sola llamada
function armarRegistro(d) {
  const c = { nombreUsuario: d.nombreUsuario, correo: d.correo, contrasena: d.contrasena, rol: 'CLIENTE', nombres: d.nombres, apellidos: d.apellidos, telefono: d.telefono || null };
  c.fechaNacimiento = d.fechaNacimiento || null;
  return c;
}

f.onsubmit = async e => {
  e.preventDefault();
  const d = Object.fromEntries(new FormData(f)), texto = boton.textContent;
  if (registro) {
    if (!claveValida(d.contrasena)) { toast(MSG_CLAVE, 'error'); return; }
    if (!TEL_OK.test(d.telefono || '')) { toast(MSG_TEL, 'error'); return; }
  }
  boton.disabled = true; boton.textContent = 'Un momento...';
  try {
    const u = await api(registro ? '/auth/register' : '/auth/login', 'POST',
      registro ? armarRegistro(d) : { nombreUsuario: d.nombreUsuario, contrasena: d.contrasena });
    sessionStorage.setItem('usuario', JSON.stringify(u));
    location.href = 'inicio.html';
  } catch (err) {
    toast(err.message, 'error');
    boton.disabled = false; boton.textContent = texto;
  }
};
