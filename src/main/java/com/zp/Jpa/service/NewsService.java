package com.zp.Jpa.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zp.Jpa.entity.News;
import com.zp.Jpa.entity.search.NewsSearch;

public interface NewsService {
	
	News addNews(News news);// 增

	Integer deleteNews(Integer nid );// 删

	News saveNews(News news);// 改

	Page<News> queryNews(Integer page, Integer size, NewsSearch search);// 查
	
	List<News> findAll();
}
