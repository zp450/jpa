package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.GroupCustomSumission;
import com.zp.Jpa.entity.search.GroupCustomSumissionSearch;

public interface GroupCustomSumissionService {
	GroupCustomSumission addGroupCustomSumission(GroupCustomSumission groupCustomSumission);// 增

	Integer deleteGroupCustomSumission(Integer gOid );// 删

	GroupCustomSumission saveGroupCustomSumission(GroupCustomSumission groupCustomSumission);// 改

	Page<GroupCustomSumission> queryGroupCustomSumission(Integer page, Integer size, GroupCustomSumissionSearch search);// 查
	
	List<GroupCustomSumission> findAll();
}
