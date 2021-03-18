package com.polytech.polysign.web.rest;

import com.polytech.polysign.domain.SignedFile;
import com.polytech.polysign.service.SignedFileService;
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
import javax.websocket.server.PathParam;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.polytech.polysign.domain.SignedFile}.
 */
@RestController
@RequestMapping("/api")
public class SignedFileResource {

    private final Logger log = LoggerFactory.getLogger(SignedFileResource.class);

    private static final String ENTITY_NAME = "signedFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignedFileService signedFileService;

    public SignedFileResource(SignedFileService signedFileService) {
        this.signedFileService = signedFileService;
    }

    /**
     * {@code POST  /signed-files} : Create a new signedFile.
     *
     * @param signedFile the signedFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signedFile, or with status {@code 400 (Bad Request)} if the signedFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/signed-files")
    public ResponseEntity<SignedFile> createSignedFile(@Valid @RequestBody SignedFile signedFile) throws URISyntaxException {
        log.debug("REST request to save SignedFile : {}", signedFile);
        if (signedFile.getId() != null) {
            throw new BadRequestAlertException("A new signedFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignedFile result = signedFileService.save(signedFile);
        return ResponseEntity.created(new URI("/api/signed-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


        /**
     * {@code POST  /signed-files} : Create a new signedFile.
     *
     * @param signedFile the signedFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signedFile, or with status {@code 400 (Bad Request)} if the signedFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/signed-files/createSignedFileAndSignatureProcess")
    public ResponseEntity<SignedFile> createSignedFileAndSignatureProcess(@Valid @RequestBody SignedFile signedFile) throws URISyntaxException {
        log.debug("REST request to save SignedFile : {}", signedFile);
        if (signedFile.getId() != null) {
            throw new BadRequestAlertException("A new signedFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignedFile result = signedFileService.saveSignedFileAndSignatureProcess(signedFile);
        return ResponseEntity.created(new URI("/api/signed-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /signed-files} : Updates an existing signedFile.
     *
     * @param signedFile the signedFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signedFile,
     * or with status {@code 400 (Bad Request)} if the signedFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signedFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/signed-files")
    public ResponseEntity<SignedFile> updateSignedFile(@Valid @RequestBody SignedFile signedFile) throws URISyntaxException {
        log.debug("REST request to update SignedFile : {}", signedFile);
        if (signedFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SignedFile result = signedFileService.save(signedFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signedFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /signed-files} : get all the signedFiles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signedFiles in body.
     */
    @GetMapping("/signed-files")
    public ResponseEntity<List<SignedFile>> getAllSignedFiles(Pageable pageable) {
        log.debug("REST request to get a page of SignedFiles");
        Page<SignedFile> page = signedFileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /signed-files/:id} : get the "id" signedFile.
     *
     * @param id the id of the signedFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signedFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/signed-files/{id}")
    public ResponseEntity<SignedFile> getSignedFile(@PathVariable Long id) {
        log.debug("REST request to get SignedFile : {}", id);
        Optional<SignedFile> signedFile = signedFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signedFile);
    }

    /**
     * {@code DELETE  /signed-files/:id} : delete the "id" signedFile.
     *
     * @param id the id of the signedFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     * @throws Exception 
     */
    @DeleteMapping("/signed-files/{id}")
    public ResponseEntity<Void> deleteSignedFile(@PathVariable Long id) {
        log.debug("REST request to delete SignedFile : {}", id);
        signedFileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }



    /**
     * {@code GET  /signed-files/signCertificate/singOrderId/{id}/userId/{userId}: sign the file of the given signOrder by the user given by his id 
     *
     * @param id the id of the signOrder to sign and the id of the user who signs
     * @throws Exception 
     */
    @GetMapping("/signed-files/signCertificate/singOrderId/{id}/userId/{userId}")
    public void signCertificate(@PathVariable Long id,@PathVariable Long userId) throws Exception {
        signedFileService.certificateCreation(id,userId);
    }
    
}
