package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.Offer;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfferRepository extends JpaRepository<Offer, Integer>,
    JpaSpecificationExecutor<Offer> {

  Offer findByCodeAndIsDeletedFalse(String code);

  Offer findByIdAndIsDeletedFalse(Integer id);

  List<Offer> findByIdNotInAndIsDeletedFalse(Set<Integer> ids);

  List<Offer> findByIsDeletedFalse();
}
