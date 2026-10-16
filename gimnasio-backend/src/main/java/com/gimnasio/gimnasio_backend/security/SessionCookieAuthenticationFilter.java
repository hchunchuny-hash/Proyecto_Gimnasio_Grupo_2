package com.gimnasio.gimnasio_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class SessionCookieAuthenticationFilter extends OncePerRequestFilter {
    private final SessionTokenService tokens;
    public SessionCookieAuthenticationFilter(SessionTokenService tokens) { this.tokens = tokens; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) for (var cookie : cookies) if ("GYM_SESSION".equals(cookie.getName())) token = cookie.getValue();
        if (token != null) {
            AuthenticatedUser user = tokens.authenticate(token);
            if (user != null) {
                var auth = new UsernamePasswordAuthenticationToken(user, null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.rol().toUpperCase())));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }
}
