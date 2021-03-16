package com.polytech.polysign.service;

import com.polytech.polysign.domain.SignatureProcess;
import com.polytech.polysign.domain.SignedFile;
import com.polytech.polysign.domain.UserEntity;
import com.polytech.polysign.domain.enumeration.Status;
import com.polytech.polysign.repository.SignatureProcessRepository;
import com.polytech.polysign.repository.UserEntityRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Service Implementation for managing {@link SignatureProcess}.
 */
@Service
@Transactional
public class SignatureProcessService {

    private final Logger log = LoggerFactory.getLogger(SignatureProcessService.class);

    private final SignatureProcessRepository signatureProcessRepository;
    
    private final SignedFileService signedFileService;

    private final UserEntityRepository userEntityRepository;

    public SignatureProcessService(SignatureProcessRepository signatureProcessRepository,SignedFileService signedFileService,UserEntityRepository userEntityRepository) {
        this.signatureProcessRepository = signatureProcessRepository;
        this.signedFileService=signedFileService;
        this.userEntityRepository=userEntityRepository;
    }

    /**
     * Save a signatureProcess.
     *
     * @param signatureProcess the entity to save.
     * @return the persisted entity.
     */
    public SignatureProcess save(SignatureProcess signatureProcess) {
        log.debug("Request to save SignatureProcess : {}", signatureProcess);
        return signatureProcessRepository.save(signatureProcess);
    }


     /**
     * Save a signatureProcess.
     *
     * @param signatureProcess the entity to save.
     * @return the persisted entity.
     */
    public void saveSignatureProcess(Long signedFileId, String username) {

        log.debug("Request to create and save SignatureProcess : {}", signedFileId, username);
        //Get UserEntity
        UserEntity userEntity = userEntityRepository.findByFirstname(username);
        //Create Signature Process
        SignatureProcess signatureProcess = new SignatureProcess();
        signatureProcess.setCreator(userEntity);
        signatureProcess.setEmissionDate(Instant.now());
        Instant emissionDate = signatureProcess.getEmissionDate();
        Instant expirationDate = emissionDate.plus(14, ChronoUnit.DAYS); 
        signatureProcess.setExpirationDate(expirationDate);

        //Get file associated to signature
        Optional<SignedFile> signedFiles = signedFileService.findOne(signedFileId);
        SignedFile signedFile = signedFiles.get();


        signatureProcess.finalFile(signedFile);
        signatureProcess.setTitle(signedFile.getFilename());
        signatureProcess.setOrderedSigning(false);
        signatureProcess.setStatus(Status.PENDING);
        signatureProcessRepository.save(signatureProcess);

    }


    /**
     * Get all the signatureProcesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SignatureProcess> findAll(Pageable pageable) {
        log.debug("Request to get all SignatureProcesses");
        return signatureProcessRepository.findAll(pageable);
    }


    /**
     * Get one signatureProcess by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SignatureProcess> findOne(Long id) {
        log.debug("Request to get SignatureProcess : {}", id);
        return signatureProcessRepository.findById(id);
    }

    /**
     * Delete the signatureProcess by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SignatureProcess : {}", id);
        signatureProcessRepository.deleteById(id);
    }
}
