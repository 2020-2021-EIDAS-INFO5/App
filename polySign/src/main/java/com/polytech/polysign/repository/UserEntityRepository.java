package com.polytech.polysign.repository;

import com.polytech.polysign.domain.UserEntity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UserEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
}
