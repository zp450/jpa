package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.Action;

public interface ActionRepository extends JpaRepository<Action, Integer>,JpaSpecificationExecutor<Action>{

}
