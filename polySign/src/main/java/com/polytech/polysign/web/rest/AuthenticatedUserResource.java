package com.polytech.polysign.web.rest;

import com.polytech.polysign.domain.AuthenticatedUser;
import com.polytech.polysign.service.AuthenticatedUserService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.polytech.polysign.domain.AuthenticatedUser}.
 */
@RestController
@RequestMapping("/api")
public class AuthenticatedUserResource {

    private final Logger log = LoggerFactory.getLogger(AuthenticatedUserResource.class);

    private static final String ENTITY_NAME = "authenticatedUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthenticatedUserService authenticatedUserService;

    public AuthenticatedUserResource(AuthenticatedUserService authenticatedUserService) {
        this.authenticatedUserService = authenticatedUserService;
    }

    /**
     * {@code POST  /authenticated-users} : Create a new authenticatedUser.
     *
     * @param authenticatedUser the authenticatedUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authenticatedUser, or with status {@code 400 (Bad Request)} if the authenticatedUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authenticated-users")
    public ResponseEntity<AuthenticatedUser> createAuthenticatedUser(@RequestBody AuthenticatedUser authenticatedUser) throws URISyntaxException {
        log.debug("REST request to save AuthenticatedUser : {}", authenticatedUser);
        if (authenticatedUser.getId() != null) {
            throw new BadRequestAlertException("A new authenticatedUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthenticatedUser result = authenticatedUserService.save(authenticatedUser);
        return ResponseEntity.created(new URI("/api/authenticated-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /authenticated-users} : Updates an existing authenticatedUser.
     *
     * @param authenticatedUser the authenticatedUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authenticatedUser,
     * or with status {@code 400 (Bad Request)} if the authenticatedUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authenticatedUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authenticated-users")
    public ResponseEntity<AuthenticatedUser> updateAuthenticatedUser(@RequestBody AuthenticatedUser authenticatedUser) throws URISyntaxException {
        log.debug("REST request to update AuthenticatedUser : {}", authenticatedUser);
        if (authenticatedUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuthenticatedUser result = authenticatedUserService.save(authenticatedUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authenticatedUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /authenticated-users} : get all the authenticatedUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authenticatedUsers in body.
     */
    @GetMapping("/authenticated-users")
    public ResponseEntity<List<AuthenticatedUser>> getAllAuthenticatedUsers(Pageable pageable) {
        log.debug("REST request to get a page of AuthenticatedUsers");
        Page<AuthenticatedUser> page = authenticatedUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authenticated-users/:id} : get the "id" authenticatedUser.
     *
     * @param id the id of the authenticatedUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authenticatedUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/authenticated-users/{id}")
    public ResponseEntity<AuthenticatedUser> getAuthenticatedUser(@PathVariable Long id) {
        log.debug("REST request to get AuthenticatedUser : {}", id);
        Optional<AuthenticatedUser> authenticatedUser = authenticatedUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authenticatedUser);
    }

    /**
     * {@code DELETE  /authenticated-users/:id} : delete the "id" authenticatedUser.
     *
     * @param id the id of the authenticatedUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authenticated-users/{id}")
    public ResponseEntity<Void> deleteAuthenticatedUser(@PathVariable Long id) {
        log.debug("REST request to delete AuthenticatedUser : {}", id);
        authenticatedUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
