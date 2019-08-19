package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zp.Jpa.entity.UserCheck;

public interface UserCheckRepository extends JpaRepository<UserCheck, Integer>,JpaSpecificationExecutor<UserCheck>{
	@Query(value = "select TOP 1 * from usercheck where uid = ?1 order by checkTime desc",nativeQuery = true)
	UserCheck findByUidAndCheckTime(Integer uid);

	List<UserCheck> findByUid(Integer uid);

}
