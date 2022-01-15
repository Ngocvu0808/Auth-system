package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author nguyen
 * @create_date 09/11/2021
 */
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {

}
