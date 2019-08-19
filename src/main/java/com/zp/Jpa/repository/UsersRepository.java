package com.zp.Jpa.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zp.Jpa.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>,JpaSpecificationExecutor<Users>{
	

	
	public List<Users> findAll();
    public Users findByWeiXinFromOpenID(String WeiXinFromOpenID);
	public List<Users> findByRecommend(String Recommend);
	
	public Users findByUid(Integer uid);

	//设置
			@Query(value="INSERT INTO User_SysRole_tb (uid,role_id) VALUES (?1,?2)",nativeQuery=true)
			@Modifying
			@Transactional
			public int setUserSysRole(Integer roleId,Integer permissionId);
			
	
	}