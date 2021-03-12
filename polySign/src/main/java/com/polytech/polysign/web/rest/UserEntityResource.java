package com.polytech.polysign.web.rest;

import com.polytech.polysign.domain.Authorit;
import com.polytech.polysign.domain.Organization;
import com.polytech.polysign.domain.UserEntity;
import com.polytech.polysign.service.AuthoritService;
import com.polytech.polysign.service.OrganizationService;
import com.polytech.polysign.service.UserEntityService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.polytech.polysign.domain.UserEntity}.
 */
@RestController
@RequestMapping("/api")
public class UserEntityResource {

    private final Logger log = LoggerFactory.getLogger(UserEntityResource.class);

    private static final String ENTITY_NAME = "userEntity";

    private final AuthoritService authoritService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserEntityService userEntityService;

    public UserEntityResource(UserEntityService userEntityService, AuthoritService authoritService) {
        this.userEntityService = userEntityService;
        this.authoritService = authoritService;
    }

    /**
     * {@code POST  /user-entities} : Create a new userEntity.
     *
     * @param userEntity the userEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userEntity, or with status {@code 400 (Bad Request)} if the userEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-entities")
    public ResponseEntity<UserEntity> createUserEntity(@Valid @RequestBody UserEntity userEntity) throws URISyntaxException {
        log.debug("REST request to save UserEntity : {}", userEntity);
        if (userEntity.getId() != null) {
            throw new BadRequestAlertException("A new userEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserEntity result = userEntityService.save(userEntity);
        return ResponseEntity.created(new URI("/api/user-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-entities} : Updates an existing userEntity.
     *
     * @param userEntity the userEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userEntity,
     * or with status {@code 400 (Bad Request)} if the userEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-entities")
    public ResponseEntity<UserEntity> updateUserEntity(@Valid @RequestBody UserEntity userEntity) throws URISyntaxException {
        log.debug("REST request to update UserEntity : {}", userEntity);
        if (userEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserEntity result = userEntityService.save(userEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-entities} : get all the userEntities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userEntities in body.
     */
    @GetMapping("/user-entities")
    public ResponseEntity<List<UserEntity>> getAllUserEntities(Pageable pageable) {
        log.debug("REST request to get a page of UserEntities");
        Page<UserEntity> page = userEntityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-entities/:id} : get the "id" userEntity.
     *
     * @param id the id of the userEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-entities/{id}")
    public ResponseEntity<UserEntity> getUserEntity(@PathVariable Long id) {
        log.debug("REST request to get UserEntity : {}", id);
        Optional<UserEntity> userEntity = userEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userEntity);
    }

    /**
     * {@code DELETE  /user-entities/:id} : delete the "id" userEntity.
     *
     * @param id the id of the userEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-entities/{id}")
    public ResponseEntity<Void> deleteUserEntity(@PathVariable Long id) {
        log.debug("REST request to delete UserEntity : {}", id);
        userEntityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


        /**
     * {@code DELETE  /user-entities/:id} : delete the "id" userEntity.
     *
     * @param id the id of the userEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-entities/idToDelete{id}/username/{username}")
    public void deleteUserEntityByUsername(@PathVariable Long id, String username) {
        log.debug("REST request to delete UserEntity : {}", id);
        userEntityService.deleteTest(id, username);    }
}
