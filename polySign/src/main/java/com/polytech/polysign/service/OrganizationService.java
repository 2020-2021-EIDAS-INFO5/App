package com.polytech.polysign.service;

import com.polytech.polysign.domain.Authorit;
import com.polytech.polysign.domain.Organization;
import com.polytech.polysign.domain.enumeration.Role;
import com.polytech.polysign.repository.AuthoritRepository;
import com.polytech.polysign.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Organization}.
 */
@Service
@Transactional
public class OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationService.class);

    private final OrganizationRepository organizationRepository;

    private final AuthoritRepository authoritRepository;

    public OrganizationService(OrganizationRepository organizationRepository,AuthoritRepository authoritRepository) {
        this.organizationRepository = organizationRepository;
        this.authoritRepository=authoritRepository;
    }

    /**
     * Save a organization.
     *
     * @param organization the entity to save.
     * @return the persisted entity.
     */
    public Organization save(Organization organization) {
        log.debug("Request to save Organization : {}", organization);
        return organizationRepository.save(organization);
    }

    /**
     * Get all the organizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Organization> findAll(Pageable pageable) {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll(pageable);
    }


    /**
     * Get one organization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Organization> findOne(Long id) {
        log.debug("Request to get Organization : {}", id);
        return organizationRepository.findById(id);
    }
    

    /**
     * Delete the organization by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
    }



      /**
     * Get organizations by  username.
     *
     * @param username the username to get his organizations.
     * @return all the organizations of the user.
     */
    public List<Organization> getAllOrganizationsByUserName(String username) {
        log.debug("Request to get organization of : {}", username);
        //List<Organization>  myOrganizations = organizationRepository.findAll().stream().filter(organization-> authoritRepository.findAll().stream().filter(auth-> auth.getHasRole())
        List<Organization> myOrganizations = new ArrayList<Organization>();
        List<Authorit> authorits = authoritRepository.findAll();
        for(Authorit authority : authorits){
            if(authority.getUser() != null){
             if(authority.getUser().getEmail() != username){
                myOrganizations.add(authority.getOrganization());
            }
         }
        }
        return myOrganizations;
    }


          /**
     * Get organizations by  username.
     *
     * @param username the username to get his organizations.
     * @return the organizations of the user.
     */
    public List<Organization> getMyOrganizationByUserName(String username) {
        log.debug("Request to get organization of : {}", username);
        //List<Organization>  myOrganizations = organizationRepository.findAll().stream().filter(organization-> authoritRepository.findAll().stream().filter(auth-> auth.getHasRole())
        List<Organization> myOrganizations = new ArrayList<Organization>();
        List<Authorit> authorits = authoritRepository.findAll();
        for(Authorit authority : authorits){
            if(authority.getUser() != null){
             if(authority.getUser().getEmail() != username && authority.getHasRole()==Role.ADMIN_ORGANIZATION){
                myOrganizations.add(authority.getOrganization());
            }
         }
        }
        return myOrganizations;
    }

}
