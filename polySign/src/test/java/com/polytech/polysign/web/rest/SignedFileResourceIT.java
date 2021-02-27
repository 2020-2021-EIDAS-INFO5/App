package com.polytech.polysign.web.rest;

import com.polytech.polysign.PolySignApp;
import com.polytech.polysign.config.TestSecurityConfiguration;
import com.polytech.polysign.domain.SignedFile;
import com.polytech.polysign.repository.SignedFileRepository;
import com.polytech.polysign.service.SignedFileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SignedFileResource} REST controller.
 */
@SpringBootTest(classes = { PolySignApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SignedFileResourceIT {

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_BYTES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_BYTES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_BYTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_BYTES_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_SIGNING_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SIGNING_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_SIZE = 1;
    private static final Integer UPDATED_SIZE = 2;

    @Autowired
    private SignedFileRepository signedFileRepository;

    @Autowired
    private SignedFileService signedFileService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignedFileMockMvc;

    private SignedFile signedFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignedFile createEntity(EntityManager em) {
        SignedFile signedFile = new SignedFile()
            .filename(DEFAULT_FILENAME)
            .fileBytes(DEFAULT_FILE_BYTES)
            .fileBytesContentType(DEFAULT_FILE_BYTES_CONTENT_TYPE)
            .signingDate(DEFAULT_SIGNING_DATE)
            .size(DEFAULT_SIZE);
        return signedFile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignedFile createUpdatedEntity(EntityManager em) {
        SignedFile signedFile = new SignedFile()
            .filename(UPDATED_FILENAME)
            .fileBytes(UPDATED_FILE_BYTES)
            .fileBytesContentType(UPDATED_FILE_BYTES_CONTENT_TYPE)
            .signingDate(UPDATED_SIGNING_DATE)
            .size(UPDATED_SIZE);
        return signedFile;
    }

    @BeforeEach
    public void initTest() {
        signedFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignedFile() throws Exception {
        int databaseSizeBeforeCreate = signedFileRepository.findAll().size();
        // Create the SignedFile
        restSignedFileMockMvc.perform(post("/api/signed-files").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signedFile)))
            .andExpect(status().isCreated());

        // Validate the SignedFile in the database
        List<SignedFile> signedFileList = signedFileRepository.findAll();
        assertThat(signedFileList).hasSize(databaseSizeBeforeCreate + 1);
        SignedFile testSignedFile = signedFileList.get(signedFileList.size() - 1);
        assertThat(testSignedFile.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testSignedFile.getFileBytes()).isEqualTo(DEFAULT_FILE_BYTES);
        assertThat(testSignedFile.getFileBytesContentType()).isEqualTo(DEFAULT_FILE_BYTES_CONTENT_TYPE);
        assertThat(testSignedFile.getSigningDate()).isEqualTo(DEFAULT_SIGNING_DATE);
        assertThat(testSignedFile.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    @Transactional
    public void createSignedFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = signedFileRepository.findAll().size();

        // Create the SignedFile with an existing ID
        signedFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignedFileMockMvc.perform(post("/api/signed-files").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signedFile)))
            .andExpect(status().isBadRequest());

        // Validate the SignedFile in the database
        List<SignedFile> signedFileList = signedFileRepository.findAll();
        assertThat(signedFileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFilenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = signedFileRepository.findAll().size();
        // set the field null
        signedFile.setFilename(null);

        // Create the SignedFile, which fails.


        restSignedFileMockMvc.perform(post("/api/signed-files").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signedFile)))
            .andExpect(status().isBadRequest());

        List<SignedFile> signedFileList = signedFileRepository.findAll();
        assertThat(signedFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSigningDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = signedFileRepository.findAll().size();
        // set the field null
        signedFile.setSigningDate(null);

        // Create the SignedFile, which fails.


        restSignedFileMockMvc.perform(post("/api/signed-files").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signedFile)))
            .andExpect(status().isBadRequest());

        List<SignedFile> signedFileList = signedFileRepository.findAll();
        assertThat(signedFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSignedFiles() throws Exception {
        // Initialize the database
        signedFileRepository.saveAndFlush(signedFile);

        // Get all the signedFileList
        restSignedFileMockMvc.perform(get("/api/signed-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signedFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)))
            .andExpect(jsonPath("$.[*].fileBytesContentType").value(hasItem(DEFAULT_FILE_BYTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileBytes").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_BYTES))))
            .andExpect(jsonPath("$.[*].signingDate").value(hasItem(DEFAULT_SIGNING_DATE.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)));
    }
    
    @Test
    @Transactional
    public void getSignedFile() throws Exception {
        // Initialize the database
        signedFileRepository.saveAndFlush(signedFile);

        // Get the signedFile
        restSignedFileMockMvc.perform(get("/api/signed-files/{id}", signedFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signedFile.getId().intValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME))
            .andExpect(jsonPath("$.fileBytesContentType").value(DEFAULT_FILE_BYTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileBytes").value(Base64Utils.encodeToString(DEFAULT_FILE_BYTES)))
            .andExpect(jsonPath("$.signingDate").value(DEFAULT_SIGNING_DATE.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE));
    }
    @Test
    @Transactional
    public void getNonExistingSignedFile() throws Exception {
        // Get the signedFile
        restSignedFileMockMvc.perform(get("/api/signed-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignedFile() throws Exception {
        // Initialize the database
        signedFileService.save(signedFile);

        int databaseSizeBeforeUpdate = signedFileRepository.findAll().size();

        // Update the signedFile
        SignedFile updatedSignedFile = signedFileRepository.findById(signedFile.getId()).get();
        // Disconnect from session so that the updates on updatedSignedFile are not directly saved in db
        em.detach(updatedSignedFile);
        updatedSignedFile
            .filename(UPDATED_FILENAME)
            .fileBytes(UPDATED_FILE_BYTES)
            .fileBytesContentType(UPDATED_FILE_BYTES_CONTENT_TYPE)
            .signingDate(UPDATED_SIGNING_DATE)
            .size(UPDATED_SIZE);

        restSignedFileMockMvc.perform(put("/api/signed-files").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSignedFile)))
            .andExpect(status().isOk());

        // Validate the SignedFile in the database
        List<SignedFile> signedFileList = signedFileRepository.findAll();
        assertThat(signedFileList).hasSize(databaseSizeBeforeUpdate);
        SignedFile testSignedFile = signedFileList.get(signedFileList.size() - 1);
        assertThat(testSignedFile.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testSignedFile.getFileBytes()).isEqualTo(UPDATED_FILE_BYTES);
        assertThat(testSignedFile.getFileBytesContentType()).isEqualTo(UPDATED_FILE_BYTES_CONTENT_TYPE);
        assertThat(testSignedFile.getSigningDate()).isEqualTo(UPDATED_SIGNING_DATE);
        assertThat(testSignedFile.getSize()).isEqualTo(UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void updateNonExistingSignedFile() throws Exception {
        int databaseSizeBeforeUpdate = signedFileRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignedFileMockMvc.perform(put("/api/signed-files").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signedFile)))
            .andExpect(status().isBadRequest());

        // Validate the SignedFile in the database
        List<SignedFile> signedFileList = signedFileRepository.findAll();
        assertThat(signedFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSignedFile() throws Exception {
        // Initialize the database
        signedFileService.save(signedFile);

        int databaseSizeBeforeDelete = signedFileRepository.findAll().size();

        // Delete the signedFile
        restSignedFileMockMvc.perform(delete("/api/signed-files/{id}", signedFile.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SignedFile> signedFileList = signedFileRepository.findAll();
        assertThat(signedFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
