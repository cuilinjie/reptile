package com.clj.reptilehouse.reptile.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clj.reptilehouse.common.page.LigerUIPage;
import com.clj.reptilehouse.reptile.entity.Article;
import com.clj.reptilehouse.reptile.service.ArticleService;
import com.clj.reptilehouse.reptile.service.KanDianPageProcessor;

@Controller
@RequestMapping("reptile")
public class ReptileController {

	@Autowired
	private ArticleService service;
	@Autowired
	private KanDianPageProcessor processor;
	
	@RequestMapping("main")
	public String main(){
		return "reptile/main";
	}
	
	@RequestMapping("detail")
	public String main(String id,Model model){
		model.addAttribute("id", id);
		return "reptile/detail";
	}
	
	@RequestMapping("list")
	@ResponseBody
	public LigerUIPage<Article> list(@RequestParam(name="startTime",required=false)String startTime,
			@RequestParam(name="endTime",required=false)String endTime,
			@RequestParam(name="hasVideo",required=false)String hasVideo,
			@RequestParam(name="page")int pageNum,
			@RequestParam(name="pagesize")int pageSize){
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("startTime", startTime);
		filter.put("endTime", endTime);
		filter.put("hasVideo", hasVideo);
		filter.put("pageSize", pageSize);
		filter.put("offset", (pageNum-1)*pageSize);
		LigerUIPage<Article> ligerUIPage = new LigerUIPage<Article>();
		ligerUIPage.setPageService(service);
		ligerUIPage.build(filter);
		return ligerUIPage;
	}
	
	@RequestMapping("start")
	@ResponseBody
	public Map<String,String> start(){
		Map<String,String> result = new HashMap<String,String>();
		try {
			processor.start();
			result.put("result", "Y");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "N");
			result.put("errmsg", e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("getone")
	@ResponseBody
	public Article getOne(@RequestParam(name="id")String id){
	    return service.selectById(id);
	}
}
