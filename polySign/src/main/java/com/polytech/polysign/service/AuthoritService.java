package com.polytech.polysign.service;

import com.polytech.polysign.domain.Authorit;
import com.polytech.polysign.domain.enumeration.Role;
import com.polytech.polysign.repository.AuthoritRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Authorit}.
 */
@Service
@Transactional
public class AuthoritService {

    private final Logger log = LoggerFactory.getLogger(AuthoritService.class);

    private final AuthoritRepository authoritRepository;

    public AuthoritService(AuthoritRepository authoritRepository) {
        this.authoritRepository = authoritRepository;
    }

    /**
     * Save a authorit.
     *
     * @param authorit the entity to save.
     * @return the persisted entity.
     */
    public Authorit save(Authorit authorit) {
        log.debug("Request to save Authorit : {}", authorit);
        return authoritRepository.save(authorit);
    }

    /**
     * Get all the authorits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Authorit> findAll(Pageable pageable) {
        log.debug("Request to get all Authorits");
        return authoritRepository.findAll(pageable);
    }


    /**
     * Get one authorit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Authorit> findOne(Long id) {
        log.debug("Request to get Authorit : {}", id);
        return authoritRepository.findById(id);
    }



    /**
     * Get one authorit by usernameID.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Authorit findAdminAuhtoritByUsername(String username) {
        log.debug("Request to get Authorit : {}", username);
        List<Authorit> authorits=  authoritRepository.findAll();
        Authorit authority = new Authorit();
        for(Authorit authorit : authorits){
            if(authorit.getUser()!=null){
            if(authorit.getUser().getEmail().equals(username) && authorit.getHasRole()==Role.ADMIN_ORGANIZATION){
                authority = authorit;
            }
        }
        }

        return authority;
    }

    

    /**
     * Delete the authorit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Authorit : {}", id);
        authoritRepository.deleteById(id);
    }

}
