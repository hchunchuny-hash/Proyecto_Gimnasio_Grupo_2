const f1 = $('#solicitar'), f2 = $('#resetear');
const tokenEnlace = new URLSearchParams(location.hash.slice(1)).get('token');
if (tokenEnlace) {
  f1.closest('#paso1').hidden = true;
  $('#paso2').hidden = false;
  f2.token.value = tokenEnlace;
  history.replaceState(null, '', location.pathname);
}

f1.onsubmit = async e => {
  e.preventDefault();
  const boton = f1.querySelector('button'); boton.disabled = true;
  try {
    const r = await api('/auth/recuperacion/solicitar', 'POST', { nombreUsuario: f1.nombreUsuario.value.trim(), correo: f1.correo.value.trim() });
    toast(r || 'Si los datos corresponden a una cuenta activa, recibirá instrucciones por correo.');
  } catch (err) {
    toast(err.message, 'error');
  } finally { boton.disabled = false; }
};

f2.onsubmit = async e => {
  e.preventDefault();
  if (f2.nuevaContrasena.value !== f2.repetir.value) { toast('Las contraseñas no coinciden.', 'error'); return; }
  if (!claveValida(f2.nuevaContrasena.value)) { toast(MSG_CLAVE, 'error'); return; }
  const boton = f2.querySelector('button'); boton.disabled = true;
  try {
    await api('/auth/recuperacion/resetear', 'POST', { token: f2.token.value.trim(), nuevaContrasena: f2.nuevaContrasena.value });
    toast('Contraseña cambiada. Ya puedes iniciar sesión.');
    setTimeout(() => location.href = 'index.html', 1800);
  } catch (err) {
    toast(err.message, 'error');
    boton.disabled = false;
  }
};
