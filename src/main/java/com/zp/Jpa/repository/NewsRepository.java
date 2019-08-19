package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.News;

public interface NewsRepository extends JpaRepository<News, Integer>,JpaSpecificationExecutor<News>{

	

}

