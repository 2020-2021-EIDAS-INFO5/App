package com.polytech.polysign.service;

import com.polytech.polysign.config.Constants;
import com.polytech.polysign.config.KeycloakConfig;
import com.polytech.polysign.domain.Authorit;
import com.polytech.polysign.domain.Organization;
import com.polytech.polysign.domain.SignOrder;
import com.polytech.polysign.domain.UserEntity;
import com.polytech.polysign.domain.UserKeycloak;
import com.polytech.polysign.repository.AuthoritRepository;
import com.polytech.polysign.repository.SignOrderRepository;
import com.polytech.polysign.repository.UserEntityRepository;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import liquibase.pro.packaged.o;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service Implementation for managing {@link UserEntity}.
 */
@Service
@Transactional
public class UserEntityService {

    private final Logger log = LoggerFactory.getLogger(UserEntityService.class);

    private final UserEntityRepository userEntityRepository;

    private final KeycloakAdminClientService kcAdminClient;

    private final OrganizationService organizationService;

    private final AuthoritService authoritService;

    private final AuthoritRepository authoritRepository;

    private final SignOrderRepository signOrderRepository;

    private final SignOrderService signOrderService;

    public UserEntityService(UserEntityRepository userEntityRepository, KeycloakAdminClientService kcAdminClient,
            OrganizationService organizationService, AuthoritService authoritService,
            AuthoritRepository authoritRepository, SignOrderRepository signOrderRepository,
            SignOrderService signOrderService) {
        this.userEntityRepository = userEntityRepository;
        this.kcAdminClient = kcAdminClient;
        this.organizationService = organizationService;
        this.authoritService = authoritService;
        this.authoritRepository = authoritRepository;
        this.signOrderRepository = signOrderRepository;
        this.signOrderService = signOrderService;
    }

    /**
     * Save a userEntity.
     *
     * @param userEntity the entity to save.
     * @return the persisted entity.
     */
    public UserEntity save(UserEntity userEntity) {
        log.debug("Request to save UserEntity : {}", userEntity);

        UserKeycloak userKeycloak = new UserKeycloak();

        userKeycloak.setEmail(userEntity.getEmail());

        userKeycloak.setFirstName(userEntity.getFirstname());

        userKeycloak.setPassword(new String(generatePassword(10))); // need password

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

        Optional<UserEntity> userOptional = userEntityRepository.findById(id);
        UserEntity userEntity = userOptional.get();
        String email = userEntity.getEmail();

        removeUser(email);
        userEntityRepository.deleteById(id);
    }

    /**
     * Delete the userEntity by id.
     *
     * @param id the id of the entity.
     */
    public void deleteTest(Long id, String username) {
        log.debug("Request to delete UserEntity : {}", id);

        Optional<UserEntity> userOptional = userEntityRepository.findById(id);
        UserEntity userEntity = userOptional.get();
        String email = userEntity.getEmail();
        List<Authorit> authorits = authoritRepository.findAll();
        for (Authorit authorit : authorits) {
            if (authorit.getOrganization() != null) {
                if (authorit.getUser() != null) {
                    if (authorit.getOrganization()
                            .equals(organizationService
                                    .findOrganizationByAuthorit(authoritService.findAdminAuhtoritByUsername(username)))
                            && authorit.getUser().getEmail().equals(userEntity.getEmail())) {
                        authoritService.delete(authorit.getId());
                    }
                }
            }
        }

        List<SignOrder> signOrders = signOrderRepository.findAll();

        for (SignOrder signOrder : signOrders) {
            if (signOrder.getSigner() != null) {
                if (signOrder.getSigner().getEmail().equals(email) && organizationService
                        .findOrganizationByAuthorit(
                                authoritService.findAdminAuhtoritByUsername(signOrder.getSignature().getCreator().getEmail()))
                        .equals(organizationService
                                .findOrganizationByAuthorit(authoritService.findAdminAuhtoritByUsername(username)))) {
                    Long orderSignID = signOrder.getId();
                    signOrderService.delete(orderSignID);
                }
            }
        }

        removeUser(email);
        userEntityRepository.deleteById(id);
    }

    private static char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$?/";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }

    public void removeUser(String userName) {

        Keycloak keycloak = KeycloakConfig.getInstance();
        UsersResource usersResource = KeycloakConfig.getInstance().realm(Constants.realm).users();
        String userId = keycloak.realm(Constants.realm).users().search(userName).get(0).getId();
        usersResource.delete(userId);

    }

}
