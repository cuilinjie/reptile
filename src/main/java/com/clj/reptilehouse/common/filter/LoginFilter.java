package com.clj.reptilehouse.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.entity.SysUser;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse rep,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response=(HttpServletResponse)rep;
	    SysUser curUser = SystemUtil.getLoginUser();
	    String url=request.getRequestURL().toString();
		if(curUser==null&&!url.contains("login.do")){
			String redurl=request.getContextPath();
			redurl =redurl.concat(Global.LOGIN_URL);
			// 如果是ajax请求响应头会有，x-requested-with；  
			if(url.indexOf("/reptile")>0){
           	    chain.doFilter(request, response);
           }else{
           	    response.sendRedirect(redurl);
           	    return;
           }
		}else{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
	
}
