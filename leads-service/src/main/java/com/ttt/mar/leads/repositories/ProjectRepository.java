package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.ProjectStatus;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ttt.mar.leads.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer>,
    JpaSpecificationExecutor<Project> {

  List<Project> findByCodeAndIsDeletedFalse(String code);

  Project findByIdAndIsDeletedFalse(Integer id);

  List<Project> findByStatusAndIsDeletedFalse(ProjectStatus status);

  Project findByIdAndStatusAndIsDeletedFalse(Integer id, ProjectStatus status);

  List<Project> findAllByIsDeletedFalse();
}
