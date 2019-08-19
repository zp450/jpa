package com.zp.Jpa.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zp.Jpa.entity.Equity;

public interface EquityRepository extends JpaRepository<Equity, Integer>,JpaSpecificationExecutor<Equity>{
	//@Query(value="SELECT * FROM [dbo].[Equity] where recommendid = 72 ", nativeQuery = true)
	List<Equity> findByRecommendId(Integer recommendId);
	@Query(value="SELECT sum(money) FROM [dbo].[Equity] ", nativeQuery = true)

	BigDecimal getCountMoney();
	List<Equity> findByUId(Integer uid);

	//查询父类
	
}
