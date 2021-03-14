package com.polytech.polysign.web.rest;

import com.polytech.polysign.domain.SignatureProcess;
import com.polytech.polysign.service.SignatureProcessService;
import com.polytech.polysign.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.polytech.polysign.domain.SignatureProcess}.
 */
@RestController
@RequestMapping("/api")
public class SignatureProcessResource {

    private final Logger log = LoggerFactory.getLogger(SignatureProcessResource.class);

    private static final String ENTITY_NAME = "signatureProcess";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignatureProcessService signatureProcessService;

    public SignatureProcessResource(SignatureProcessService signatureProcessService) {
        this.signatureProcessService = signatureProcessService;
    }

    /**
     * {@code POST  /signature-processes} : Create a new signatureProcess.
     *
     * @param signatureProcess the signatureProcess to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signatureProcess, or with status {@code 400 (Bad Request)} if the signatureProcess has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/signature-processes")
    public ResponseEntity<SignatureProcess> createSignatureProcess(@Valid @RequestBody SignatureProcess signatureProcess) throws URISyntaxException {
        log.debug("REST request to save SignatureProcess : {}", signatureProcess);
        if (signatureProcess.getId() != null) {
            throw new BadRequestAlertException("A new signatureProcess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignatureProcess result = signatureProcessService.save(signatureProcess);
        return ResponseEntity.created(new URI("/api/signature-processes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /signature-processes} : Updates an existing signatureProcess.
     *
     * @param signatureProcess the signatureProcess to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signatureProcess,
     * or with status {@code 400 (Bad Request)} if the signatureProcess is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signatureProcess couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/signature-processes")
    public ResponseEntity<SignatureProcess> updateSignatureProcess(@Valid @RequestBody SignatureProcess signatureProcess) throws URISyntaxException {
        log.debug("REST request to update SignatureProcess : {}", signatureProcess);
        if (signatureProcess.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SignatureProcess result = signatureProcessService.save(signatureProcess);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signatureProcess.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /signature-processes} : get all the signatureProcesses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signatureProcesses in body.
     */
    @GetMapping("/signature-processes")
    public ResponseEntity<List<SignatureProcess>> getAllSignatureProcesses(Pageable pageable) {
        log.debug("REST request to get a page of SignatureProcesses");
        Page<SignatureProcess> page = signatureProcessService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /signature-processes/:id} : get the "id" signatureProcess.
     *
     * @param id the id of the signatureProcess to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signatureProcess, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/signature-processes/{id}")
    public ResponseEntity<SignatureProcess> getSignatureProcess(@PathVariable Long id) {
        log.debug("REST request to get SignatureProcess : {}", id);
        Optional<SignatureProcess> signatureProcess = signatureProcessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signatureProcess);
    }

    /**
     * {@code DELETE  /signature-processes/:id} : delete the "id" signatureProcess.
     *
     * @param id the id of the signatureProcess to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/signature-processes/{id}")
    public ResponseEntity<Void> deleteSignatureProcess(@PathVariable Long id) {
        log.debug("REST request to delete SignatureProcess : {}", id);
        signatureProcessService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/signature-processes/signedFile/{id}/creator/{username}")
    public ResponseEntity<Void> deleteSignatureProcess(@PathVariable Long id,@PathVariable String username ) {
        log.debug("REST request to add SignatureProcess : {}", id);
        signatureProcessService.saveSignatureProcess(id, username);;
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
