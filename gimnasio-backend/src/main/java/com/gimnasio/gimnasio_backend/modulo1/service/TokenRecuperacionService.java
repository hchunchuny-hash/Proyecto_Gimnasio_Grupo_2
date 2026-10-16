package com.gimnasio.gimnasio_backend.modulo1.service;

import com.gimnasio.gimnasio_backend.modulo1.dto.TokenRecuperacionDTOs.*;
import com.gimnasio.gimnasio_backend.modulo1.entity.TokenRecuperacion;
import com.gimnasio.gimnasio_backend.modulo1.entity.Usuario;
import com.gimnasio.gimnasio_backend.modulo1.repository.TokenRecuperacionRepository;
import com.gimnasio.gimnasio_backend.modulo1.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Base64;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.gimnasio.gimnasio_backend.security.SessionTokenService;

@Service
public class TokenRecuperacionService {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Autowired
    private TokenRecuperacionRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    private SessionTokenService sessions;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Value("${app.frontend.url:http://localhost:5500}")
    private String frontendUrl;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    @Value("${spring.mail.host:}")
    private String mailHost;

    @Value("${spring.mail.password:}")
    private String mailPassword;

    @PostConstruct
    void validarConfiguracionCorreo() {
        if (mailEnabled && (mailSender == null || mailHost.isBlank() || mailFrom.isBlank() || mailPassword.isBlank()))
            throw new IllegalStateException("La recuperación por correo está habilitada, pero falta configurar SMTP.");
    }

    @Transactional
    public void solicitarRecuperacion(String nombreUsuario, String correo) {
        if (!mailEnabled || mailSender == null) return;
        Usuario usuario = usuarioRepository.findByNombreUsuarioAndCorreo(nombreUsuario, correo)
                .orElse(null);

        if (usuario == null || !Boolean.TRUE.equals(usuario.getEstado())) return;

        byte[] random = new byte[32];
        SECURE_RANDOM.nextBytes(random);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(random);
        TokenRecuperacion tokenEntity = TokenRecuperacion.builder()
                .usuario(usuario)
                .token(hashToken(token))
                .fechaExpiracion(LocalDateTime.now().plusMinutes(30))
                .usado(false)
                .build();

        tokenRepository.save(tokenEntity);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(usuario.getCorreo());
        message.setSubject("Recuperación de contraseña - GymAdmin");
        message.setText("Usa este enlace para cambiar tu contraseña (vence en 30 minutos): "
                + frontendUrl.replaceAll("/$", "") + "/recuperar.html#token=" + token);
        mailSender.send(message);
    }

    @Transactional
    public void cambiarContrasena(ResetPasswordRequest request) {
        TokenRecuperacion tokenEntity = tokenRepository.findByTokenAndUsadoFalse(hashToken(request.getToken()))
                .orElseThrow(() -> new RuntimeException("El token es inválido o ya ha sido utilizado."));

        if (tokenEntity.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token de recuperación ha expirado.");
        }

        Usuario usuario = tokenEntity.getUsuario();
        usuario.setContrasena(PASSWORD_ENCODER.encode(request.getNuevaContrasena()));
        usuarioRepository.save(usuario);
        sessions.revokeUser(usuario.getUsuarioId());

        tokenEntity.setUsado(true);
        tokenRepository.save(tokenEntity);
    }

    private String hashToken(String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("No está disponible SHA-256.", ex);
        }
    }
}
