package com.ttt.mar.email.repositories;

import com.ttt.mar.email.entities.TypeSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TypeSendRepository extends JpaRepository<TypeSend, Integer>,
    JpaSpecificationExecutor<TypeSendRepository> {


}
