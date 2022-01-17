package com.ttt.mar.config.repositories;

import com.ttt.mar.config.entities.SystemNavigator;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SystemNavigatorRepository extends JpaRepository<SystemNavigator, Integer>,
    JpaSpecificationExecutor<SystemNavigator> {

  List<SystemNavigator> findAllByIsDeletedFalse();
}
