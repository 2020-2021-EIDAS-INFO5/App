package com.polytech.polysign.repository;

import java.util.List;

import com.polytech.polysign.domain.User;
import com.polytech.polysign.domain.UserEntity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UserEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    public UserEntity findByFirstname(String username);
}
