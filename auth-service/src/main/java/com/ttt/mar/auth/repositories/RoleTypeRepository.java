package com.ttt.mar.auth.repositories;

import com.ttt.rnd.lib.entities.RoleType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bontk
 * @created_date 17/08/2020
 */
public interface RoleTypeRepository extends JpaRepository<RoleType, Integer> {

  Optional<RoleType> findByCode(String code);

  List<RoleType> findAllByCodeInAndIsDeletedFalse(List<String> types);
}
