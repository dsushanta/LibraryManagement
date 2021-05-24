package com.bravo.johny.repository;

import com.bravo.johny.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<ConfigEntity, Integer> {
}
