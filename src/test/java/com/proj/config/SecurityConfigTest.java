package com.proj.config;

import com.proj.auth.JwtAuthenticationConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebAppConfiguration
public class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockitoBean JwtAuthenticationConverter converter;
    @MockitoBean JwtDecoder decoder;
    @MockitoBean UserDetailsService userDetailsService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void admins_should_be_able_to_access_books_endpoint() throws Exception {
        mvc.perform(get("/api/books"))
                .andExpect(status().is(not(HttpStatus.FORBIDDEN)));
    }

    @Test
    @WithMockUser
    void users_should_not_be_able_to_access_books_endpoint() throws Exception {
        mvc.perform(get("/api/books"))
                .andExpect(status().isForbidden());
    }
}
