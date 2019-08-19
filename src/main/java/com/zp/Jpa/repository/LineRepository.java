package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.Lines;

public interface LineRepository extends JpaRepository<Lines, Integer>,JpaSpecificationExecutor<Lines>{

}
