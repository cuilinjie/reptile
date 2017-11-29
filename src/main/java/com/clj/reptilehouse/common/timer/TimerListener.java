package com.clj.reptilehouse.common.timer;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.clj.reptilehouse.common.timer.LoginTimerTask;

import java.util.*;

public class TimerListener implements ServletContextListener {
	private Timer timer = null;

	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		event.getServletContext().log("定时器销毁");
	}

	public void contextInitialized(ServletContextEvent event) {
		// 在这里初始化监听器，在tomcat启动的时候监听器启动
		timer = new Timer(true);
		event.getServletContext().log("定时器已启动");// 添加日志，可在tomcat日志（一般在localhost）中查看到

		timer.schedule(new LoginTimerTask(event.getServletContext()), new Date(), 15*1000);
	}
}
