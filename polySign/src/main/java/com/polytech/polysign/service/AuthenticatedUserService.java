package com.polytech.polysign.service;

import com.polytech.polysign.domain.AuthenticatedUser;
import com.polytech.polysign.repository.AuthenticatedUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AuthenticatedUser}.
 */
@Service
@Transactional
public class AuthenticatedUserService {

    private final Logger log = LoggerFactory.getLogger(AuthenticatedUserService.class);

    private final AuthenticatedUserRepository authenticatedUserRepository;

    public AuthenticatedUserService(AuthenticatedUserRepository authenticatedUserRepository) {
        this.authenticatedUserRepository = authenticatedUserRepository;
    }

    /**
     * Save a authenticatedUser.
     *
     * @param authenticatedUser the entity to save.
     * @return the persisted entity.
     */
    public AuthenticatedUser save(AuthenticatedUser authenticatedUser) {
        log.debug("Request to save AuthenticatedUser : {}", authenticatedUser);
        return authenticatedUserRepository.save(authenticatedUser);
    }

    /**
     * Get all the authenticatedUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuthenticatedUser> findAll(Pageable pageable) {
        log.debug("Request to get all AuthenticatedUsers");
        return authenticatedUserRepository.findAll(pageable);
    }


    /**
     * Get one authenticatedUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuthenticatedUser> findOne(Long id) {
        log.debug("Request to get AuthenticatedUser : {}", id);
        return authenticatedUserRepository.findById(id);
    }

    /**
     * Delete the authenticatedUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AuthenticatedUser : {}", id);
        authenticatedUserRepository.deleteById(id);
    }
}
