package com.polytech.polysign.web.rest;

import com.polytech.polysign.domain.Organization;
import com.polytech.polysign.repository.OrganizationRepository;
import com.polytech.polysign.service.OrganizationService;
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
 * REST controller for managing {@link com.polytech.polysign.domain.Organization}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    private static final String ENTITY_NAME = "organization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationService organizationService;

    private final OrganizationRepository organizationRepository;

    public OrganizationResource(OrganizationService organizationService, OrganizationRepository organizationRepository) {
        this.organizationService = organizationService;
        this.organizationRepository = organizationRepository;
    }

    /**
     * {@code POST  /organizations} : Create a new organization.
     *
     * @param organization the organization to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organization, or with status {@code 400 (Bad Request)} if the organization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organizations")
    public ResponseEntity<Organization> createOrganization(@Valid @RequestBody Organization organization) throws URISyntaxException {
        log.debug("REST request to save Organization : {}", organization);
        if (organization.getId() != null) {
            throw new BadRequestAlertException("A new organization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Organization result = organizationService.save(organization);
        return ResponseEntity.created(new URI("/api/organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organizations} : Updates an existing organization.
     *
     * @param organization the organization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organization,
     * or with status {@code 400 (Bad Request)} if the organization is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organizations")
    public ResponseEntity<Organization> updateOrganization(@Valid @RequestBody Organization organization) throws URISyntaxException {
        log.debug("REST request to update Organization : {}", organization);
        if (organization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Organization result = organizationService.save(organization);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organization.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organizations} : get all the organizations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizations in body.
     */
    @GetMapping("/organizations")
    public ResponseEntity<List<Organization>> getAllOrganizations(Pageable pageable) {
        log.debug("REST request to get a page of Organizations");
        Page<Organization> page = organizationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organizations/:id} : get the "id" organization.
     *
     * @param id the id of the organization to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organization, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organizations/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable Long id) {
        log.debug("REST request to get Organization : {}", id);
        Optional<Organization> organization = organizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organization);
    }

    /**
     * {@code DELETE  /organizations/:id} : delete the "id" organization.
     *
     * @param id the id of the organization to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organizations/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        log.debug("REST request to delete Organization : {}", id);
        organizationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


      /**
     * {@code GET  /organizations/:username} : get the "username" organization.
     *
     * @param id the id of the organization to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organization, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organizations/all/{username}")
    public List<Organization> getAllMyOrganization(@PathVariable String username) {
        log.debug("REST request to get Organization : {}", username);
        List<Organization> myOrganizations = organizationService.getAllOrganizationsByUserName(username);
        return myOrganizations;
    }

    @GetMapping("/organizations/my/{username}")
    public List<Organization> getMyOrganization(@PathVariable String username) {
        log.debug("REST request to get Organization : {}", username);
        List<Organization> myOrganizations = organizationService.getMyOrganizationByUserName(username);
        return myOrganizations;
    }



            /**
     * {@code GET  /students/export} : Return a CSV containeing Students informations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK) and with body a String containg the student list in CSV}
     */
    @GetMapping("/organizations/export")
    public String exportAllUserEntities() {
        log.debug("GET request to export Students");
        String exportString = "\"name\",\"street_address\",\"postal_code\",\"city\",\"country\",\"vatNumber\"\n";
        List<Organization> organizations = organizationRepository.findAll();
        for (Organization organization: organizations)
            exportString += organization.toCSV() + "\n";
        return exportString;
    @GetMapping("/organizations/myUserAndAdmin/{username}")
    public List<Organization> getMyOrganizationUserAndAdmin(@PathVariable String username) {
        log.debug("REST request to get Organization : {}", username);
        List<Organization> myOrganizations = organizationService.getMyOrganizationByUserName(username);
        return myOrganizations;

    }
}
