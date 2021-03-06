package com.polytech.polysign.service;

import com.polytech.polysign.domain.UserEntity;
import com.polytech.polysign.domain.UserKeycloak;
import com.polytech.polysign.repository.UserEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserEntity}.
 */
@Service
@Transactional
public class UserEntityService {

    private final Logger log = LoggerFactory.getLogger(UserEntityService.class);

    private final UserEntityRepository userEntityRepository;

    private final KeycloakAdminClientService kcAdminClient;


    public UserEntityService(UserEntityRepository userEntityRepository,KeycloakAdminClientService kcAdminClient) {
        this.userEntityRepository = userEntityRepository;
        this.kcAdminClient=kcAdminClient;
    }

    /**
     * Save a userEntity.
     *
     * @param userEntity the entity to save.
     * @return the persisted entity.
     */
    public UserEntity save(UserEntity userEntity) {
        log.debug("Request to save UserEntity : {}", userEntity);

        UserKeycloak userKeycloak =new UserKeycloak();

        userKeycloak.setEmail(userEntity.getEmail());

        userKeycloak.setFirstName(userEntity.getFirstname());

        userKeycloak.setPassword("1234"); //need password

        userKeycloak.setLastName(userEntity.getLastname());

        kcAdminClient.addUserTest(userKeycloak);

        return userEntityRepository.save(userEntity);
    }

    /**
     * Get all the userEntities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserEntity> findAll(Pageable pageable) {
        log.debug("Request to get all UserEntities");
        return userEntityRepository.findAll(pageable);
    }


    /**
     * Get one userEntity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserEntity> findOne(Long id) {
        log.debug("Request to get UserEntity : {}", id);
        return userEntityRepository.findById(id);
    }

    /**
     * Delete the userEntity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserEntity : {}", id);
        userEntityRepository.deleteById(id);
    }
}
