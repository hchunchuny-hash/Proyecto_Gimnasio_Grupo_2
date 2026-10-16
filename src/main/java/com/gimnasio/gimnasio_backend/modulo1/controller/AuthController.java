package com.gimnasio.gimnasio_backend.modulo1.controller;

import com.gimnasio.gimnasio_backend.modulo1.dto.AuthDTOs.*;
import com.gimnasio.gimnasio_backend.modulo1.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gimnasio.gimnasio_backend.security.SessionTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.beans.factory.annotation.Value;
import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private SessionTokenService sessions;

    @Value("${app.security.cookie-secure:false}") private boolean secureCookie;

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken csrfToken) { return Map.of("token", csrfToken.getToken()); }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.registrar(request);
        String token = response.getToken(); response.setToken(null);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie(token, Duration.ofHours(8)).toString()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            String token = response.getToken(); response.setToken(null);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie(token, Duration.ofHours(8)).toString()).body(response);
        }
        catch (org.springframework.security.authentication.BadCredentialsException ex) {
            return ResponseEntity.status(401).body(java.util.Map.of("mensaje", "Usuario o contraseña incorrectos."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) for (var cookie : cookies) if ("GYM_SESSION".equals(cookie.getName())) sessions.revoke(cookie.getValue());
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie("", Duration.ZERO).toString()).build();
    }

    private ResponseCookie cookie(String value, Duration age) {
        return ResponseCookie.from("GYM_SESSION", value).httpOnly(true).secure(secureCookie)
                .sameSite("Strict").path("/").maxAge(age).build();
    }
}
