package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Integer>,JpaSpecificationExecutor<Group>{

	Group findByName(String name);

}

