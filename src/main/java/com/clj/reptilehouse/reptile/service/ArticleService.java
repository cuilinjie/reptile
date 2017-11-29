package com.clj.reptilehouse.reptile.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clj.reptilehouse.common.page.PageService;
import com.clj.reptilehouse.reptile.dao.ArticleDao;
import com.clj.reptilehouse.reptile.entity.Article;

@Service
public class ArticleService implements PageService<Article> {
	
	@Autowired
	private ArticleDao dao;

	@Override
	public List<Article> listPagination(Map<String, Object> filter) {
		return dao.listPagination(filter);
	}

	@Override
	public int count(Map<String, Object> filter) {
		return dao.count(filter);
	}

	public int insert(Article article){
		return dao.insert(article);
	}
	
	/*public int update(Article article){
		return dao.update(article);
	}*/
	
	public Article selectById(String id){
		return dao.selectById(id);
	}
}
