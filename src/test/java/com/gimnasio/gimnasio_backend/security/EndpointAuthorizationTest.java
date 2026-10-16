package com.gimnasio.gimnasio_backend.security;

import com.gimnasio.gimnasio_backend.modulo1.controller.UsuarioAdminController;
import com.gimnasio.gimnasio_backend.modulo1.service.UsuarioAdminService;
import com.gimnasio.gimnasio_backend.modulo2.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioAdminController.class)
@Import(SecurityConfiguration.class)
class EndpointAuthorizationTest {
    @Autowired MockMvc mvc;
    @MockBean SessionTokenService sessions;
    @MockBean ClienteRepository clientes;
    @MockBean UsuarioAdminService usuarios;

    @Test void privateEndpointRejectsAnonymousRequest() throws Exception {
        mvc.perform(get("/api/usuarios")).andExpect(status().isUnauthorized());
    }

    @Test void employeeCannotOpenUserAdministration() throws Exception {
        mvc.perform(get("/api/usuarios").with(user("recepcion").roles("RECEPCION")))
                .andExpect(status().isForbidden());
    }

    @Test void administratorCanOpenUserAdministration() throws Exception {
        mvc.perform(get("/api/usuarios").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk());
    }

    @Test void trainerCannotReadAnotherClientsPayments() throws Exception {
        mvc.perform(get("/api/pagos/cliente/42").with(user("coach").roles("ENTRENADOR")))
                .andExpect(status().isForbidden());
    }

    @Test void employeeCannotWriteClientTrainingProgress() throws Exception {
        mvc.perform(post("/api/progresos").with(user("employee").roles("EMPLEADO")).with(csrf()))
                .andExpect(status().isForbidden());
    }
}
