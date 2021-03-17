package com.polytech.polysign.repository;

import com.polytech.polysign.domain.SignaturePlacement;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SignaturePlacement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignaturePlacementRepository extends JpaRepository<SignaturePlacement, Long> {
}
