package com.ttt.mar.leads.repositories;

import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.entities.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepositories extends com.ttt.rnd.lib.repositories.UserRepository,
    JpaSpecificationExecutor<User> {

  List<User> findByIdInAndIsDeletedFalseAndStatus(Set<Integer> ids, UserStatus status);

  List<User> findByIdInAndIsDeletedFalse(Set<Integer> ids);

  Optional<User> findByIdAndIsDeletedIsFalse(Integer id);

}
