package com.clj.reptilehouse.common.util;

import java.io.IOException;
import java.util.Properties;

import org.springside.modules.utils.PropertiesLoader;

public class ConfigUtil
{
	static String propPostPath = "/config/post.properties";
	static String propConfPath = "/config/config.properties";
	static Properties propPost;
	static Properties propConf;
	static boolean inited = false;

	private static boolean init()
	{
		if( inited ){
			return true;
		}
		try {
			propPost = PropertiesLoader.loadProperties(propPostPath);
			propConf = PropertiesLoader.loadProperties(propConfPath);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		inited = true;
		return inited;
	}

	public static String getProp(String key)
	{
		if( !init() ){
			return null;
		}
		return propPost.getProperty(key);
	}
	
	public static String getConfProp(String key)
	{
		if( !init() ){
			return null;
		}
		return propConf.getProperty(key);
	}

	public static String getProp(String key, String defaultValue)
	{
		if( !init() ){
			return null;
		}
		return propPost.getProperty(key, defaultValue);
	}
}
