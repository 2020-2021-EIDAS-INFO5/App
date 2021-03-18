package com.polytech.polysign.web.rest;

import com.polytech.polysign.domain.SignOrder;
import com.polytech.polysign.domain.UserEntity;
import com.polytech.polysign.service.SignOrderService;
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
 * REST controller for managing {@link com.polytech.polysign.domain.SignOrder}.
 */
@RestController
@RequestMapping("/api")
public class SignOrderResource {

    private final Logger log = LoggerFactory.getLogger(SignOrderResource.class);

    private static final String ENTITY_NAME = "signOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignOrderService signOrderService;



    public SignOrderResource(SignOrderService signOrderService) {
        this.signOrderService = signOrderService;
    }

    /**
     * {@code POST  /sign-orders} : Create a new signOrder.
     *
     * @param signOrder the signOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signOrder, or with status {@code 400 (Bad Request)} if the signOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sign-orders")
    public ResponseEntity<SignOrder> createSignOrder(@RequestBody SignOrder signOrder) throws URISyntaxException {
        log.debug("REST request to save SignOrder : {}", signOrder);
        if (signOrder.getId() != null) {
            throw new BadRequestAlertException("A new signOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignOrder result = signOrderService.save(signOrder);
        return ResponseEntity.created(new URI("/api/sign-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sign-orders} : Updates an existing signOrder.
     *
     * @param signOrder the signOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signOrder,
     * or with status {@code 400 (Bad Request)} if the signOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sign-orders")
    public ResponseEntity<SignOrder> updateSignOrder(@RequestBody SignOrder signOrder) throws URISyntaxException {
        log.debug("REST request to update SignOrder : {}", signOrder);
        if (signOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SignOrder result = signOrderService.save(signOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sign-orders} : get all the signOrders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signOrders in body.
     */
    @GetMapping("/sign-orders")
    public ResponseEntity<List<SignOrder>> getAllSignOrders(Pageable pageable) {
        log.debug("REST request to get a page of SignOrders");
        Page<SignOrder> page = signOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sign-orders/:id} : get the "id" signOrder.
     *
     * @param id the id of the signOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sign-orders/{id}")
    public ResponseEntity<SignOrder> getSignOrder(@PathVariable Long id) {
        log.debug("REST request to get SignOrder : {}", id);
        Optional<SignOrder> signOrder = signOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signOrder);
    }

    /**
     * {@code DELETE  /sign-orders/:id} : delete the "id" signOrder.
     *
     * @param id the id of the signOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sign-orders/{id}")
    public ResponseEntity<Void> deleteSignOrder(@PathVariable Long id) {
        log.debug("REST request to delete SignOrder : {}", id);
        signOrderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


      /**
     * {@code GET  /sign-orders} : get all the signOrders by username.
     *
     * @param pageable the pagination information.
     * @param username the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signOrders in body.
     */
  /*  @GetMapping("/sign-orders/ofUser/{username}")
    public ResponseEntity<List<SignOrder>> getAllSignOrdersOfUser(Pageable pageable, @PathVariable String username) {
        log.debug("REST request to get a page of SignOrders");
        Page<SignOrder> page = signOrderService.findAllSignatureOfSigner(pageable, username);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }*/


      /**
     * {@code GET  /sign-orders} : get all the signOrders by username.
     *
     * @param username the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signOrders in body.
     */
    @GetMapping("/sign-orders/ofUser/{username}")
    public List<SignOrder> getAllSignOrdersOfUserTest(@PathVariable String username) {
        log.debug("REST request to get a page of SignOrders");
        return  signOrderService.findAllSignatureOfSigner(username);
    }

    /**
     * {@code POST  /sign-orders} : Create a new signOrder.
     *
     * @param signOrder the signOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signOrder, or with status {@code 400 (Bad Request)} if the signOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sign-orders/createSignOrderForUserEntity/{signedFileID}/organizationID/{organizationID}")
    public ResponseEntity<SignOrder> createSignOrderForUserEntity(@RequestBody UserEntity userEntity, @PathVariable Long signedFileID, @PathVariable Long organizationID) throws URISyntaxException {

        SignOrder result = signOrderService.createSignOrderForUser(signedFileID, userEntity,organizationID);

        return ResponseEntity.created(new URI("/api/sign-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

}
