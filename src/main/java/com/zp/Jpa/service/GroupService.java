package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.Group;
import com.zp.Jpa.entity.search.GroupSearch;



public interface GroupService {
	Group addGroup(Group group);// 增

	Group deleteGroup(Integer gid);// 删

	Group saveGroup(Group group);// 改

	Page<Group> queryGroup(Integer page, Integer size, GroupSearch search);// 查
	
	List<Group> findAll();
}
