package com.polytech.polysign.service;

import com.polytech.polysign.domain.Auth;
import com.polytech.polysign.repository.AuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Auth}.
 */
@Service
@Transactional
public class AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Save a auth.
     *
     * @param auth the entity to save.
     * @return the persisted entity.
     */
    public Auth save(Auth auth) {
        log.debug("Request to save Auth : {}", auth);
        return authRepository.save(auth);
    }

    /**
     * Get all the auths.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Auth> findAll() {
        log.debug("Request to get all Auths");
        return authRepository.findAll();
    }


    /**
     * Get one auth by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Auth> findOne(Long id) {
        log.debug("Request to get Auth : {}", id);
        return authRepository.findById(id);
    }

    /**
     * Delete the auth by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Auth : {}", id);
        authRepository.deleteById(id);
    }
}
