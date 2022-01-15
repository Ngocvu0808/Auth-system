package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author nguyen
 * @create_date 08/09/2021
 */

public interface ScheduleRepository extends JpaRepository<Schedule, Integer>,
    JpaSpecificationExecutor<Schedule> {

  Schedule findByIdAndIsDeletedFalse(Integer id);

  Schedule findByNameAndIsDeletedIsFalse(String name);

}
