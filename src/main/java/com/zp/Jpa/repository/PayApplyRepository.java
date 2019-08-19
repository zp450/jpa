package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.PayApply;

public interface PayApplyRepository extends JpaRepository<PayApply, String>,JpaSpecificationExecutor<PayApply>{

	
}
