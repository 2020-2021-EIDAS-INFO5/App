package com.polytech.polysign.web.rest;

import com.polytech.polysign.PolySignApp;
import com.polytech.polysign.config.TestSecurityConfiguration;
import com.polytech.polysign.domain.Authorit;
import com.polytech.polysign.repository.AuthoritRepository;
import com.polytech.polysign.service.AuthoritService;

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
 * Integration tests for the {@link AuthoritResource} REST controller.
 */
@SpringBootTest(classes = { PolySignApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class AuthoritResourceIT {

    private static final Role DEFAULT_HAS_ROLE = Role.ADMIN_ORGANIZATION;
    private static final Role UPDATED_HAS_ROLE = Role.USER_ORGANIZATION;

    @Autowired
    private AuthoritRepository authoritRepository;

    @Autowired
    private AuthoritService authoritService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthoritMockMvc;

    private Authorit authorit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authorit createEntity(EntityManager em) {
        Authorit authorit = new Authorit()
            .hasRole(DEFAULT_HAS_ROLE);
        return authorit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authorit createUpdatedEntity(EntityManager em) {
        Authorit authorit = new Authorit()
            .hasRole(UPDATED_HAS_ROLE);
        return authorit;
    }

    @BeforeEach
    public void initTest() {
        authorit = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthorit() throws Exception {
        int databaseSizeBeforeCreate = authoritRepository.findAll().size();
        // Create the Authorit
        restAuthoritMockMvc.perform(post("/api/authorits").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(authorit)))
            .andExpect(status().isCreated());

        // Validate the Authorit in the database
        List<Authorit> authoritList = authoritRepository.findAll();
        assertThat(authoritList).hasSize(databaseSizeBeforeCreate + 1);
        Authorit testAuthorit = authoritList.get(authoritList.size() - 1);
        assertThat(testAuthorit.getHasRole()).isEqualTo(DEFAULT_HAS_ROLE);
    }

    @Test
    @Transactional
    public void createAuthoritWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authoritRepository.findAll().size();

        // Create the Authorit with an existing ID
        authorit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthoritMockMvc.perform(post("/api/authorits").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(authorit)))
            .andExpect(status().isBadRequest());

        // Validate the Authorit in the database
        List<Authorit> authoritList = authoritRepository.findAll();
        assertThat(authoritList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkHasRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = authoritRepository.findAll().size();
        // set the field null
        authorit.setHasRole(null);

        // Create the Authorit, which fails.


        restAuthoritMockMvc.perform(post("/api/authorits").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(authorit)))
            .andExpect(status().isBadRequest());

        List<Authorit> authoritList = authoritRepository.findAll();
        assertThat(authoritList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuthorits() throws Exception {
        // Initialize the database
        authoritRepository.saveAndFlush(authorit);

        // Get all the authoritList
        restAuthoritMockMvc.perform(get("/api/authorits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorit.getId().intValue())))
            .andExpect(jsonPath("$.[*].hasRole").value(hasItem(DEFAULT_HAS_ROLE.toString())));
    }
    
    @Test
    @Transactional
    public void getAuthorit() throws Exception {
        // Initialize the database
        authoritRepository.saveAndFlush(authorit);

        // Get the authorit
        restAuthoritMockMvc.perform(get("/api/authorits/{id}", authorit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(authorit.getId().intValue()))
            .andExpect(jsonPath("$.hasRole").value(DEFAULT_HAS_ROLE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAuthorit() throws Exception {
        // Get the authorit
        restAuthoritMockMvc.perform(get("/api/authorits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthorit() throws Exception {
        // Initialize the database
        authoritService.save(authorit);

        int databaseSizeBeforeUpdate = authoritRepository.findAll().size();

        // Update the authorit
        Authorit updatedAuthorit = authoritRepository.findById(authorit.getId()).get();
        // Disconnect from session so that the updates on updatedAuthorit are not directly saved in db
        em.detach(updatedAuthorit);
        updatedAuthorit
            .hasRole(UPDATED_HAS_ROLE);

        restAuthoritMockMvc.perform(put("/api/authorits").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuthorit)))
            .andExpect(status().isOk());

        // Validate the Authorit in the database
        List<Authorit> authoritList = authoritRepository.findAll();
        assertThat(authoritList).hasSize(databaseSizeBeforeUpdate);
        Authorit testAuthorit = authoritList.get(authoritList.size() - 1);
        assertThat(testAuthorit.getHasRole()).isEqualTo(UPDATED_HAS_ROLE);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthorit() throws Exception {
        int databaseSizeBeforeUpdate = authoritRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthoritMockMvc.perform(put("/api/authorits").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(authorit)))
            .andExpect(status().isBadRequest());

        // Validate the Authorit in the database
        List<Authorit> authoritList = authoritRepository.findAll();
        assertThat(authoritList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuthorit() throws Exception {
        // Initialize the database
        authoritService.save(authorit);

        int databaseSizeBeforeDelete = authoritRepository.findAll().size();

        // Delete the authorit
        restAuthoritMockMvc.perform(delete("/api/authorits/{id}", authorit.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Authorit> authoritList = authoritRepository.findAll();
        assertThat(authoritList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
