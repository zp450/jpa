package com.zp.Jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.LinkMan;


public interface LinkManRepository  extends JpaRepository<LinkMan, Integer>,JpaSpecificationExecutor<LinkMan>{

	List<LinkMan> findByParentId(Integer uid);

}
