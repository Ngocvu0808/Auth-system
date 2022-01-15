package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.service.ServiceTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServiceTagRepository extends JpaRepository<ServiceTag, Integer>,
    JpaSpecificationExecutor<ServiceTag> {

}
