package com.ttt.mar.auth.repositories;

import com.ttt.rnd.lib.entities.Role;
import com.ttt.rnd.lib.repositories.RoleRepository;
import java.util.List;

/**
 * @author bontk
 * @created_date 17/08/2020
 */
public interface RoleExtendedRepository extends RoleRepository {

  List<Role> findAllByIsDeletedFalseAndTypeId(Integer type);

  List<Role> findAllByIsDeletedFalseAndTypeIdIn(List<Integer> types);
}
