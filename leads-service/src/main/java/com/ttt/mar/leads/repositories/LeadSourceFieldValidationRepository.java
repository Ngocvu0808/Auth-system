package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.LeadSourceFieldValidation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadSourceFieldValidationRepository extends
    JpaRepository<LeadSourceFieldValidation, Integer> {

}
