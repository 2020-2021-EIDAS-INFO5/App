package com.polytech.polysign.service;

import com.polytech.polysign.domain.Authorit;
import com.polytech.polysign.domain.Organization;
import com.polytech.polysign.domain.SignOrder;
import com.polytech.polysign.domain.SignatureProcess;
import com.polytech.polysign.domain.SignedFile;
import com.polytech.polysign.domain.UserEntity;
import com.polytech.polysign.domain.enumeration.Role;
import com.polytech.polysign.repository.OrganizationRepository;
import com.polytech.polysign.repository.SignOrderRepository;
import com.polytech.polysign.repository.SignatureProcessRepository;
import com.polytech.polysign.repository.UserEntityRepository;
import com.polytech.polysign.web.rest.UserEntityResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Service Implementation for managing {@link SignOrder}.
 */
@Service
@Transactional
public class SignOrderService {

    private final Logger log = LoggerFactory.getLogger(SignOrderService.class);

    private final SignOrderRepository signOrderRepository;

    private final SignedFileService signedFileService;

    private final SignatureProcessRepository signatureProcessRepository;

    private final UserEntityRepository userEntityRepository;

    private final UserEntityService userEntityService;

    private final OrganizationService organizationService;

    private final AuthoritService authoritService;

    public SignOrderService(SignOrderRepository signOrderRepository, @Lazy SignedFileService signedFileService,
            SignatureProcessRepository signatureProcessRepository, UserEntityRepository userEntityRepository,
            @Lazy UserEntityService userEntityService,OrganizationService organizationService,AuthoritService authoritService) {
        this.signOrderRepository = signOrderRepository;
        this.signedFileService = signedFileService;
        this.signatureProcessRepository = signatureProcessRepository;
        this.userEntityRepository = userEntityRepository;
        this.userEntityService = userEntityService;
        this.organizationService= organizationService;
        this.authoritService = authoritService;
    }

    /**
     * Save a signOrder.
     *
     * @param signOrder the entity to save.
     * @return the persisted entity.
     */
    public SignOrder save(SignOrder signOrder) {
        log.debug("Request to save SignOrder : {}", signOrder);
        return signOrderRepository.save(signOrder);
    }

    /**
     * Get all the signOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SignOrder> findAll(Pageable pageable) {
        log.debug("Request to get all SignOrders");
        return signOrderRepository.findAll(pageable);
    }

    /**
     * Get one signOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SignOrder> findOne(Long id) {
        log.debug("Request to get SignOrder : {}", id);
        return signOrderRepository.findById(id);
    }

    /**
     * Delete the signOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SignOrder : {}", id);
        signOrderRepository.deleteById(id);
    }

    /**
     * Get the signOrders of user.
     *
     * @param pageable the pagination information.
     * @param username the username of the user
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SignOrder> findAllSignatureOfSigner(String username) {
        log.debug("Request to get all SignOrders");
        List<SignOrder> mySignatures = new ArrayList<SignOrder>();
        List<SignOrder> allSignatures = signOrderRepository.findAll();
        for (SignOrder signOrder : allSignatures) {
            if (signOrder.getSigner() != null) {
                if (signOrder.getSigner().getEmail() != null) {
                    if (signOrder.getSigner().getEmail().equals(username)) {
                        mySignatures.add(signOrder);
                    }
                }
            }
        }

        return mySignatures;
    }

    /**
     * Get the signOrders of user.
     *
     * @param pageable the pagination information.
     * @param username the username of the user
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public SignOrder createSignOrderForUser(Long signedFileID, UserEntity userEntity1, Long organizationID) {


    	

        String email = userEntity1.getEmail();
        UserEntity userEntity = userEntityRepository.findByEmail(email);
        
        if (userEntity == null) {
            userEntity = userEntityService.saveSignature(userEntity1);
        }

        //create role in oranization

        Organization organization = organizationService.findOne(organizationID).get();


        //create auhorit

        Authorit authorit =new Authorit();

        authorit.setOrganization(organization);

        authorit.setHasRole(Role.SIGNER);

        authorit.setUser(userEntity);

        //save authorit

        authoritService.save(authorit);

        SignedFile signedFile = signedFileService.findOne(signedFileID).get();


        //Create new Signed File for user

        SignedFile signedFileUser = new SignedFile();

        signedFileUser.setSigningDate(signedFile.getSigningDate());

        signedFileUser.setFileBytes(signedFile.getFileBytes());

        signedFileUser.setFilename(signedFile.getFilename());

        signedFileUser.setFileBytesContentType(signedFile.getFileBytesContentType());

        SignedFile signedFileUser1 = signedFileService.save(signedFileUser);

        SignOrder signOrder = new SignOrder();
        signOrder.setFile(signedFileUser1);

        SignatureProcess mySignature = new SignatureProcess();

        List<SignatureProcess> signatures = signatureProcessRepository.findAll();
        for (SignatureProcess signature : signatures) {

            if (signature.getFinalFile() != null) {
                if (signature.getFinalFile().getId().equals(signedFileID)) {
                    mySignature = signature;
                }
            }
        }
        signOrder.setSignature(mySignature);

        signOrder.setSigned(false);

        signOrder.setSigner(userEntity);

        SignOrder persistSignOrder = save(signOrder);

        mySignature.getSignOrders().add(persistSignOrder);

        return persistSignOrder;
    }
}
