package com.polytech.polysign.web.rest;

import com.polytech.polysign.PolySignApp;
import com.polytech.polysign.config.TestSecurityConfiguration;
import com.polytech.polysign.domain.SignaturePlacement;
import com.polytech.polysign.repository.SignaturePlacementRepository;
import com.polytech.polysign.service.SignaturePlacementService;

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
 * Integration tests for the {@link SignaturePlacementResource} REST controller.
 */
@SpringBootTest(classes = { PolySignApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SignaturePlacementResourceIT {

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;

    private static final Double DEFAULT_COORDINATE_X = 1D;
    private static final Double UPDATED_COORDINATE_X = 2D;

    private static final Double DEFAULT_COORDINATE_Y = 1D;
    private static final Double UPDATED_COORDINATE_Y = 2D;

    @Autowired
    private SignaturePlacementRepository signaturePlacementRepository;

    @Autowired
    private SignaturePlacementService signaturePlacementService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignaturePlacementMockMvc;

    private SignaturePlacement signaturePlacement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignaturePlacement createEntity(EntityManager em) {
        SignaturePlacement signaturePlacement = new SignaturePlacement()
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .coordinateX(DEFAULT_COORDINATE_X)
            .coordinateY(DEFAULT_COORDINATE_Y);
        return signaturePlacement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignaturePlacement createUpdatedEntity(EntityManager em) {
        SignaturePlacement signaturePlacement = new SignaturePlacement()
            .pageNumber(UPDATED_PAGE_NUMBER)
            .coordinateX(UPDATED_COORDINATE_X)
            .coordinateY(UPDATED_COORDINATE_Y);
        return signaturePlacement;
    }

    @BeforeEach
    public void initTest() {
        signaturePlacement = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignaturePlacement() throws Exception {
        int databaseSizeBeforeCreate = signaturePlacementRepository.findAll().size();
        // Create the SignaturePlacement
        restSignaturePlacementMockMvc.perform(post("/api/signature-placements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signaturePlacement)))
            .andExpect(status().isCreated());

        // Validate the SignaturePlacement in the database
        List<SignaturePlacement> signaturePlacementList = signaturePlacementRepository.findAll();
        assertThat(signaturePlacementList).hasSize(databaseSizeBeforeCreate + 1);
        SignaturePlacement testSignaturePlacement = signaturePlacementList.get(signaturePlacementList.size() - 1);
        assertThat(testSignaturePlacement.getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(testSignaturePlacement.getCoordinateX()).isEqualTo(DEFAULT_COORDINATE_X);
        assertThat(testSignaturePlacement.getCoordinateY()).isEqualTo(DEFAULT_COORDINATE_Y);
    }

    @Test
    @Transactional
    public void createSignaturePlacementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = signaturePlacementRepository.findAll().size();

        // Create the SignaturePlacement with an existing ID
        signaturePlacement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignaturePlacementMockMvc.perform(post("/api/signature-placements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signaturePlacement)))
            .andExpect(status().isBadRequest());

        // Validate the SignaturePlacement in the database
        List<SignaturePlacement> signaturePlacementList = signaturePlacementRepository.findAll();
        assertThat(signaturePlacementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSignaturePlacements() throws Exception {
        // Initialize the database
        signaturePlacementRepository.saveAndFlush(signaturePlacement);

        // Get all the signaturePlacementList
        restSignaturePlacementMockMvc.perform(get("/api/signature-placements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signaturePlacement.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].coordinateX").value(hasItem(DEFAULT_COORDINATE_X.doubleValue())))
            .andExpect(jsonPath("$.[*].coordinateY").value(hasItem(DEFAULT_COORDINATE_Y.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSignaturePlacement() throws Exception {
        // Initialize the database
        signaturePlacementRepository.saveAndFlush(signaturePlacement);

        // Get the signaturePlacement
        restSignaturePlacementMockMvc.perform(get("/api/signature-placements/{id}", signaturePlacement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signaturePlacement.getId().intValue()))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.coordinateX").value(DEFAULT_COORDINATE_X.doubleValue()))
            .andExpect(jsonPath("$.coordinateY").value(DEFAULT_COORDINATE_Y.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingSignaturePlacement() throws Exception {
        // Get the signaturePlacement
        restSignaturePlacementMockMvc.perform(get("/api/signature-placements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignaturePlacement() throws Exception {
        // Initialize the database
        signaturePlacementService.save(signaturePlacement);

        int databaseSizeBeforeUpdate = signaturePlacementRepository.findAll().size();

        // Update the signaturePlacement
        SignaturePlacement updatedSignaturePlacement = signaturePlacementRepository.findById(signaturePlacement.getId()).get();
        // Disconnect from session so that the updates on updatedSignaturePlacement are not directly saved in db
        em.detach(updatedSignaturePlacement);
        updatedSignaturePlacement
            .pageNumber(UPDATED_PAGE_NUMBER)
            .coordinateX(UPDATED_COORDINATE_X)
            .coordinateY(UPDATED_COORDINATE_Y);

        restSignaturePlacementMockMvc.perform(put("/api/signature-placements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSignaturePlacement)))
            .andExpect(status().isOk());

        // Validate the SignaturePlacement in the database
        List<SignaturePlacement> signaturePlacementList = signaturePlacementRepository.findAll();
        assertThat(signaturePlacementList).hasSize(databaseSizeBeforeUpdate);
        SignaturePlacement testSignaturePlacement = signaturePlacementList.get(signaturePlacementList.size() - 1);
        assertThat(testSignaturePlacement.getPageNumber()).isEqualTo(UPDATED_PAGE_NUMBER);
        assertThat(testSignaturePlacement.getCoordinateX()).isEqualTo(UPDATED_COORDINATE_X);
        assertThat(testSignaturePlacement.getCoordinateY()).isEqualTo(UPDATED_COORDINATE_Y);
    }

    @Test
    @Transactional
    public void updateNonExistingSignaturePlacement() throws Exception {
        int databaseSizeBeforeUpdate = signaturePlacementRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignaturePlacementMockMvc.perform(put("/api/signature-placements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signaturePlacement)))
            .andExpect(status().isBadRequest());

        // Validate the SignaturePlacement in the database
        List<SignaturePlacement> signaturePlacementList = signaturePlacementRepository.findAll();
        assertThat(signaturePlacementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSignaturePlacement() throws Exception {
        // Initialize the database
        signaturePlacementService.save(signaturePlacement);

        int databaseSizeBeforeDelete = signaturePlacementRepository.findAll().size();

        // Delete the signaturePlacement
        restSignaturePlacementMockMvc.perform(delete("/api/signature-placements/{id}", signaturePlacement.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SignaturePlacement> signaturePlacementList = signaturePlacementRepository.findAll();
        assertThat(signaturePlacementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
