package com.polytech.polysign.service;

import com.polytech.polysign.domain.SignOrder;
import com.polytech.polysign.domain.SignOrder_;
import com.polytech.polysign.domain.SignaturePlacement;
import com.polytech.polysign.domain.SignatureProcess;
import com.polytech.polysign.domain.UserEntity;
import com.polytech.polysign.repository.SignOrderRepository;
import com.polytech.polysign.repository.SignaturePlacementRepository;
import com.polytech.polysign.repository.SignatureProcessRepository;
import com.polytech.polysign.repository.SignedFileRepository;
import com.polytech.polysign.repository.UserEntityRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link SignaturePlacement}.
 */
@Service
@Transactional
public class SignaturePlacementService {

    private final Logger log = LoggerFactory.getLogger(SignaturePlacementService.class);

    private final SignaturePlacementRepository signaturePlacementRepository;

    private final SignatureProcessRepository signatureProcessRepository;

    private final UserEntityRepository userEntityRepository;

    private final SignOrderRepository signOrderRepository;

    public SignaturePlacementService(SignaturePlacementRepository signaturePlacementRepository,
            SignatureProcessRepository signatureProcessRepository, UserEntityRepository userEntityRepository,
            SignOrderRepository signOrderRepository) {
        this.signaturePlacementRepository = signaturePlacementRepository;
        this.signatureProcessRepository = signatureProcessRepository;
        this.userEntityRepository = userEntityRepository;
        this.signOrderRepository = signOrderRepository;
    }

    /**
     * Save a signaturePlacement.
     *
     * @param signaturePlacement the entity to save.
     * @return the persisted entity.
     */
    public SignaturePlacement save(SignaturePlacement signaturePlacement) {
        log.debug("Request to save SignaturePlacement : {}", signaturePlacement);
        return signaturePlacementRepository.save(signaturePlacement);
    }

    /**
     * Get all the signaturePlacements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SignaturePlacement> findAll() {
        log.debug("Request to get all SignaturePlacements");
        return signaturePlacementRepository.findAll();
    }

    /**
     * Get one signaturePlacement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SignaturePlacement> findOne(Long id) {
        log.debug("Request to get SignaturePlacement : {}", id);
        return signaturePlacementRepository.findById(id);
    }

    /**
     * Delete the signaturePlacement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SignaturePlacement : {}", id);
        signaturePlacementRepository.deleteById(id);
    }

    public SignaturePlacement saveSignaturePlacementForUserAndSignature(SignaturePlacement signaturePlacement,
            Long signFileID, String email) {

        List<SignatureProcess> signatureProcesses = signatureProcessRepository.findAll();
        SignatureProcess mySignatureProcess = null;
        for (SignatureProcess signatureProcess : signatureProcesses) {
            if (signatureProcess.getFinalFile() != null) {
                if (signatureProcess.getFinalFile().getId().equals(signFileID)) {
                    mySignatureProcess = signatureProcess;
                }
            }
        }

        Long signatureID = mySignatureProcess.getId();
        Long userEntityID = userEntityRepository.findByEmail(email).getId();

        SignOrder mySignOrder = new SignOrder();

        List<SignOrder> signOrders = signOrderRepository.findAll();

        for (SignOrder signOrder : signOrders) {
            if (signOrder.getSignature() != null && signOrder.getSigner() != null) {
                if (signOrder.getSignature().getId().equals(signatureID)
                        && signOrder.getSigner().getId().equals(userEntityID)) {
                    mySignOrder = signOrder;
                }
            }
        }
        signaturePlacement.setPlacement(mySignOrder);
        return signaturePlacementRepository.save(signaturePlacement);

    }

}
