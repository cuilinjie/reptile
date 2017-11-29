package com.clj.reptilehouse.common.timer;

import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.clj.reptilehouse.common.util.SystemUtil;

//import fund.database.*; 
public class LoginTimerTask extends TimerTask {
	private static boolean isRunning = false;
	private ServletContext context = null;

	public LoginTimerTask(ServletContext context) {
		this.context = context;
		
		SystemUtil.webAppRootKey = context.getInitParameter("webAppRootKey");
		
		System.out.println("SystemUtil.getRootPath():" + SystemUtil.getRootPath());
		System.out.println("SystemUtil.getRootPath1():" + SystemUtil.getRootPath1());
	}

	public void run() {
		if (!isRunning) {
			isRunning = true;
//			context.log("开始执行指定任务");

			// ———————————————————————————-

			// 在这里放上我们自己的代码
//			System.out.println("time running...");
			SystemUtil.CheckLoginUser();

			// ———————————————————————————-
			
			isRunning = false;
//			context.log("指定任务执行结束");
		} else {
			context.log("上一次任务执行还未结束");
		}
	}
}
