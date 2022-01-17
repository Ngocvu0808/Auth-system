package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.ProjectUserPermission;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectUserPermissionRepository extends
    JpaRepository<ProjectUserPermission, Integer>,
    JpaSpecificationExecutor<ProjectUserPermission> {

  @Query(value = " Select p From ProjectUserPermission p Where p.projectUserId = :id And p.isDeleted = false")
  List<ProjectUserPermission> getOneProjectUserPermission(@Param("id") Integer id);

  List<ProjectUserPermission> findByProjectUserIdInAndIsDeletedFalse(Set<Integer> ids);
}
