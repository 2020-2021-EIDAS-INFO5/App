package com.polytech.polysign.service;

import com.polytech.polysign.domain.SignatureProcess;
import com.polytech.polysign.repository.SignatureProcessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SignatureProcess}.
 */
@Service
@Transactional
public class SignatureProcessService {

    private final Logger log = LoggerFactory.getLogger(SignatureProcessService.class);

    private final SignatureProcessRepository signatureProcessRepository;

    public SignatureProcessService(SignatureProcessRepository signatureProcessRepository) {
        this.signatureProcessRepository = signatureProcessRepository;
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
