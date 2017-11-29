package com.clj.reptilehouse.reptile.service;

import java.util.HashMap;
import java.util.Map;

import org.springside.modules.utils.spring.SpringContextHolder;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Spider.Status;
import us.codecraft.webmagic.processor.PageProcessor;

import com.clj.reptilehouse.reptile.entity.Article;

public class KanDianArticlePageProcessor implements PageProcessor{
	
	private ArticleService service = SpringContextHolder.getBean(ArticleService.class);
	
	private Spider spider = null;
	
	private static KanDianArticlePageProcessor INSTANCE = new KanDianArticlePageProcessor();
	
	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	
	private Map<String,Article> articleMap = new HashMap<String,Article>();
	
	private KanDianArticlePageProcessor(){
		
	}
	
	public static KanDianArticlePageProcessor getInstance(){
		return INSTANCE;
	}
	
	@Override
	public void process(Page page) {
		String url = page.getRequest().getUrl();
		String partUrl = url.substring(0,url.indexOf(".html?"));
		String articleId = partUrl.substring(partUrl.lastIndexOf("_")+1);
		Article article = articleMap.get(articleId);
		if(article != null){
			String articleContent = page.getHtml().$("div#artibody").replace("<p><span style=\"font-family: KaiTi_GB2312,KaiTi;font-size:14px;\">.*</p>", "").get();
			article.setContent(articleContent);
			article.setHasVideo(article.getContent().indexOf("</video>")>=0?1:0);
			service.insert(article);
		}
	}

	@Override
	public Site getSite() {
		return site;
	}

	public synchronized void addUrlAndStart(Article article){
		if(spider == null){
			spider = Spider.create(INSTANCE);
		}
		this.spider.addUrl(article.getSourceUrl());
		articleMap.put(article.getSourceId(), article);
        if(this.spider.getStatus() != Status.Running){
			spider.setExitWhenComplete(true);
			spider.setEmptySleepTime(1000*60*5);
			spider.thread(10);
			spider.start();
		}
	}
}
