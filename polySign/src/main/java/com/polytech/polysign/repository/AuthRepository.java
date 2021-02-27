package com.polytech.polysign.repository;

import com.polytech.polysign.domain.Auth;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Auth entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
}
