package com.polytech.polysign.web.rest;

import com.polytech.polysign.domain.Auth;
import com.polytech.polysign.service.AuthService;
import com.polytech.polysign.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.polytech.polysign.domain.Auth}.
 */
@RestController
@RequestMapping("/api")
public class AuthResource {

    private final Logger log = LoggerFactory.getLogger(AuthResource.class);

    private static final String ENTITY_NAME = "auth";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    /**
     * {@code POST  /auths} : Create a new auth.
     *
     * @param auth the auth to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auth, or with status {@code 400 (Bad Request)} if the auth has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/auths")
    public ResponseEntity<Auth> createAuth(@Valid @RequestBody Auth auth) throws URISyntaxException {
        log.debug("REST request to save Auth : {}", auth);
        if (auth.getId() != null) {
            throw new BadRequestAlertException("A new auth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Auth result = authService.save(auth);
        return ResponseEntity.created(new URI("/api/auths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /auths} : Updates an existing auth.
     *
     * @param auth the auth to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auth,
     * or with status {@code 400 (Bad Request)} if the auth is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auth couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/auths")
    public ResponseEntity<Auth> updateAuth(@Valid @RequestBody Auth auth) throws URISyntaxException {
        log.debug("REST request to update Auth : {}", auth);
        if (auth.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Auth result = authService.save(auth);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auth.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /auths} : get all the auths.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auths in body.
     */
    @GetMapping("/auths")
    public List<Auth> getAllAuths() {
        log.debug("REST request to get all Auths");
        return authService.findAll();
    }

    /**
     * {@code GET  /auths/:id} : get the "id" auth.
     *
     * @param id the id of the auth to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auth, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/auths/{id}")
    public ResponseEntity<Auth> getAuth(@PathVariable Long id) {
        log.debug("REST request to get Auth : {}", id);
        Optional<Auth> auth = authService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auth);
    }

    /**
     * {@code DELETE  /auths/:id} : delete the "id" auth.
     *
     * @param id the id of the auth to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/auths/{id}")
    public ResponseEntity<Void> deleteAuth(@PathVariable Long id) {
        log.debug("REST request to delete Auth : {}", id);
        authService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
