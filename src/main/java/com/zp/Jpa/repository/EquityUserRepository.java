package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.equity.EquityUser;

public interface EquityUserRepository extends JpaRepository<EquityUser, Integer>,JpaSpecificationExecutor<EquityUser>{

	

	List<EquityUser> findByRecommendId(Integer recommendId);

	EquityUser findByUId(Integer uid);

	

}
