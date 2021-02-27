package com.polytech.polysign.web.rest;

import com.polytech.polysign.PolySignApp;
import com.polytech.polysign.config.TestSecurityConfiguration;
import com.polytech.polysign.domain.AuthenticatedUser;
import com.polytech.polysign.repository.AuthenticatedUserRepository;
import com.polytech.polysign.service.AuthenticatedUserService;

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

/**
 * Integration tests for the {@link AuthenticatedUserResource} REST controller.
 */
@SpringBootTest(classes = { PolySignApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class AuthenticatedUserResourceIT {

    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;

    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthenticatedUserMockMvc;

    private AuthenticatedUser authenticatedUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuthenticatedUser createEntity(EntityManager em) {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        return authenticatedUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuthenticatedUser createUpdatedEntity(EntityManager em) {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        return authenticatedUser;
    }

    @BeforeEach
    public void initTest() {
        authenticatedUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthenticatedUser() throws Exception {
        int databaseSizeBeforeCreate = authenticatedUserRepository.findAll().size();
        // Create the AuthenticatedUser
        restAuthenticatedUserMockMvc.perform(post("/api/authenticated-users").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(authenticatedUser)))
            .andExpect(status().isCreated());

        // Validate the AuthenticatedUser in the database
        List<AuthenticatedUser> authenticatedUserList = authenticatedUserRepository.findAll();
        assertThat(authenticatedUserList).hasSize(databaseSizeBeforeCreate + 1);
        AuthenticatedUser testAuthenticatedUser = authenticatedUserList.get(authenticatedUserList.size() - 1);
    }

    @Test
    @Transactional
    public void createAuthenticatedUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authenticatedUserRepository.findAll().size();

        // Create the AuthenticatedUser with an existing ID
        authenticatedUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthenticatedUserMockMvc.perform(post("/api/authenticated-users").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(authenticatedUser)))
            .andExpect(status().isBadRequest());

        // Validate the AuthenticatedUser in the database
        List<AuthenticatedUser> authenticatedUserList = authenticatedUserRepository.findAll();
        assertThat(authenticatedUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAuthenticatedUsers() throws Exception {
        // Initialize the database
        authenticatedUserRepository.saveAndFlush(authenticatedUser);

        // Get all the authenticatedUserList
        restAuthenticatedUserMockMvc.perform(get("/api/authenticated-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authenticatedUser.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getAuthenticatedUser() throws Exception {
        // Initialize the database
        authenticatedUserRepository.saveAndFlush(authenticatedUser);

        // Get the authenticatedUser
        restAuthenticatedUserMockMvc.perform(get("/api/authenticated-users/{id}", authenticatedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(authenticatedUser.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingAuthenticatedUser() throws Exception {
        // Get the authenticatedUser
        restAuthenticatedUserMockMvc.perform(get("/api/authenticated-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthenticatedUser() throws Exception {
        // Initialize the database
        authenticatedUserService.save(authenticatedUser);

        int databaseSizeBeforeUpdate = authenticatedUserRepository.findAll().size();

        // Update the authenticatedUser
        AuthenticatedUser updatedAuthenticatedUser = authenticatedUserRepository.findById(authenticatedUser.getId()).get();
        // Disconnect from session so that the updates on updatedAuthenticatedUser are not directly saved in db
        em.detach(updatedAuthenticatedUser);

        restAuthenticatedUserMockMvc.perform(put("/api/authenticated-users").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuthenticatedUser)))
            .andExpect(status().isOk());

        // Validate the AuthenticatedUser in the database
        List<AuthenticatedUser> authenticatedUserList = authenticatedUserRepository.findAll();
        assertThat(authenticatedUserList).hasSize(databaseSizeBeforeUpdate);
        AuthenticatedUser testAuthenticatedUser = authenticatedUserList.get(authenticatedUserList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthenticatedUser() throws Exception {
        int databaseSizeBeforeUpdate = authenticatedUserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthenticatedUserMockMvc.perform(put("/api/authenticated-users").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(authenticatedUser)))
            .andExpect(status().isBadRequest());

        // Validate the AuthenticatedUser in the database
        List<AuthenticatedUser> authenticatedUserList = authenticatedUserRepository.findAll();
        assertThat(authenticatedUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuthenticatedUser() throws Exception {
        // Initialize the database
        authenticatedUserService.save(authenticatedUser);

        int databaseSizeBeforeDelete = authenticatedUserRepository.findAll().size();

        // Delete the authenticatedUser
        restAuthenticatedUserMockMvc.perform(delete("/api/authenticated-users/{id}", authenticatedUser.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuthenticatedUser> authenticatedUserList = authenticatedUserRepository.findAll();
        assertThat(authenticatedUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
