package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.service.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

  Tag findByTag(String name);
}
