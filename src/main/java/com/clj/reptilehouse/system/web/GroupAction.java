package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.tree.TreeEntity;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.entity.SysOrganization;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.OrganService;
import com.clj.reptilehouse.system.service.UserService;

@Controller
@RequestMapping(value = "/system/group")
public class GroupAction {
	private final String viewPath = "/system/member/group";
	private static Logger logger = LoggerFactory.getLogger(GroupAction.class);
	
	@Autowired
	private OrganService organService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter main.do ..." );
		return viewPath+"/main";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getGroup.do")
	public List<SysOrganization> getGroup(ModelMap map,HttpServletResponse response) throws IOException {
		logger.info( "Enter getGroup.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		List<SysOrganization> list = null;
		if( SystemUtil.isSuperUser(curUser)){
			list = organService.getAllOrgan();
		}else{
			SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
			if( userOrg != null ){
				list = organService.findOrganByCode(userOrg.getTreeCode());			
			}
		}
	    return list;         
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAllNodes.do")
	public List<TreeEntity> getAllNodes(ModelMap map,HttpServletResponse response) throws IOException {
		logger.info( "Enter getAllNodes.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
		List<SysOrganization> list=null;
		if( SystemUtil.isSuperUser(curUser)){
			list = organService.getAllOrgan();
		}else{
			if( userOrg != null ){
				list = organService.findOrganByCode(userOrg.getTreeCode());			
			}
		}
		List<SysUser> ulist = userService.findMngUserBy(userOrg.getTreeCode());
	    return merge(list,ulist);         
	}
	
	private List<TreeEntity> merge(List<SysOrganization> olist,List<SysUser> ulist){
		List<TreeEntity> nodes =new ArrayList<TreeEntity>();
		if(olist!=null&&!olist.isEmpty()){
			for(SysOrganization org:olist){
				TreeEntity tree=new TreeEntity();
				tree.setId(org.getId());
				tree.setName(org.getOrgName());
				tree.setPid(org.getParentId());
				tree.setType(0);
				tree.setTreeCode(org.getTreeCode());
				nodes.add(tree);
			}
			if(ulist!=null&&!ulist.isEmpty()){
				for(SysUser user:ulist){
					TreeEntity tree=new TreeEntity();
					tree.setId(user.getId());
					tree.setName(user.getUsername());
					tree.setPid(user.getOrgId());
					tree.setType(1);
					nodes.add(tree);
				}
			}
		}
		return nodes;
	}
	
	@ResponseBody
	@RequestMapping(value = "/add.do")
	public Map<String,Object> add(Long pid,String name, ModelMap map) {
		logger.info( "Enter add.do ..." );
		String result="success";
		Map<String,Object> rmap=new HashMap<String,Object>();
		if(organService.isOrgNameUnique(null,name)){
			SysOrganization organ = new SysOrganization();
			Long now = System.currentTimeMillis();
			organ.setModifiedTime(now);
			organ.setCreatedTime(now);
			organ.setParentId(pid);
			organ.setOrgName(name);
			organ.setStatus(Global.STATE_ON);
			organService.addOrgan(organ);
			rmap.put("entity", organ);
		}else{
			result="error";
			rmap.put("message","组织名称已经存在，请稍后添加！");
		}
		rmap.put("result",result);
		return rmap;
	}
	@ResponseBody
	@RequestMapping(value = "/update.do")
	public Map<String,Object> update(Long id,String name, ModelMap map) {
		logger.info( "Enter add.do ..." );
		String result="success";
		String message="添加成功！";
		Map<String,Object> rmap=new HashMap<String,Object>();
		if(organService.isOrgNameUnique(id,name)){
			SysOrganization organ=organService.get(id);
			Long now = System.currentTimeMillis();
			organ.setModifiedTime(now);
			organ.setOrgName(name);
			organService.saveOrgan(organ);
		}else{
			result="error";
			message="组织名称已经存在！";
		}
		rmap.put("message",message);
		rmap.put("result",result);
		return rmap;
	}
	
	@ResponseBody
	@RequestMapping(value="/delete.do")
	public void del(long id){
		organService.deleteOrgan(id);
		return ;
	}
}
