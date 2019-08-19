package com.zp.Jpa.service.Impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.News;
import com.zp.Jpa.entity.search.NewsSearch;
import com.zp.Jpa.repository.NewsRepository;
import com.zp.Jpa.service.NewsService;
import com.zp.Jpa.tools.StringUtils;
@Service
public class NewsServiceImpl implements NewsService{
	@Autowired
	 private NewsRepository newsRepository;
	@Override
	public News addNews(News news) {
		// TODO Auto-generated method stub
		return newsRepository.save(news);
	}

	@Override
	public Integer deleteNews(Integer nid) {
		// TODO Auto-generated method stub
		News news=newsRepository.findOne(nid);
		news.setIsDel(1);
		News news2=newsRepository.save(news);
		if(StringUtils.isEmpty(news2)) {return 1;}
		return 0;
	}

	@Override
	public News saveNews(News news) {
		// TODO Auto-generated method stub
		return newsRepository.save(news);
	}

	@Override
	public Page<News> queryNews(Integer page, Integer size, NewsSearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.DESC, "addTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return newsRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<News> getWhereClause(final NewsSearch search) {
		return new Specification<News>() {
			@Override
			public Predicate toPredicate(Root<News> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("addTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("addTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}

				if (search.getTitle() != null && !" ".equals(search.getTitle().trim())) {
					exList.add(cb.like(root.<String>get("title"), "%" + search.getTitle() + "%"));
				}
				
				
				if (search.getIsDel() != null ) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}

	@Override
	public List<News> findAll() {
		// TODO Auto-generated method stub
		return newsRepository.findAll();
	}

}
