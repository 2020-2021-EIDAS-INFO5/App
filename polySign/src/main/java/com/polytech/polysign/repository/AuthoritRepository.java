package com.polytech.polysign.repository;

import com.polytech.polysign.domain.Authorit;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Authorit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthoritRepository extends JpaRepository<Authorit, Long> {
}
