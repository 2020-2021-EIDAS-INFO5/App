package com.polytech.polysign.web.rest;

import com.polytech.polysign.domain.SignaturePlacement;
import com.polytech.polysign.service.SignaturePlacementService;
import com.polytech.polysign.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.polytech.polysign.domain.SignaturePlacement}.
 */
@RestController
@RequestMapping("/api")
public class SignaturePlacementResource {

    private final Logger log = LoggerFactory.getLogger(SignaturePlacementResource.class);

    private static final String ENTITY_NAME = "signaturePlacement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignaturePlacementService signaturePlacementService;

    public SignaturePlacementResource(SignaturePlacementService signaturePlacementService) {
        this.signaturePlacementService = signaturePlacementService;
    }

    /**
     * {@code POST  /signature-placements} : Create a new signaturePlacement.
     *
     * @param signaturePlacement the signaturePlacement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signaturePlacement, or with status {@code 400 (Bad Request)} if the signaturePlacement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/signature-placements")
    public ResponseEntity<SignaturePlacement> createSignaturePlacement(@RequestBody SignaturePlacement signaturePlacement) throws URISyntaxException {
        log.debug("REST request to save SignaturePlacement : {}", signaturePlacement);
        if (signaturePlacement.getId() != null) {
            throw new BadRequestAlertException("A new signaturePlacement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignaturePlacement result = signaturePlacementService.saveSignaturePlacementForUserAndSignature(signaturePlacement,2L,"otba.zeramdini@gmail.com" );       
        return ResponseEntity.created(new URI("/api/signature-placements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /signature-placements} : Updates an existing signaturePlacement.
     *
     * @param signaturePlacement the signaturePlacement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signaturePlacement,
     * or with status {@code 400 (Bad Request)} if the signaturePlacement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signaturePlacement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/signature-placements")
    public ResponseEntity<SignaturePlacement> updateSignaturePlacement(@RequestBody SignaturePlacement signaturePlacement) throws URISyntaxException {
        log.debug("REST request to update SignaturePlacement : {}", signaturePlacement);
        if (signaturePlacement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SignaturePlacement result = signaturePlacementService.save(signaturePlacement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signaturePlacement.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /signature-placements} : get all the signaturePlacements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signaturePlacements in body.
     */
    @GetMapping("/signature-placements")
    public List<SignaturePlacement> getAllSignaturePlacements() {
        log.debug("REST request to get all SignaturePlacements");
        return signaturePlacementService.findAll();
    }

    /**
     * {@code GET  /signature-placements/:id} : get the "id" signaturePlacement.
     *
     * @param id the id of the signaturePlacement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signaturePlacement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/signature-placements/{id}")
    public ResponseEntity<SignaturePlacement> getSignaturePlacement(@PathVariable Long id) {
        log.debug("REST request to get SignaturePlacement : {}", id);
        Optional<SignaturePlacement> signaturePlacement = signaturePlacementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signaturePlacement);
    }

    /**
     * {@code DELETE  /signature-placements/:id} : delete the "id" signaturePlacement.
     *
     * @param id the id of the signaturePlacement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/signature-placements/{id}")
    public ResponseEntity<Void> deleteSignaturePlacement(@PathVariable Long id) {
        log.debug("REST request to delete SignaturePlacement : {}", id);
        signaturePlacementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


       /**
     * {@code POST  /signature-placements} : Create a new signaturePlacement.
     *
     * @param signaturePlacement the signaturePlacement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signaturePlacement, or with status {@code 400 (Bad Request)} if the signaturePlacement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
   /* @PostMapping("/signature-placements/signFileID/{signFileID}/userEmail/{email}")
    public ResponseEntity<SignaturePlacement> createSignaturePlacementForUserAndSignature(@RequestBody SignaturePlacement signaturePlacement,@PathVariable Long signFileID, @PathVariable String email) throws URISyntaxException {
        log.debug("REST request to save SignaturePlacement : {}", signaturePlacement);
        if (signaturePlacement.getId() != null) {
            throw new BadRequestAlertException("A new signaturePlacement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignaturePlacement result = signaturePlacementService.saveSignaturePlacementForUserAndSignature(signaturePlacement,signFileID,email );
        return ResponseEntity.created(new URI("/api/signature-placements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/
}
