package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationRepository extends JpaRepository<Validation, Integer> {

  Validation findByCode(String code);
}
