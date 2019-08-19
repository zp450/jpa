package com.zp.Jpa.service;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.WeiXinWebMenu;
import com.zp.Jpa.entity.search.WeiXinWebMenuSearch;

public interface WeiXinWebMenuService {
  WeiXinWebMenuService saveWeiXinWebMenu(WeiXinWebMenuService weiXinWebMenu);
   Page<WeiXinWebMenu> queryWeiXinWebMenu(Integer page, Integer size,WeiXinWebMenuSearch search);
}
