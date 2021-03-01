package com.polytech.polysign.web.rest;

import com.polytech.polysign.PolySignApp;
import com.polytech.polysign.config.TestSecurityConfiguration;
import com.polytech.polysign.domain.SignatureProcess;
import com.polytech.polysign.repository.SignatureProcessRepository;
import com.polytech.polysign.service.SignatureProcessService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polytech.polysign.domain.enumeration.Status;
/**
 * Integration tests for the {@link SignatureProcessResource} REST controller.
 */
@SpringBootTest(classes = { PolySignApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SignatureProcessResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_EMISSION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EMISSION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Status DEFAULT_STATUS = Status.COMPLETED;
    private static final Status UPDATED_STATUS = Status.PENDING;

    private static final Boolean DEFAULT_ORDERED_SIGNING = false;
    private static final Boolean UPDATED_ORDERED_SIGNING = true;

    @Autowired
    private SignatureProcessRepository signatureProcessRepository;

    @Autowired
    private SignatureProcessService signatureProcessService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignatureProcessMockMvc;

    private SignatureProcess signatureProcess;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignatureProcess createEntity(EntityManager em) {
        SignatureProcess signatureProcess = new SignatureProcess()
            .title(DEFAULT_TITLE)
            .emissionDate(DEFAULT_EMISSION_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .status(DEFAULT_STATUS)
            .orderedSigning(DEFAULT_ORDERED_SIGNING);
        return signatureProcess;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignatureProcess createUpdatedEntity(EntityManager em) {
        SignatureProcess signatureProcess = new SignatureProcess()
            .title(UPDATED_TITLE)
            .emissionDate(UPDATED_EMISSION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .status(UPDATED_STATUS)
            .orderedSigning(UPDATED_ORDERED_SIGNING);
        return signatureProcess;
    }

    @BeforeEach
    public void initTest() {
        signatureProcess = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignatureProcess() throws Exception {
        int databaseSizeBeforeCreate = signatureProcessRepository.findAll().size();
        // Create the SignatureProcess
        restSignatureProcessMockMvc.perform(post("/api/signature-processes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signatureProcess)))
            .andExpect(status().isCreated());

        // Validate the SignatureProcess in the database
        List<SignatureProcess> signatureProcessList = signatureProcessRepository.findAll();
        assertThat(signatureProcessList).hasSize(databaseSizeBeforeCreate + 1);
        SignatureProcess testSignatureProcess = signatureProcessList.get(signatureProcessList.size() - 1);
        assertThat(testSignatureProcess.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSignatureProcess.getEmissionDate()).isEqualTo(DEFAULT_EMISSION_DATE);
        assertThat(testSignatureProcess.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testSignatureProcess.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSignatureProcess.isOrderedSigning()).isEqualTo(DEFAULT_ORDERED_SIGNING);
    }

    @Test
    @Transactional
    public void createSignatureProcessWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = signatureProcessRepository.findAll().size();

        // Create the SignatureProcess with an existing ID
        signatureProcess.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignatureProcessMockMvc.perform(post("/api/signature-processes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signatureProcess)))
            .andExpect(status().isBadRequest());

        // Validate the SignatureProcess in the database
        List<SignatureProcess> signatureProcessList = signatureProcessRepository.findAll();
        assertThat(signatureProcessList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureProcessRepository.findAll().size();
        // set the field null
        signatureProcess.setTitle(null);

        // Create the SignatureProcess, which fails.


        restSignatureProcessMockMvc.perform(post("/api/signature-processes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signatureProcess)))
            .andExpect(status().isBadRequest());

        List<SignatureProcess> signatureProcessList = signatureProcessRepository.findAll();
        assertThat(signatureProcessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmissionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureProcessRepository.findAll().size();
        // set the field null
        signatureProcess.setEmissionDate(null);

        // Create the SignatureProcess, which fails.


        restSignatureProcessMockMvc.perform(post("/api/signature-processes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signatureProcess)))
            .andExpect(status().isBadRequest());

        List<SignatureProcess> signatureProcessList = signatureProcessRepository.findAll();
        assertThat(signatureProcessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureProcessRepository.findAll().size();
        // set the field null
        signatureProcess.setExpirationDate(null);

        // Create the SignatureProcess, which fails.


        restSignatureProcessMockMvc.perform(post("/api/signature-processes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signatureProcess)))
            .andExpect(status().isBadRequest());

        List<SignatureProcess> signatureProcessList = signatureProcessRepository.findAll();
        assertThat(signatureProcessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureProcessRepository.findAll().size();
        // set the field null
        signatureProcess.setStatus(null);

        // Create the SignatureProcess, which fails.


        restSignatureProcessMockMvc.perform(post("/api/signature-processes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signatureProcess)))
            .andExpect(status().isBadRequest());

        List<SignatureProcess> signatureProcessList = signatureProcessRepository.findAll();
        assertThat(signatureProcessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderedSigningIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureProcessRepository.findAll().size();
        // set the field null
        signatureProcess.setOrderedSigning(null);

        // Create the SignatureProcess, which fails.


        restSignatureProcessMockMvc.perform(post("/api/signature-processes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signatureProcess)))
            .andExpect(status().isBadRequest());

        List<SignatureProcess> signatureProcessList = signatureProcessRepository.findAll();
        assertThat(signatureProcessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSignatureProcesses() throws Exception {
        // Initialize the database
        signatureProcessRepository.saveAndFlush(signatureProcess);

        // Get all the signatureProcessList
        restSignatureProcessMockMvc.perform(get("/api/signature-processes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signatureProcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].emissionDate").value(hasItem(DEFAULT_EMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].orderedSigning").value(hasItem(DEFAULT_ORDERED_SIGNING.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSignatureProcess() throws Exception {
        // Initialize the database
        signatureProcessRepository.saveAndFlush(signatureProcess);

        // Get the signatureProcess
        restSignatureProcessMockMvc.perform(get("/api/signature-processes/{id}", signatureProcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signatureProcess.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.emissionDate").value(DEFAULT_EMISSION_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.orderedSigning").value(DEFAULT_ORDERED_SIGNING.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingSignatureProcess() throws Exception {
        // Get the signatureProcess
        restSignatureProcessMockMvc.perform(get("/api/signature-processes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignatureProcess() throws Exception {
        // Initialize the database
        signatureProcessService.save(signatureProcess);

        int databaseSizeBeforeUpdate = signatureProcessRepository.findAll().size();

        // Update the signatureProcess
        SignatureProcess updatedSignatureProcess = signatureProcessRepository.findById(signatureProcess.getId()).get();
        // Disconnect from session so that the updates on updatedSignatureProcess are not directly saved in db
        em.detach(updatedSignatureProcess);
        updatedSignatureProcess
            .title(UPDATED_TITLE)
            .emissionDate(UPDATED_EMISSION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .status(UPDATED_STATUS)
            .orderedSigning(UPDATED_ORDERED_SIGNING);

        restSignatureProcessMockMvc.perform(put("/api/signature-processes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSignatureProcess)))
            .andExpect(status().isOk());

        // Validate the SignatureProcess in the database
        List<SignatureProcess> signatureProcessList = signatureProcessRepository.findAll();
        assertThat(signatureProcessList).hasSize(databaseSizeBeforeUpdate);
        SignatureProcess testSignatureProcess = signatureProcessList.get(signatureProcessList.size() - 1);
        assertThat(testSignatureProcess.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSignatureProcess.getEmissionDate()).isEqualTo(UPDATED_EMISSION_DATE);
        assertThat(testSignatureProcess.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testSignatureProcess.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSignatureProcess.isOrderedSigning()).isEqualTo(UPDATED_ORDERED_SIGNING);
    }

    @Test
    @Transactional
    public void updateNonExistingSignatureProcess() throws Exception {
        int databaseSizeBeforeUpdate = signatureProcessRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignatureProcessMockMvc.perform(put("/api/signature-processes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signatureProcess)))
            .andExpect(status().isBadRequest());

        // Validate the SignatureProcess in the database
        List<SignatureProcess> signatureProcessList = signatureProcessRepository.findAll();
        assertThat(signatureProcessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSignatureProcess() throws Exception {
        // Initialize the database
        signatureProcessService.save(signatureProcess);

        int databaseSizeBeforeDelete = signatureProcessRepository.findAll().size();

        // Delete the signatureProcess
        restSignatureProcessMockMvc.perform(delete("/api/signature-processes/{id}", signatureProcess.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SignatureProcess> signatureProcessList = signatureProcessRepository.findAll();
        assertThat(signatureProcessList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
