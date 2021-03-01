package com.polytech.polysign.web.rest;

import com.polytech.polysign.domain.Authorit;
import com.polytech.polysign.service.AuthoritService;
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
 * REST controller for managing {@link com.polytech.polysign.domain.Authorit}.
 */
@RestController
@RequestMapping("/api")
public class AuthoritResource {

    private final Logger log = LoggerFactory.getLogger(AuthoritResource.class);

    private static final String ENTITY_NAME = "authorit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthoritService authoritService;

    public AuthoritResource(AuthoritService authoritService) {
        this.authoritService = authoritService;
    }

    /**
     * {@code POST  /authorits} : Create a new authorit.
     *
     * @param authorit the authorit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authorit, or with status {@code 400 (Bad Request)} if the authorit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authorits")
    public ResponseEntity<Authorit> createAuthorit(@Valid @RequestBody Authorit authorit) throws URISyntaxException {
        log.debug("REST request to save Authorit : {}", authorit);
        if (authorit.getId() != null) {
            throw new BadRequestAlertException("A new authorit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Authorit result = authoritService.save(authorit);
        return ResponseEntity.created(new URI("/api/authorits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /authorits} : Updates an existing authorit.
     *
     * @param authorit the authorit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorit,
     * or with status {@code 400 (Bad Request)} if the authorit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authorits")
    public ResponseEntity<Authorit> updateAuthorit(@Valid @RequestBody Authorit authorit) throws URISyntaxException {
        log.debug("REST request to update Authorit : {}", authorit);
        if (authorit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Authorit result = authoritService.save(authorit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorit.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /authorits} : get all the authorits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authorits in body.
     */
    @GetMapping("/authorits")
    public ResponseEntity<List<Authorit>> getAllAuthorits(Pageable pageable) {
        log.debug("REST request to get a page of Authorits");
        Page<Authorit> page = authoritService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authorits/:id} : get the "id" authorit.
     *
     * @param id the id of the authorit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authorit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/authorits/{id}")
    public ResponseEntity<Authorit> getAuthorit(@PathVariable Long id) {
        log.debug("REST request to get Authorit : {}", id);
        Optional<Authorit> authorit = authoritService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authorit);
    }

    /**
     * {@code DELETE  /authorits/:id} : delete the "id" authorit.
     *
     * @param id the id of the authorit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authorits/{id}")
    public ResponseEntity<Void> deleteAuthorit(@PathVariable Long id) {
        log.debug("REST request to delete Authorit : {}", id);
        authoritService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
