package com.polytech.polysign.web.rest;

import com.polytech.polysign.PolySignApp;
import com.polytech.polysign.config.TestSecurityConfiguration;
import com.polytech.polysign.domain.UserEntity;
import com.polytech.polysign.repository.UserEntityRepository;
import com.polytech.polysign.service.UserEntityService;

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
 * Integration tests for the {@link UserEntityResource} REST controller.
 */
@SpringBootTest(classes = { PolySignApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class UserEntityResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "{d@,x7ocG.(u.T";
    private static final String UPDATED_EMAIL = "><_BF[@c!1zQh.4t2";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserEntityMockMvc;

    private UserEntity userEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEntity createEntity(EntityManager em) {
        UserEntity userEntity = new UserEntity()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE);
        return userEntity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEntity createUpdatedEntity(EntityManager em) {
        UserEntity userEntity = new UserEntity()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE);
        return userEntity;
    }

    @BeforeEach
    public void initTest() {
        userEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserEntity() throws Exception {
        int databaseSizeBeforeCreate = userEntityRepository.findAll().size();
        // Create the UserEntity
        restUserEntityMockMvc.perform(post("/api/user-entities").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userEntity)))
            .andExpect(status().isCreated());

        // Validate the UserEntity in the database
        List<UserEntity> userEntityList = userEntityRepository.findAll();
        assertThat(userEntityList).hasSize(databaseSizeBeforeCreate + 1);
        UserEntity testUserEntity = userEntityList.get(userEntityList.size() - 1);
        assertThat(testUserEntity.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserEntity.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserEntity.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserEntity.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createUserEntityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userEntityRepository.findAll().size();

        // Create the UserEntity with an existing ID
        userEntity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserEntityMockMvc.perform(post("/api/user-entities").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userEntity)))
            .andExpect(status().isBadRequest());

        // Validate the UserEntity in the database
        List<UserEntity> userEntityList = userEntityRepository.findAll();
        assertThat(userEntityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEntityRepository.findAll().size();
        // set the field null
        userEntity.setFirstName(null);

        // Create the UserEntity, which fails.


        restUserEntityMockMvc.perform(post("/api/user-entities").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userEntity)))
            .andExpect(status().isBadRequest());

        List<UserEntity> userEntityList = userEntityRepository.findAll();
        assertThat(userEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEntityRepository.findAll().size();
        // set the field null
        userEntity.setLastName(null);

        // Create the UserEntity, which fails.


        restUserEntityMockMvc.perform(post("/api/user-entities").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userEntity)))
            .andExpect(status().isBadRequest());

        List<UserEntity> userEntityList = userEntityRepository.findAll();
        assertThat(userEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEntityRepository.findAll().size();
        // set the field null
        userEntity.setEmail(null);

        // Create the UserEntity, which fails.


        restUserEntityMockMvc.perform(post("/api/user-entities").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userEntity)))
            .andExpect(status().isBadRequest());

        List<UserEntity> userEntityList = userEntityRepository.findAll();
        assertThat(userEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEntityRepository.findAll().size();
        // set the field null
        userEntity.setPhone(null);

        // Create the UserEntity, which fails.


        restUserEntityMockMvc.perform(post("/api/user-entities").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userEntity)))
            .andExpect(status().isBadRequest());

        List<UserEntity> userEntityList = userEntityRepository.findAll();
        assertThat(userEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserEntities() throws Exception {
        // Initialize the database
        userEntityRepository.saveAndFlush(userEntity);

        // Get all the userEntityList
        restUserEntityMockMvc.perform(get("/api/user-entities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }
    
    @Test
    @Transactional
    public void getUserEntity() throws Exception {
        // Initialize the database
        userEntityRepository.saveAndFlush(userEntity);

        // Get the userEntity
        restUserEntityMockMvc.perform(get("/api/user-entities/{id}", userEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userEntity.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }
    @Test
    @Transactional
    public void getNonExistingUserEntity() throws Exception {
        // Get the userEntity
        restUserEntityMockMvc.perform(get("/api/user-entities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserEntity() throws Exception {
        // Initialize the database
        userEntityService.save(userEntity);

        int databaseSizeBeforeUpdate = userEntityRepository.findAll().size();

        // Update the userEntity
        UserEntity updatedUserEntity = userEntityRepository.findById(userEntity.getId()).get();
        // Disconnect from session so that the updates on updatedUserEntity are not directly saved in db
        em.detach(updatedUserEntity);
        updatedUserEntity
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE);

        restUserEntityMockMvc.perform(put("/api/user-entities").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserEntity)))
            .andExpect(status().isOk());

        // Validate the UserEntity in the database
        List<UserEntity> userEntityList = userEntityRepository.findAll();
        assertThat(userEntityList).hasSize(databaseSizeBeforeUpdate);
        UserEntity testUserEntity = userEntityList.get(userEntityList.size() - 1);
        assertThat(testUserEntity.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserEntity.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserEntity.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserEntity.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserEntity() throws Exception {
        int databaseSizeBeforeUpdate = userEntityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEntityMockMvc.perform(put("/api/user-entities").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userEntity)))
            .andExpect(status().isBadRequest());

        // Validate the UserEntity in the database
        List<UserEntity> userEntityList = userEntityRepository.findAll();
        assertThat(userEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserEntity() throws Exception {
        // Initialize the database
        userEntityService.save(userEntity);

        int databaseSizeBeforeDelete = userEntityRepository.findAll().size();

        // Delete the userEntity
        restUserEntityMockMvc.perform(delete("/api/user-entities/{id}", userEntity.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserEntity> userEntityList = userEntityRepository.findAll();
        assertThat(userEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
