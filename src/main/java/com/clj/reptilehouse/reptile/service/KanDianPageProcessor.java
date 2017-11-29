package com.clj.reptilehouse.reptile.service;

import java.util.List;

import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import com.clj.reptilehouse.common.util.DateUtil;
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.reptile.entity.Article;

@Service
public class KanDianPageProcessor implements PageProcessor {

	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
	    Json json = page.getJson();
	    String currentUrl = page.getRequest().getUrl();
	    String currentPageNum = currentUrl.substring(currentUrl.indexOf("&page=")+6,currentUrl.indexOf("&size=")); 
	    String pageSize = json.jsonPath("result.data.number").get();
	    String totalNum = json.jsonPath("result.data.total_number").get();
	    int nextPageNum = Integer.parseInt(currentPageNum) + 1;
	    if(nextPageNum*Integer.parseInt(pageSize)<=Integer.parseInt(totalNum)){
	    	page.addTargetRequest(currentUrl.replace("&page="+currentPageNum, "&page="+nextPageNum));
	    }
	    List<String> list = json.jsonPath("result.data.articles[*]").all();
	    Article sourceArticle = null;
	    for(String article:list){
	    	JSONObject articleJson = JSONObject.fromObject(article);
	    	sourceArticle = new Article();
	    	sourceArticle.setId(IdWorker.nextId());
	    	sourceArticle.setSourceId(articleJson.getString("article_id"));
	    	sourceArticle.setCollectedNum(articleJson.getInt("collected_num"));
	    	sourceArticle.setCollectTime(DateUtil.long2Str(System.currentTimeMillis()));
	    	sourceArticle.setCommentNum(articleJson.getInt("comment_num"));
	    	sourceArticle.setLikedNum(articleJson.getInt("liked_num"));
	    	sourceArticle.setShortTitle(articleJson.getString("short_title"));
	    	sourceArticle.setSource("新浪看点");
	    	sourceArticle.setSourceAuthorId(articleJson.getString("author_uid"));
	    	sourceArticle.setSourceAuthorName(articleJson.getString("author_name"));
	    	sourceArticle.setSourcePublishTime(articleJson.getString("publish_time"));
	    	sourceArticle.setSourceUrl(articleJson.getString("pub_url"));
	    	sourceArticle.setTitle(articleJson.getString("title"));
	    	KanDianArticlePageProcessor.getInstance().addUrlAndStart(sourceArticle);
	    }
	}
	
	public void start() {
        Spider.create(this).addUrl("http://o.mpapi.sina.cn/article/listent?type=recommend&page=1&size=10").setExitWhenComplete(true).thread(1).run();
    }
}
