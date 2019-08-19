package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.GroupCustomSumission;

public interface GroupCustomSumissionRepository extends JpaRepository<GroupCustomSumission, Integer>,JpaSpecificationExecutor<GroupCustomSumission>{

}
