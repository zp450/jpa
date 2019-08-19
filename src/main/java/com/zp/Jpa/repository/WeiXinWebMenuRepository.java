package com.zp.Jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zp.Jpa.entity.WeiXinWebMenu;

public interface WeiXinWebMenuRepository  extends JpaRepository<WeiXinWebMenu, Integer>,JpaSpecificationExecutor<WeiXinWebMenu>{

}
