package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.ProjectUser;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author kietdt
 * @created_date 22/04/2021
 */
@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Integer>,
    JpaSpecificationExecutor<ProjectUser> {

  List<ProjectUser> findByProjectIdAndUserIdInAndIsDeletedFalse(Integer projectId,
      Set<Integer> userIds);

  @Query("select projectUser.userId from ProjectUser as projectUser where projectUser.projectId =:projectId and projectUser.isDeleted = false ")
  Set<Integer> getUserIdByProjectId(Integer projectId);

  @Query(value = " Select p From ProjectUser p Where p.isDeleted = false And p.id = :id And p.projectId = :projectId")
  ProjectUser getProjectUser(@Param("id") Integer id, @Param("projectId") Integer projectId);
}
