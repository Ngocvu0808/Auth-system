package com.ttt.mar.notify.repositories.template;

import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.entities.UserStatus;
import java.util.List;
import java.util.Set;

public interface UserRepositories extends com.ttt.rnd.lib.repositories.UserRepository {

  List<User> findByIdInAndIsDeletedFalseAndStatus(Set<Integer> ids, UserStatus status);

  List<User> findByIdInAndIsDeletedFalse(Set<Integer> ids);
}
