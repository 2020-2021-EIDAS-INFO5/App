package com.polytech.polysign.repository;

import com.polytech.polysign.domain.SignOrder;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SignOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignOrderRepository extends JpaRepository<SignOrder, Long> {
}
