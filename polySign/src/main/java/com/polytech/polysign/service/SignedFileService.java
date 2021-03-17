package com.polytech.polysign.service;

import com.polytech.polysign.domain.SignatureProcess;
import com.polytech.polysign.domain.SignedFile;
import com.polytech.polysign.domain.UserEntity;
import com.polytech.polysign.domain.enumeration.Status;
import com.polytech.polysign.repository.SignatureProcessRepository;
import com.polytech.polysign.repository.SignedFileRepository;
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
 * Service Implementation for managing {@link SignedFile}.
 */
@Service
@Transactional
public class SignedFileService {

    private final Logger log = LoggerFactory.getLogger(SignedFileService.class);

    private final SignedFileRepository signedFileRepository;

    private final UserEntityRepository userEntityRepository;

    private final SignatureProcessRepository signatureProcessRepository;

    public SignedFileService(SignedFileRepository signedFileRepository, UserEntityRepository userEntityRepository, SignatureProcessRepository signatureProcessRepository) {
        this.signedFileRepository = signedFileRepository;
        this.userEntityRepository = userEntityRepository;
        this.signatureProcessRepository = signatureProcessRepository;
    }

    /**
     * Save a signedFile.
     *
     * @param signedFile the entity to save.
     * @return the persisted entity.
     */
    public SignedFile save(SignedFile signedFile) {
        log.debug("Request to save SignedFile : {}", signedFile);
        return signedFileRepository.save(signedFile);
    }

    /**
     * Save a signedFile.
     *
     * @param signedFile the entity to save.
     * @return the persisted entity.
     */
    public SignedFile saveSignedFileAndSignatureProcess(SignedFile signedFile) {

            SignedFile signedFile1 = signedFileRepository.save(signedFile);
             // Get UserEntity
             UserEntity userEntity = userEntityRepository.findByFirstname(signedFile1.getFilename());
             // Create Signature Process
             SignatureProcess signatureProcess = new SignatureProcess();
             signatureProcess.setCreator(userEntity);
             signatureProcess.setEmissionDate(Instant.now());
             Instant emissionDate = signatureProcess.getEmissionDate();
             Instant expirationDate = emissionDate.plus(14, ChronoUnit.DAYS);
             signatureProcess.setExpirationDate(expirationDate);
    

             signatureProcess.finalFile(signedFile1);
             signatureProcess.setTitle(signedFile1.getFilename());
             signatureProcess.setOrderedSigning(false);
             signatureProcess.setStatus(Status.PENDING);
             signatureProcessRepository.save(signatureProcess);

             return signedFile1;

    }

    /**
     * Get all the signedFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SignedFile> findAll(Pageable pageable) {
        log.debug("Request to get all SignedFiles");
        return signedFileRepository.findAll(pageable);
    }

    /**
     * Get one signedFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SignedFile> findOne(Long id) {
        log.debug("Request to get SignedFile : {}", id);
        return signedFileRepository.findById(id);
    }

    /**
     * Delete the signedFile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SignedFile : {}", id);
        signedFileRepository.deleteById(id);
    }
}
