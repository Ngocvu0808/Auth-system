package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.DistributeApi;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributeApiRepository
    extends JpaRepository<DistributeApi, Integer>, JpaSpecificationExecutor<DistributeApi> {

  DistributeApi findByIdAndIsDeletedFalse(Integer id);

  @Query(value = "SELECT DISTINCT(d.id) FROM DistributeApi d WHERE d.isDeleted = false AND d.status = :status AND d.id NOT IN :listId  ")
  Set<Integer> getListIdProjectDistributeByStatus(@Param("status") DistributeApiStatus status,
      @Param("listId") List<Integer> listId);

  @Query(value = "SELECT d FROM DistributeApi d WHERE d.id IN :listId  ")
  List<DistributeApi> getListProjectDistributeByStatus(@Param("listId") Set<Integer> listId);


  List<DistributeApi> findByIdInAndStatusAndIsDeletedFalse(Set<Integer> ids,
      DistributeApiStatus status);

  List<DistributeApi> findAllByStatusAndIsDeletedFalse(DistributeApiStatus status);

  List<DistributeApi> findAllByIsDeletedFalse();

  List<DistributeApi> findByIdInAndIsDeletedFalse(Set<Integer> ids);

  List<DistributeApi> findByMethodAndStatusAndIsDeletedFalse(ApiMethod method,
      DistributeApiStatus status);

}
