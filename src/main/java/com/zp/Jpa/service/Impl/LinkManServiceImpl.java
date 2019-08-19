package com.zp.Jpa.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.LinkMan;
import com.zp.Jpa.repository.LinkManRepository;
import com.zp.Jpa.service.LinkManService;
@Service
public class LinkManServiceImpl implements LinkManService{
	@Autowired
 private LinkManRepository linkManRepository;
	@Override
	public LinkMan addLinkMan(LinkMan linkMan) {
		// TODO Auto-generated method stub
		return linkManRepository.save(linkMan);
	}

	@Override
	public List<LinkMan> findByParentId(Integer uid) {
		// TODO Auto-generated method stub
		return linkManRepository.findByParentId(uid);
	}

	@Override
	public LinkMan findOne(Integer lkId) {
		// TODO Auto-generated method stub
		return linkManRepository.findOne(lkId);
	}

	@Override
	public void deleteLinkMan(Integer lmId) {
		// TODO Auto-generated method stub
		 linkManRepository.delete(lmId);;
	}

}
