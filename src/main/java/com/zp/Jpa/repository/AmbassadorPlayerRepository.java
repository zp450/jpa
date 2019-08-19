package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.AmbassadorPlayer;

public interface AmbassadorPlayerRepository extends JpaRepository<AmbassadorPlayer, Integer>,JpaSpecificationExecutor<AmbassadorPlayer>{

}
