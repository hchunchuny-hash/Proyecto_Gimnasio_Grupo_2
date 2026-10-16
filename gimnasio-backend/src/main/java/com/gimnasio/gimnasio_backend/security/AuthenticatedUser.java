package com.gimnasio.gimnasio_backend.security;

public record AuthenticatedUser(Integer usuarioId, String nombreUsuario, String rol) { }
