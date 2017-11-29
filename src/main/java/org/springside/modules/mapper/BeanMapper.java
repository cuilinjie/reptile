/**
 * Copyright (c) 2005-2011 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: BeanMapper.java,v 1.1 2012/10/30 05:50:38 jxs Exp $
 */
package org.springside.modules.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.
 * 
 * @author calvin
 */
public class BeanMapper {

	private BeanMapper() {
	}

	/**
	 * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
	 */
	private static DozerBeanMapper dozer = null;
	
	public static void initDozer() {
		if (dozer == null) {
			List<String> myMappingFiles = new ArrayList<String>();
			myMappingFiles.add("dozerBeanMapping.xml");

			dozer = new DozerBeanMapper();
			dozer.setMappingFiles(myMappingFiles);
		}
	}

	/**
	 * 基于Dozer转换对象的类型.
	 */
	public static <T> T map(Object source, Class<T> destinationClass) {
		if( dozer == null ) initDozer();
		return dozer.map(source, destinationClass);
	}

	/**
	 * 基于Dozer转换Collection中对象的类型.
	 */
	public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
		if( dozer == null ) initDozer();
		List<T> destinationList = Lists.newArrayList();
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	/**
	 * 基于Dozer将对象A的值拷贝到对象B中.
	 */
	public static void copy(Object source, Object destinationObject) {
		if( dozer == null ) initDozer();
		dozer.map(source, destinationObject);
	}
}