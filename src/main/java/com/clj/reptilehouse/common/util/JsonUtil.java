package com.clj.reptilehouse.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.orm.Page;
import org.springside.modules.utils.Reflections;

import com.clj.reptilehouse.common.page.PageInfo;
import com.clj.reptilehouse.common.tree.TreeUtils;
import com.clj.reptilehouse.common.util.WebFuncUtil;
import com.clj.reptilehouse.system.entity.SysUser;

public class JsonUtil {

//	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	private static JsonMapper binder = JsonMapper.buildNormalMapper();
	
	// 以下是常见json返回值
	public final static String JSON_RESULT_SUCCESS = "{\"result\":\"success\"}"; 	
	public final static String JSON_RESULT_FALSE = "{\"result\":\"false\"}";	
	public final static String JSON_RESULT_FALSE_MSGMDL = "{\"result\":\"false\", \"message\":\"%s\"}";	
	public final static String JSON_RESULT_SUCCESS_MSGMDL = "{\"result\":\"success\", \"message\":\"%s\"}";	
	public final static String JSON_RESULT_OUTTIME = "{\"result\":\"outtime\"}";	

	public static String object2json(Object obj) {
		return binder.toJson(obj);
	}

	public static String bean2json(Object obj) {
		return binder.toJson(obj);
	}

	public static String list2json(List<?> list) {
		return binder.toJson(list);
	}

	public static String array2json(Object[] array) {
		return binder.toJson(array);
	}

	public static String map2json(Map<?, ?> map) {
		return binder.toJson(map);
	}

	public static String set2json(Set<?> set) {
		return binder.toJson(set);
	}
	
	public static String propToJson(Properties props)
	{
		return binder.toJson(props);
	}
	/**
	 * json转map
	 * @author hlz
	 * @date 2016年1月11日
	 *
	 */
	public static Map<?,?> json2Map(String json) {
		return binder.jsonToMap(json);
	}
	/**
	 * 根据key 获取
	 * @author hlz
	 * @date 2016年2月18日
	 *
	 */
	public static Object  getJsonValue(String jsonStr,String key){
        Object rulsObj=null;
        Map<?,?> rulsMap=json2Map(jsonStr);
        if(rulsMap!=null&&rulsMap.size()>0){
            rulsObj=rulsMap.get(key);
        }
        return rulsObj;
    }

	public static <T> List<T> getDTOList(String jsonString, Class<T> clazz) {
		List<T> list = binder.fromJson(jsonString, binder.constructParametricType(ArrayList.class,clazz));
		return list;
	}
	
	public static String getObjAttName(final String jsonAttName, final String objAttNames, final String jsonAttNames) {
		String[] objNames = objAttNames.split(",");
		String[] jsonNames = jsonAttNames.split(",");
		for( int i=0; i<jsonNames.length; i++ ){
			if( jsonNames[i].equals(jsonAttName) ){
				String[] segNames = objNames[i].split("@");
				return segNames[0];
			}
		}
		return jsonAttName;
	}

	public static String getJsonColValue(Object obj, String colStr )
	{
		String[] colAtts = colStr.split("@");
		Object objCol = Reflections.getFieldValue(obj, colAtts[0]);
		
		if( colAtts.length == 1){
			return binder.toJson(objCol);
		}

		String value = null;
		if( objCol != null){
			switch(colAtts.length){
			case 2:
				value = WebFuncUtil.getResValue(colAtts[1], objCol.toString());
				break;
			case 4:
				value = WebFuncUtil.getObjColValue(colAtts[1], colAtts[2], colAtts[3], objCol.toString());
				break;
			}		
		}
		
		return binder.toJson(value);
	}

	
	// 转换树对象列表为json树信息
	// 参数nodeAttNames为指定要转换的树节点属性，以逗号间隔
	// 参数jsonAttNames为指定转换属性对应的json属性名称，个数及次序必须与nodeAttNames保持一致
	@SuppressWarnings("rawtypes")
	public static String getJsonTreeInfo(List listNode, String nodeAttNames, String jsonAttNames, String childName)
	{
		return getJsonTreeInfo(listNode, nodeAttNames, jsonAttNames, childName, TreeUtils.TREE_DEFAULT_COL_NAME, TreeUtils.TREE_DEFAULT_LEVEL_LENGTH);
	}
	
	// 转换树对象列表为json树信息
	// 参数nodeAttNames为指定要转换的树节点属性，以逗号间隔
	// 参数jsonAttNames为指定转换属性对应的json属性名称，个数及次序必须与nodeAttNames保持一致
	@SuppressWarnings("rawtypes")
	public static String getJsonTreeInfo(List listNode, String nodeAttNames, String jsonAttNames, String childName, String treeCode, int levelLength)
	{
		if( listNode==null ){
			return "[]";
		}
		String[] nodeAtts = nodeAttNames.split(",");
		String[] jsonAtts = (jsonAttNames==null || "".equals(jsonAttNames) ) ? nodeAtts:jsonAttNames.split(",");

		int attNum = nodeAtts.length;
		
		String lastTreeCode = "";
		String curTreeCode = "";
		int nChild = 0;
		int nodeNum = listNode.size();
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (int i = 0; i < nodeNum; i++)
		{
			curTreeCode = Reflections.getFieldValue(listNode.get(i), treeCode).toString();
			if( i>0 ){
				if( curTreeCode.length()>lastTreeCode.length()){
					sb.append(", \"").append(childName).append("\": [ ");
					nChild++;
				}
				else if( curTreeCode.length()<lastTreeCode.length() ){
					int nLevel = (lastTreeCode.length()-curTreeCode.length())/levelLength;
					for( int k=0; k<nLevel; k++ ){
						sb.append("} ]");
						nChild--;
					}
					sb.append(" }, ");
				}
				else{
					sb.append(" }, ");
				}
			}
			for( int j=0; j<attNum; j++ )
			{
				String value = getJsonColValue(listNode.get(i), nodeAtts[j]);
				if( j==0 ){
					sb.append("{ \"");
				}
				else{
					sb.append(", \"");
				}
				sb.append(jsonAtts[j]).append("\":").append(value);
			}
			lastTreeCode = curTreeCode;			
		}
		while (nChild > 0) {
			sb.append(" } ]");
			nChild--;
		}
		if( nodeNum>0 ){
			sb.append(" }");			
		}	
		sb.append(" ]");
		
		return sb.toString();
	}
	
	public static String getJsonObjInfo(Object obj, String[] objAtts, String[] jsonAtts)
	{
		int nAtt = objAtts.length;
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		for( int i=0; i<nAtt; i++ ){
			String value = getJsonColValue(obj, objAtts[i]);
			if( i>0 ){
				sb.append(", ");
			}
			sb.append("\"").append(jsonAtts[i]).append("\":").append(value);			
		}
		sb.append(" }");
		return sb.toString();
	}
	
	public static String getJsonObjInfo(Object obj, String ObjAttNames, String jsonAttNames)
	{
		String[] objAtts = ObjAttNames.split(",");
		String[] jsonAtts = (jsonAttNames==null || "".equals(jsonAttNames) ) ? objAtts:jsonAttNames.split(",");
		for( int i=0; i<jsonAtts.length; i++ ){
			int n = jsonAtts[i].indexOf("@");
			if( n>0 ){
				jsonAtts[i] = jsonAtts[i].substring(0, n);
			}
		}

		return getJsonObjInfo(obj, objAtts, jsonAtts);
	}
	
	@SuppressWarnings("rawtypes")
	public static String getJsonListInfo(List listObj, String jsonName)
	{
		if( listObj==null ){
			return "[]";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		int nodeNum = listObj.size();
		for (int i=0; i < nodeNum; i++)
		{
			if( i>0 ){
				sb.append(", ");
			}
			sb.append("{\"").append(jsonName).append("\":").append(binder.toJson(listObj.get(i))).append("}");
		}
		sb.append(" ]");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getJsonListInfo(List listObj, String nodeAttNames, String jsonAttNames)
	{
		if( listObj==null ){
			return "[]";
		}
		String[] nodeAtts = nodeAttNames.split(",");
		String[] jsonAtts = (jsonAttNames==null || "".equals(jsonAttNames) ) ? nodeAtts:jsonAttNames.split(",");
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		int nodeNum = listObj.size();
		for (int i=0; i < nodeNum; i++)
		{
			if( i>0 ){
				sb.append(", ");
			}
			sb.append(getJsonObjInfo(listObj.get(i), nodeAtts, jsonAtts));
		}
		sb.append(" ]");
		return sb.toString();
	}

	public static<T> String getJsonPageInfo(Page<T> page, String nodeAttNames, String jsonAttNames)
	{
		if( page==null ){
			return "{}";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"pageNo\":").append(page.getPageNo());
		sb.append(", \"pageSize\":").append(page.getPageSize());
		sb.append(", \"orderBy\":\"").append(page.getOrderBy()).append("\"");
		sb.append(", \"orderDir\":\"").append(page.getOrderDir()).append("\"");
		sb.append(", \"totalItems\":").append(page.getTotalItems());		
		sb.append(", \"result\":").append(getJsonListInfo(page.getResult(), nodeAttNames, jsonAttNames));
		sb.append(" }");
		return sb.toString();
	}
	
	public static<T> String getNewJsonPageInfo(PageInfo<T> page, String nodeAttNames, String jsonAttNames)
	{
		if( page==null ){
			return "{}";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"pageNum\":").append(page.getPageNum());
		sb.append(", \"pageSize\":").append(page.getPageSize());
		sb.append(", \"size\":").append(page.getSize());
		sb.append(", \"startRow\":").append(page.getStartRow());
		sb.append(", \"endRow\":").append(page.getEndRow());
		sb.append(", \"total\":").append(page.getTotal());
		sb.append(", \"pages\":").append(page.getPages());
		sb.append(", \"firstPage\":").append(page.getFirstPage());
		sb.append(", \"prePage\":").append(page.getPrePage());
		sb.append(", \"nextPage\":").append(page.getNextPage());
		sb.append(", \"lastPage\":").append(page.getLastPage());
		sb.append(", \"isFirstPage\":").append(page.isIsFirstPage());
		sb.append(", \"isLastPage\":").append(page.isIsLastPage());
		sb.append(", \"hasPreviousPage\":").append(page.isHasPreviousPage());
		sb.append(", \"hasNextPage\":").append(page.isHasNextPage());
		sb.append(", \"navigatePages\":").append(page.getNavigatePages());
		sb.append(", \"navigatepageNums\":").append(binder.toJson(page.getNavigatepageNums()));
		sb.append(", \"list\":").append(getJsonListInfo(page.getList(), nodeAttNames, jsonAttNames));
		sb.append(" }");
		return sb.toString();
	}

	public static<T> String getJsonPageInfo(Page<T> page, String nodeAttNames, String jsonAttNames, String childName)
	{
		if( page==null ){
			return "{}";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"pageNo\":").append(page.getPageNo());
		sb.append(", \"pageSize\":").append(page.getPageSize());
		sb.append(", \"orderBy\":\"").append(page.getOrderBy()).append("\"");
		sb.append(", \"orderDir\":\"").append(page.getOrderDir()).append("\"");
		sb.append(", \"totalItems\":").append(page.getTotalItems());		
		sb.append(", \"result\":").append(getJsonTreeInfo(page.getResult(), nodeAttNames, jsonAttNames, childName));
		sb.append(" }");
		return sb.toString();
	}
	
	public static void main(String[] args) throws IOException{
		System.out.println("JsonUtil test");
		//Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
		Map<Integer, SysUser> map = new LinkedHashMap<Integer, SysUser>();
		Set<SysUser> set = new HashSet<SysUser>();
		List<SysUser> list = new ArrayList<SysUser>();
		SysUser user1 = new SysUser();
		list.add(user1);
		map.put(1, user1);
		set.add(user1);
		SysUser user2 = new SysUser();
		list.add(user2);
		map.put(2, user2);
		set.add(user2);
		SysUser[] array = new SysUser[]{user1,user2};
		String jsonStr = map2json(map);
		System.out.println("map2json:" + jsonStr);
		jsonStr = list2json(list);
		System.out.println("list2json:" + jsonStr);
		jsonStr = array2json(array);
		System.out.println("array2json:" + jsonStr);
		jsonStr = set2json(set);
		System.out.println("set2json:" + jsonStr);
		
		Properties p = new Properties();
		p.put("1", "User1");
		p.put("2", "User2");
		jsonStr = propToJson(p);
		System.out.println("propToJson:" + jsonStr);
//		List<SysUser> list1 = getDTOList(jsonStr, SysUser.class);
//		if( list1!=null ){
//			for(SysUser u:list1){
//				System.out.println(" userId:"+u.getUserId()+" userName:"+u.getUserName());			
//			}
//		}
		System.out.println("....");
	}
}
