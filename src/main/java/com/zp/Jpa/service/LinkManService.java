package com.zp.Jpa.service;

import java.util.List;

import com.zp.Jpa.entity.LinkMan;

public interface LinkManService {
	LinkMan addLinkMan(LinkMan linkMan);
	List<LinkMan> findByParentId(Integer uid);
	LinkMan findOne(Integer lkId);
	void deleteLinkMan(Integer lmId);
}
