package com.polytech.polysign.repository;

import com.polytech.polysign.domain.AuthenticatedUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AuthenticatedUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthenticatedUserRepository extends JpaRepository<AuthenticatedUser, Long> {
}
