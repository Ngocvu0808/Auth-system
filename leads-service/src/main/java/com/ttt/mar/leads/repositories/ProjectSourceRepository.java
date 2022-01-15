package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.ProjectSource;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProjectSourceRepository extends JpaRepository<ProjectSource, Integer>,
    JpaSpecificationExecutor<ProjectSource> {

  @Query("select projectSource.sourceId from ProjectSource as projectSource where projectSource.projectId =:projectId and projectSource.isDeleted = false ")
  Set<Integer> getSourceIdByProjectId(Integer projectId);

  Page<ProjectSource> findAllByProjectIdAndIsDeletedFalse(Integer projectId, Pageable var1);

  List<ProjectSource> findByProjectIdAndSourceIdInAndIsDeletedFalse(Integer projectId,
      Set<Integer> sourceIds);

  ProjectSource findByIdAndProjectIdAndIsDeletedFalse(Integer id, Integer projectId);

  List<ProjectSource> findByProjectIdAndIsDeletedFalse(Integer projectId);
}
