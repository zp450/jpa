package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.AmbassadorPlayer;
import com.zp.Jpa.entity.search.AmbassadorPlayerSearch;

public interface AmbassadorPlayerService {
	AmbassadorPlayer addAmbassadorPlayer(AmbassadorPlayer ambassadorPlayer);// 增

	Integer deleteAmbassadorPlayer(Integer aPid );// 删

	AmbassadorPlayer saveAmbassadorPlayer(AmbassadorPlayer ambassadorPlayer);// 改

	Page<AmbassadorPlayer> queryAmbassadorPlayer(Integer page, Integer size, AmbassadorPlayerSearch search);// 查
	
	List<AmbassadorPlayer> findAll();
}
