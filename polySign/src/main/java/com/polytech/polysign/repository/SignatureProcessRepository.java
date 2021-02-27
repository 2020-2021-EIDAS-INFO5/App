package com.polytech.polysign.repository;

import com.polytech.polysign.domain.SignatureProcess;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SignatureProcess entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignatureProcessRepository extends JpaRepository<SignatureProcess, Long> {
}
