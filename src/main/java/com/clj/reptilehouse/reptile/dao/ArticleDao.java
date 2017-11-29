package com.clj.reptilehouse.reptile.dao;

import org.apache.ibatis.annotations.Param;

import com.clj.reptilehouse.common.PaginationCommonDao;
import com.clj.reptilehouse.reptile.entity.Article;

public interface ArticleDao extends PaginationCommonDao<Article>{
	
	public int insert(@Param(value="article")Article article);
	//public int update(Article article);	
	public Article selectById(String id);
}
