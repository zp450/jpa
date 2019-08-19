package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.UserEmailTest;

public interface UserEmailTestRepository extends JpaRepository<UserEmailTest, Integer>,JpaSpecificationExecutor<UserEmailTest>{

	UserEmailTest findByCode(String code);
}
