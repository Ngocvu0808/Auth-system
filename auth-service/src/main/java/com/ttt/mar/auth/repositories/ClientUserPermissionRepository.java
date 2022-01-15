package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.application.ClientUserPermission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bontk
 * @created_date 01/08/2020
 */
public interface ClientUserPermissionRepository extends
    JpaRepository<ClientUserPermission, Integer> {

  List<ClientUserPermission> findAllByClientUserIdAndIsDeletedFalse(Integer clientUserId);

  Optional<ClientUserPermission> findByClientUserIdAndRoleIdAndIsDeletedFalse(Integer clientUserId, Integer roleId);

  List<ClientUserPermission> findByClientUserIdAndRoleIdInAndIsDeletedFalse(Integer clientUserId, List<Integer> roleIds);

  List<ClientUserPermission> findAllByClientUserIdInAndIsDeletedFalse(List<Integer> clientUserIds);
}
