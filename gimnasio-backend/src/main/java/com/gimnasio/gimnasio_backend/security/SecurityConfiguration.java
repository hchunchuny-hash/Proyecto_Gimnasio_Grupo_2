package com.gimnasio.gimnasio_backend.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.core.Authentication;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {
    @Value("${app.security.cookie-secure:false}") private boolean secureCookies;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, SessionTokenService tokens, ClienteRepository clientes) throws Exception {
        http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository(secureCookies))
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/api/auth/login", "POST"),
                                new AntPathRequestMatcher("/api/auth/register", "POST"),
                                new AntPathRequestMatcher("/api/auth/recuperacion/solicitar", "POST"),
                                new AntPathRequestMatcher("/api/auth/recuperacion/resetear", "POST")))
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            res.getWriter().write("{\"mensaje\":\"Autenticación requerida o sesión vencida.\"}");
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            res.getWriter().write("{\"mensaje\":\"No tiene permiso para esta operación.\"}");
                        }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/register",
                                "/api/auth/recuperacion/solicitar", "/api/auth/recuperacion/resetear").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/csrf").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                        .requestMatchers("/api/usuarios/**").hasAnyRole("ADMINISTRADOR", "ADMIN")
                        .requestMatchers("/api/mi-cuenta/**").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/clientes/*").access(ownClient(clientes))
                        .requestMatchers(HttpMethod.GET, "/api/membresias/cliente/*", "/api/pagos/cliente/*",
                                "/api/asistencia/cliente/*", "/api/reservas/cliente/*", "/api/progresos/cliente/*",
                                "/api/rutinas/cliente/*", "/api/clases/inscripciones/cliente/*", "/api/facturas/cliente/*").access(ownClient(clientes))
                        .requestMatchers(HttpMethod.GET, "/api/notificaciones").access(ownClientQuery(clientes))
                        .requestMatchers(HttpMethod.POST, "/api/clases/inscripciones", "/api/asistencia/marcar",
                                "/api/reservas", "/api/progresos").access(clientOrStaff())
                        .requestMatchers(HttpMethod.GET, "/api/areas", "/api/clases", "/api/clases/horarios",
                                "/api/ejercicios", "/api/servicios", "/api/tipos-membresia")
                                .hasAnyRole("CLIENTE", "ADMINISTRADOR", "ADMIN", "EMPLEADO", "RECEPCION", "ENTRENADOR")
                        .requestMatchers("/api/ejercicios/**", "/api/rutinas/**", "/api/progresos/**").hasRole("ENTRENADOR")
                        .requestMatchers("/api/clases/**", "/api/reservas/**", "/api/asistencia/**", "/api/areas/**", "/api/servicios/**")
                                .hasAnyRole("ENTRENADOR", "EMPLEADO", "RECEPCION")
                        .requestMatchers(HttpMethod.GET, "/api/clientes/**").hasAnyRole("ENTRENADOR", "EMPLEADO", "RECEPCION")
                        .requestMatchers("/api/clientes/**", "/api/membresias/**", "/api/pagos/**", "/api/facturas/**",
                                "/api/notificaciones/**", "/api/tipos-membresia/**")
                                .hasAnyRole("EMPLEADO", "RECEPCION")
                        .requestMatchers("/api/**").hasAnyRole("ADMINISTRADOR", "ADMIN")
                        .anyRequest().denyAll())
                .addFilterBefore(new SessionCookieAuthenticationFilter(tokens), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> ownClient(ClienteRepository clientes) {
        return (authentication, context) -> {
            Authentication current = authentication.get();
            String path = context.getRequest().getRequestURI();
            if (hasAnyRole(current, "ADMINISTRADOR", "ADMIN"))
                return new AuthorizationDecision(true);
            if (path.startsWith("/api/clientes/") && hasAnyRole(current, "ENTRENADOR", "EMPLEADO", "RECEPCION"))
                return new AuthorizationDecision(true);
            if ((path.startsWith("/api/membresias/cliente/") || path.startsWith("/api/pagos/cliente/")
                    || path.startsWith("/api/facturas/cliente/")) && hasAnyRole(current, "EMPLEADO", "RECEPCION"))
                return new AuthorizationDecision(true);
            if ((path.startsWith("/api/asistencia/cliente/") || path.startsWith("/api/reservas/cliente/")
                    || path.startsWith("/api/clases/inscripciones/cliente/"))
                    && hasAnyRole(current, "ENTRENADOR", "EMPLEADO", "RECEPCION"))
                return new AuthorizationDecision(true);
            if ((path.startsWith("/api/progresos/cliente/") || path.startsWith("/api/rutinas/cliente/"))
                    && hasAnyRole(current, "ENTRENADOR"))
                return new AuthorizationDecision(true);
            if (!(current.getPrincipal() instanceof AuthenticatedUser user)) return new AuthorizationDecision(false);
            var profile = clientes.findByUsuarioUsuarioId(user.usuarioId()).orElse(null);
            if (profile == null) return new AuthorizationDecision(false);
            String id = path.substring(path.lastIndexOf('/') + 1);
            try { return new AuthorizationDecision(profile.getClienteId().equals(Integer.valueOf(id))); }
            catch (NumberFormatException ex) { return new AuthorizationDecision(false); }
        };
    }

    private org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> ownClientQuery(ClienteRepository clientes) {
        return (authentication, context) -> {
            Authentication current = authentication.get();
            if (hasAnyRole(current, "ADMINISTRADOR", "ADMIN", "EMPLEADO", "RECEPCION")) return new AuthorizationDecision(true);
            if (!(current.getPrincipal() instanceof AuthenticatedUser user)) return new AuthorizationDecision(false);
            var profile = clientes.findByUsuarioUsuarioId(user.usuarioId()).orElse(null);
            String requested = context.getRequest().getParameter("clienteId");
            try { return new AuthorizationDecision(profile != null && requested != null && profile.getClienteId().equals(Integer.valueOf(requested))); }
            catch (NumberFormatException ex) { return new AuthorizationDecision(false); }
        };
    }

    private boolean isStaff(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(a -> List.of("ROLE_ADMINISTRADOR", "ROLE_ADMIN", "ROLE_EMPLEADO", "ROLE_RECEPCION", "ROLE_ENTRENADOR").contains(a.getAuthority()));
    }

    private org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> clientOrStaff() {
        return (authentication, context) -> {
            Authentication current = authentication.get();
            boolean client = current.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
            if (context.getRequest().getRequestURI().equals("/api/progresos"))
                return new AuthorizationDecision(client || hasAnyRole(current, "ENTRENADOR", "ADMINISTRADOR", "ADMIN"));
            return new AuthorizationDecision(client || isStaff(current));
        };
    }

    private boolean hasAnyRole(Authentication authentication, String... roles) {
        return authentication.getAuthorities().stream().anyMatch(a -> java.util.Arrays.stream(roles)
                .anyMatch(role -> a.getAuthority().equals("ROLE_" + role)));
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(@Value("${app.cors.allowed-origins:http://localhost:5500}") String configuredOrigins) {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOriginPatterns(java.util.Arrays.stream(configuredOrigins.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList());
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN"));
        cors.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    private CsrfTokenRepository csrfTokenRepository(boolean secure) {
        CookieCsrfTokenRepository repository = new CookieCsrfTokenRepository();
        repository.setCookieCustomizer(cookie -> cookie.path("/").sameSite("Strict").secure(secure));
        return repository;
    }
}
