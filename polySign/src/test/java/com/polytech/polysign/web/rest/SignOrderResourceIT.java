package com.polytech.polysign.web.rest;

import com.polytech.polysign.PolySignApp;
import com.polytech.polysign.config.TestSecurityConfiguration;
import com.polytech.polysign.domain.SignOrder;
import com.polytech.polysign.repository.SignOrderRepository;
import com.polytech.polysign.service.SignOrderService;

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

import com.polytech.polysign.domain.enumeration.SignatureMethod;
/**
 * Integration tests for the {@link SignOrderResource} REST controller.
 */
@SpringBootTest(classes = { PolySignApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SignOrderResourceIT {

    private static final Integer DEFAULT_RANK = 1;
    private static final Integer UPDATED_RANK = 2;

    private static final SignatureMethod DEFAULT_SIGNATURE_METHOD = SignatureMethod.EMAIL;
    private static final SignatureMethod UPDATED_SIGNATURE_METHOD = SignatureMethod.SMS;

    @Autowired
    private SignOrderRepository signOrderRepository;

    @Autowired
    private SignOrderService signOrderService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignOrderMockMvc;

    private SignOrder signOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignOrder createEntity(EntityManager em) {
        SignOrder signOrder = new SignOrder()
            .rank(DEFAULT_RANK)
            .signatureMethod(DEFAULT_SIGNATURE_METHOD);
        return signOrder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignOrder createUpdatedEntity(EntityManager em) {
        SignOrder signOrder = new SignOrder()
            .rank(UPDATED_RANK)
            .signatureMethod(UPDATED_SIGNATURE_METHOD);
        return signOrder;
    }

    @BeforeEach
    public void initTest() {
        signOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignOrder() throws Exception {
        int databaseSizeBeforeCreate = signOrderRepository.findAll().size();
        // Create the SignOrder
        restSignOrderMockMvc.perform(post("/api/sign-orders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signOrder)))
            .andExpect(status().isCreated());

        // Validate the SignOrder in the database
        List<SignOrder> signOrderList = signOrderRepository.findAll();
        assertThat(signOrderList).hasSize(databaseSizeBeforeCreate + 1);
        SignOrder testSignOrder = signOrderList.get(signOrderList.size() - 1);
        assertThat(testSignOrder.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testSignOrder.getSignatureMethod()).isEqualTo(DEFAULT_SIGNATURE_METHOD);
    }

    @Test
    @Transactional
    public void createSignOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = signOrderRepository.findAll().size();

        // Create the SignOrder with an existing ID
        signOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignOrderMockMvc.perform(post("/api/sign-orders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signOrder)))
            .andExpect(status().isBadRequest());

        // Validate the SignOrder in the database
        List<SignOrder> signOrderList = signOrderRepository.findAll();
        assertThat(signOrderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSignOrders() throws Exception {
        // Initialize the database
        signOrderRepository.saveAndFlush(signOrder);

        // Get all the signOrderList
        restSignOrderMockMvc.perform(get("/api/sign-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK)))
            .andExpect(jsonPath("$.[*].signatureMethod").value(hasItem(DEFAULT_SIGNATURE_METHOD.toString())));
    }
    
    @Test
    @Transactional
    public void getSignOrder() throws Exception {
        // Initialize the database
        signOrderRepository.saveAndFlush(signOrder);

        // Get the signOrder
        restSignOrderMockMvc.perform(get("/api/sign-orders/{id}", signOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signOrder.getId().intValue()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK))
            .andExpect(jsonPath("$.signatureMethod").value(DEFAULT_SIGNATURE_METHOD.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSignOrder() throws Exception {
        // Get the signOrder
        restSignOrderMockMvc.perform(get("/api/sign-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignOrder() throws Exception {
        // Initialize the database
        signOrderService.save(signOrder);

        int databaseSizeBeforeUpdate = signOrderRepository.findAll().size();

        // Update the signOrder
        SignOrder updatedSignOrder = signOrderRepository.findById(signOrder.getId()).get();
        // Disconnect from session so that the updates on updatedSignOrder are not directly saved in db
        em.detach(updatedSignOrder);
        updatedSignOrder
            .rank(UPDATED_RANK)
            .signatureMethod(UPDATED_SIGNATURE_METHOD);

        restSignOrderMockMvc.perform(put("/api/sign-orders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSignOrder)))
            .andExpect(status().isOk());

        // Validate the SignOrder in the database
        List<SignOrder> signOrderList = signOrderRepository.findAll();
        assertThat(signOrderList).hasSize(databaseSizeBeforeUpdate);
        SignOrder testSignOrder = signOrderList.get(signOrderList.size() - 1);
        assertThat(testSignOrder.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testSignOrder.getSignatureMethod()).isEqualTo(UPDATED_SIGNATURE_METHOD);
    }

    @Test
    @Transactional
    public void updateNonExistingSignOrder() throws Exception {
        int databaseSizeBeforeUpdate = signOrderRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignOrderMockMvc.perform(put("/api/sign-orders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signOrder)))
            .andExpect(status().isBadRequest());

        // Validate the SignOrder in the database
        List<SignOrder> signOrderList = signOrderRepository.findAll();
        assertThat(signOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSignOrder() throws Exception {
        // Initialize the database
        signOrderService.save(signOrder);

        int databaseSizeBeforeDelete = signOrderRepository.findAll().size();

        // Delete the signOrder
        restSignOrderMockMvc.perform(delete("/api/sign-orders/{id}", signOrder.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SignOrder> signOrderList = signOrderRepository.findAll();
        assertThat(signOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
