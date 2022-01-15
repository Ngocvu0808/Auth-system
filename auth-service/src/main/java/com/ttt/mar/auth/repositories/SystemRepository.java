package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.service.System;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SystemRepository extends JpaRepository<System, Integer>,
    JpaSpecificationExecutor<System> {

}
