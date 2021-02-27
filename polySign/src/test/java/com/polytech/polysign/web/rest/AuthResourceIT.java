package com.polytech.polysign.web.rest;

import com.polytech.polysign.PolySignApp;
import com.polytech.polysign.config.TestSecurityConfiguration;
import com.polytech.polysign.domain.Auth;
import com.polytech.polysign.repository.AuthRepository;
import com.polytech.polysign.service.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polytech.polysign.domain.enumeration.Role;
/**
 * Integration tests for the {@link AuthResource} REST controller.
 */
@SpringBootTest(classes = { PolySignApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class AuthResourceIT {

    private static final Role DEFAULT_HAS_ROLE = Role.ADMIN;
    private static final Role UPDATED_HAS_ROLE = Role.USER;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthMockMvc;

    private Auth auth;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auth createEntity(EntityManager em) {
        Auth auth = new Auth()
            .hasRole(DEFAULT_HAS_ROLE);
        return auth;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auth createUpdatedEntity(EntityManager em) {
        Auth auth = new Auth()
            .hasRole(UPDATED_HAS_ROLE);
        return auth;
    }

    @BeforeEach
    public void initTest() {
        auth = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuth() throws Exception {
        int databaseSizeBeforeCreate = authRepository.findAll().size();
        // Create the Auth
        restAuthMockMvc.perform(post("/api/auths").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auth)))
            .andExpect(status().isCreated());

        // Validate the Auth in the database
        List<Auth> authList = authRepository.findAll();
        assertThat(authList).hasSize(databaseSizeBeforeCreate + 1);
        Auth testAuth = authList.get(authList.size() - 1);
        assertThat(testAuth.getHasRole()).isEqualTo(DEFAULT_HAS_ROLE);
    }

    @Test
    @Transactional
    public void createAuthWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authRepository.findAll().size();

        // Create the Auth with an existing ID
        auth.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthMockMvc.perform(post("/api/auths").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auth)))
            .andExpect(status().isBadRequest());

        // Validate the Auth in the database
        List<Auth> authList = authRepository.findAll();
        assertThat(authList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkHasRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = authRepository.findAll().size();
        // set the field null
        auth.setHasRole(null);

        // Create the Auth, which fails.


        restAuthMockMvc.perform(post("/api/auths").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auth)))
            .andExpect(status().isBadRequest());

        List<Auth> authList = authRepository.findAll();
        assertThat(authList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuths() throws Exception {
        // Initialize the database
        authRepository.saveAndFlush(auth);

        // Get all the authList
        restAuthMockMvc.perform(get("/api/auths?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auth.getId().intValue())))
            .andExpect(jsonPath("$.[*].hasRole").value(hasItem(DEFAULT_HAS_ROLE.toString())));
    }
    
    @Test
    @Transactional
    public void getAuth() throws Exception {
        // Initialize the database
        authRepository.saveAndFlush(auth);

        // Get the auth
        restAuthMockMvc.perform(get("/api/auths/{id}", auth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auth.getId().intValue()))
            .andExpect(jsonPath("$.hasRole").value(DEFAULT_HAS_ROLE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAuth() throws Exception {
        // Get the auth
        restAuthMockMvc.perform(get("/api/auths/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuth() throws Exception {
        // Initialize the database
        authService.save(auth);

        int databaseSizeBeforeUpdate = authRepository.findAll().size();

        // Update the auth
        Auth updatedAuth = authRepository.findById(auth.getId()).get();
        // Disconnect from session so that the updates on updatedAuth are not directly saved in db
        em.detach(updatedAuth);
        updatedAuth
            .hasRole(UPDATED_HAS_ROLE);

        restAuthMockMvc.perform(put("/api/auths").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuth)))
            .andExpect(status().isOk());

        // Validate the Auth in the database
        List<Auth> authList = authRepository.findAll();
        assertThat(authList).hasSize(databaseSizeBeforeUpdate);
        Auth testAuth = authList.get(authList.size() - 1);
        assertThat(testAuth.getHasRole()).isEqualTo(UPDATED_HAS_ROLE);
    }

    @Test
    @Transactional
    public void updateNonExistingAuth() throws Exception {
        int databaseSizeBeforeUpdate = authRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthMockMvc.perform(put("/api/auths").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auth)))
            .andExpect(status().isBadRequest());

        // Validate the Auth in the database
        List<Auth> authList = authRepository.findAll();
        assertThat(authList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuth() throws Exception {
        // Initialize the database
        authService.save(auth);

        int databaseSizeBeforeDelete = authRepository.findAll().size();

        // Delete the auth
        restAuthMockMvc.perform(delete("/api/auths/{id}", auth.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Auth> authList = authRepository.findAll();
        assertThat(authList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
