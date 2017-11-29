package com.clj.reptilehouse.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;

public class CommonUtil {
	
	public static String getUUID32() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
	/**
	 * 将一个对象转换为一个Map
	 * @param obj
	 * @return
	 */
	public static Map<String, String> getMapByObject(Object obj) {
		Map<String, String> reMap = new HashMap<String, String>();
		if (obj == null)
			return null;
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {
				try {
					Field f = obj.getClass().getDeclaredField(
							fields[i].getName());
					f.setAccessible(true);
					Object o = f.get(obj);
					if (!StringUtil.isNullOrEmpty(o)) {
						reMap.put(fields[i].getName(), o.toString());
					}
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		if(reMap.containsKey("serialVersionUID")){
			reMap.remove("serialVersionUID");
		}
		return reMap;
	}

	/**
	 * 将一个Map转换为一个对象
	 * @param map
	 * @param object  需要转换的目标对象
	 * @return
	 */
	public static Object getObjectByMap(Map map,Object object) {
		
		Iterator iterator = map.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();

			try {
				BeanUtils.setProperty(object, key, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return object;
	}
}
