package com.clj.reptilehouse.common.util;

import java.util.HashMap;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
/**
 * 排序类，通用于获取的对象的方法值 都是int类型
 * 
* @author jiangxs
 * 
*/
public final class SortUtils {

	 /**
     * 装载已经用过的规则 实现类似单例模式
     */
    private static Map<String, SortUtils> sortMap = new HashMap<String, SortUtils>();

    private Method[] methodArr = null;
    private int[] typeArr = null;

   /**
     * 构造函数 并保存该规则
     * 
    * @param clazz
     * @param args
     */
    private <T> SortUtils(Class<T> clazz, String... args) {
        methodArr = new Method[args.length];
        typeArr = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            String key = args[i].split("#")[0];
            try {
                methodArr[i] = clazz.getMethod(key, new Class[] {});
                typeArr[i] = Integer.valueOf(args[i].split("#")[1]);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

   /**
     * 排序规则
     * 
    * @author jiangxs 2012-03-13
     */
    private Comparator<Object> comparator = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            for (int i = 0; i < methodArr.length; i++) {
                try {
                    Object value1 = methodArr[i].invoke(o1);
                    Object value2 = methodArr[i].invoke(o2);
                    double double1 = 0;
                    double double2 = 0;

                   if (value1 instanceof Integer) {
                        double1 = (Integer) value1;
                        double2 = (Integer) value2;
                    } else if (value1 instanceof Boolean) {
                        double1 = (Boolean) value1 ? 1 : -1;
                        double2 = (Boolean) value2 ? 1 : -1;
                    } else if (value1 instanceof Double) {
                        double1 = (Double) value1;
                        double2 = (Double) value2;
                    } else if (value1 instanceof Float) {
                        double1 = (Float) value1;
                        double2 = (Float) value2;
                    } else if (value1 instanceof Long) {
                        double1 = (Long) value1;
                        double2 = (Long) value2;
                    } else {
                        double1 = value1.toString().compareToIgnoreCase(
                                value2.toString());
                        double2 = -double1;
                    }
                    if (double1 == double2) {
                        continue;
                    }
                    if (typeArr[i] == 1) {
                        return (double1 > double2) ? 1 : -1;
                    } else {
                        return (double1 > double2) ? -1 : 1;
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            return 0;
        }
    };

   /**
     * 获取排序规则 2012-3-13
     * 
    * @return SortUtils
     * @author jiangxs
     */
    private static <T> SortUtils getSort(Class<T> clazz, String... args) {
        String key = clazz.getName() + Arrays.toString(args);
        if (sortMap.containsKey(key)) {
            return sortMap.get(key);
        } else {
        	SortUtils sort = new SortUtils(clazz, args);
            sortMap.put(key, sort);
            return sort;
        }
    }

   /**
     * <pre>
     * 首先会在容器中，根据class+规则去找。如果没有见则new 
    * 调用方式 SortUtil.sort(list,"方法名#升序(1)/降序(-1)","..","..")
     * 后面字符串参数：比如："getMark#1","getAge#-1"
     * 表示先按照getMark的值按照升序排，如果相等再按照getAge的降序排
     * 如果返回值是true类型，若按照true先排："isOnline#1" ,若按照false先排："isOnline#-1"
     * </pre>
     * 
    * @author jiangxs 2012-3-13
     * @param list
     * @param args
     */
    public static <T> void sort(List<T> list, String... args) {
        if (list == null || list.size() == 0 || args.length == 0) {
            return;
        }
        SortUtils sort = getSort(list.get(0).getClass(), args);
        Collections.sort(list, sort.comparator);
    }

   /**
     * 给Map进行排序 对map的value进行排序
     * 
    * @author chenjy 2012-3-13
     * @param map
     *            被排序的map
     * @param args
     *            排序方法条件：方法名x#1升序-1倒序, 方法名y#-1倒序
     * @return List<T>
     */
    public static <T, F> List<F> sortMap(Map<T, F> map, String... args) {
        List<F> list = new ArrayList<F>();
        if (map == null || map.isEmpty()) {
            return list;
        }
        list.addAll(map.values());
        sort(list, args);
        return list;
    }
}
