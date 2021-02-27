package com.polytech.polysign.service;

import com.polytech.polysign.domain.SignedFile;
import com.polytech.polysign.repository.SignedFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SignedFile}.
 */
@Service
@Transactional
public class SignedFileService {

    private final Logger log = LoggerFactory.getLogger(SignedFileService.class);

    private final SignedFileRepository signedFileRepository;

    public SignedFileService(SignedFileRepository signedFileRepository) {
        this.signedFileRepository = signedFileRepository;
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
