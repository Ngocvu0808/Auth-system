package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.ScheduleValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author nguyen
 * @create_date 08/09/2021
 */
public interface ScheduleValueRepository extends JpaRepository<ScheduleValue, Integer>,
    JpaSpecificationExecutor<ScheduleValue> {

  ScheduleValue findByScheduleIdAndIsDeletedFalse(Integer id);
}
