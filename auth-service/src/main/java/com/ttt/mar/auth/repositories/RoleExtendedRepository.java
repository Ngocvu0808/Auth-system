package com.ttt.mar.auth.repositories;

import com.ttt.rnd.lib.entities.Role;
import com.ttt.rnd.lib.repositories.RoleRepository;
import java.util.List;

public interface RoleExtendedRepository extends RoleRepository {

  List<Role> findAllByIsDeletedFalseAndTypeId(Integer type);

  List<Role> findAllByIsDeletedFalseAndTypeIdIn(List<Integer> types);
}
