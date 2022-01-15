package com.ttt.mar.leads.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttt.mar.leads.entities.ProjectDistribute;


@Repository
public interface ProjectDistributeRepository
    extends JpaRepository<ProjectDistribute, Integer>, JpaSpecificationExecutor<ProjectDistribute> {

  @Query(value = "SELECT p.id FROM ProjectDistribute p WHERE p.isDeleted = false AND p.projectId = :projectId")
  List<Integer> getListProjectDistributeExist(@Param("projectId") Integer projectId);

  List<ProjectDistribute> findByProjectIdAndDistributeApiIdInAndIsDeletedFalse(Integer projectId,
      Set<Integer> distributeIds);

  @Query("select projectDistribute.distributeId from ProjectDistribute as projectDistribute where projectDistribute.projectId =:projectId and projectDistribute.isDeleted = false ")
  Set<Integer> getDistributeIdByProjectId(Integer projectId);

  ProjectDistribute findByIdAndProjectIdAndIsDeletedFalse(Integer id, Integer projectId);

  List<ProjectDistribute> findByProjectIdAndIsDeletedFalse(Integer projectId);
}
