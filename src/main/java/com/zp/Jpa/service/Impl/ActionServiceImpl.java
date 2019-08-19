package com.zp.Jpa.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.Action;
import com.zp.Jpa.repository.ActionRepository;
import com.zp.Jpa.service.ActionService;
@Service
public class ActionServiceImpl implements ActionService{

	@Autowired
	private ActionRepository actionRepository;
	@Override
	public Action addAction(Action action) {
		// TODO Auto-generated method stub
		return actionRepository.save(action);
	}

	@Override
	public void deleteAction(Integer aid) {
		// TODO Auto-generated method stub
		actionRepository.delete(aid);
	}

	@Override
	public List<Action> queryAction() {
		// TODO Auto-generated method stub
		return actionRepository.findAll();
	}

}
