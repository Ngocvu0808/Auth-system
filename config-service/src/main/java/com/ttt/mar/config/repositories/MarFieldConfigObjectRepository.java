package com.ttt.mar.config.repositories;

import com.ttt.mar.config.entities.MarFieldConfigObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarFieldConfigObjectRepository extends
    JpaRepository<MarFieldConfigObject, Integer> {

  MarFieldConfigObject findByCode(String code);
}
