package com.polytech.polysign.repository;

import com.polytech.polysign.domain.SignedFile;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SignedFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignedFileRepository extends JpaRepository<SignedFile, Long> {
}
