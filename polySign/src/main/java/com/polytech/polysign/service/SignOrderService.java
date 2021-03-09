package com.polytech.polysign.service;

import com.polytech.polysign.domain.SignOrder;
import com.polytech.polysign.repository.SignOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public SignOrderService(SignOrderRepository signOrderRepository) {
        this.signOrderRepository = signOrderRepository;
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
        List<SignOrder> mySignatures= new ArrayList<SignOrder>();
        List<SignOrder> allSignatures=  signOrderRepository.findAll();
        for(SignOrder signOrder : allSignatures){
            if(signOrder.getSigner()!=null){
                if(signOrder.getSigner().getEmail()!=null){
                    if(signOrder.getSigner().getEmail().equals(username)){
                        mySignatures.add(signOrder);
                    }
                }
            }
        }

        return mySignatures;
    }


}
