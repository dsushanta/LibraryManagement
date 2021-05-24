package com.bravo.johny.repository;

import com.bravo.johny.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserName(String UserName);

    @Nullable
    UserEntity findFirstByUserName(String UserName);

    @Nullable
    List<UserEntity> findByLastName(String lastName, Pageable pageable);

    @Nullable
    List<UserEntity> findByLastName(String lastName);

    @Nullable
    UserEntity findFirstByEmail(String userName);

    UserEntity deleteByUserName(String userName);
}