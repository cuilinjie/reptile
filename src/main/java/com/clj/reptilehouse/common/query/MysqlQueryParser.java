package com.clj.reptilehouse.common.query;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mysql query parser
 * @author jiangxs
 * @since 1.0.1(Jul 4, 2015)
 */
public class MysqlQueryParser implements QueryParser {

	private Pattern FIELD_PATTERN = Pattern.compile("#\\{([a-zA-Z_$0-9]+)\\}");
	
	@Override
	public StringBuffer parse(Query query) {
		if(query != null) {
			StringBuffer sb = new StringBuffer();
			List<Comparator> comparators = query.getComparators();
			boolean hasComparators = (comparators != null && !comparators.isEmpty());
			if(hasComparators) {
				for(int i = 0; i < comparators.size(); i++) {
					Comparator c = comparators.get(i);
					if(i > 0) {
						if(c instanceof GroupComparator) {
							if(!((GroupComparator) c).isStart()) {
								sb.append(c.getExpression());
								continue;
							}
						}
						if(c instanceof LogicalComparator) {
							Comparator prev = comparators.get(i - 1);
							if(!(prev instanceof GroupComparator) || !((GroupComparator) prev).isStart()) {
								LogicalOperator lo = c.getLogicalOperator();
								if(lo == null) lo = Comparator.DEFAULT_LOGICAL_OPERATOR;
								sb.append(" " + lo + " ");
							}
						}
					}

					parseComparator(sb, c);
				}
			}
			if(!hasComparators) sb.append(" 1=1 ");
			Sort sort = query.getSort();
			if(sort != null && !isBlank(sort.getName())) {
				sb.append(" ORDER BY `" + escapeSQLField(sort.getName(), false) + "`");
				if(sort.getDirection() != null) sb.append(" " + sort.getDirection().name());
			}
			int limit  = query.getLimit();
			if(limit > 0) {
				sb.append(" LIMIT " + limit);
			}
			return sb;
		}
		return null;
	}
	
	private void parseComparator(StringBuffer sb, Comparator c) {
		String expression = c.getExpression();
		Matcher m = FIELD_PATTERN.matcher(expression);
		while(m.find()) {
			String field = m.group(1);
			Object value = getFieldValue(field, c);
			m.appendReplacement(sb, value.toString());
		}
		m.appendTail(sb);
	}
	
	@SuppressWarnings("unchecked")
	private String getFieldValue(String name, Comparator c) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(name, c.getClass());
			Method m = pd.getReadMethod();
			Object value = m.invoke(c);
			if("name".equals(name)) return "`" + escapeSQLField(parseNameField(value.toString()), true) + "`";
			if(c instanceof InComparator) {
				Object val = ((InComparator) c).getValue();
				Collection<Object> coll = null;
				if(val.getClass().isArray()) {
					int len = Array.getLength(val);
					coll = new ArrayList<Object>();
					Class<?> comType = val.getClass().getComponentType();
					if(comType.isPrimitive()) {
						String type = comType.getName();
						String methodName = toHumpString("get." + type);
						Method method = Array.class.getMethod(methodName, Object.class, int.class);
						for(int i = 0; i < len; i++) {
							Object comValue = method.invoke(Array.class, val, i);
							coll.add(comValue);
						}

					} else {
						Object[] objs = (Object[]) val;
						coll = Arrays.asList(objs);
					}
					for(int i = 0; i < len; i++) {
						//coll.add(Array.)
					}
				} else {
					coll = (Collection<Object>)(((InComparator) c).getValue());
				}
				return quoteCollectionValue(coll);
			} else {
				return quoteValue(value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String parseNameField(String field) {
		int index = field.indexOf('.');
		if(index < 0) return field;
		String split[] = field.split("\\.");
		StringBuilder sb = new StringBuilder(split[0]);
		for(int i = 1; i < split.length;i++) {
			String s = split[i]; 
			sb.append(s.substring(0, 1).toUpperCase());
			sb.append(s.substring(1));
		}
		return sb.toString();
	}
	
	private String quoteValue(Object value) {
		if(value instanceof Boolean) return (((Boolean)value).booleanValue() ? "1" : "0");
		if(value instanceof Number) return value.toString();
		if(value instanceof Date) return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value) + "'";
		String val = value.toString();
		return "'" + escapeSQLParameter(val, true) + "'";
	}

	@SuppressWarnings("rawtypes")
	private String quoteCollectionValue(Collection list) {
		int count = 0;
		int len = list.size();
		String ret = "";
		for(Object value : list) {
			ret += quoteValue(value);
			if(count < len - 1) ret += ",";
			count++;
		}
		return ret;
	}
	
	private String escapeSQLField(String src, boolean inReg) {
		if(src != null) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < src.length(); i++) {
				char c = src.charAt(i);
				if(inReg) {
					if(c == '`') sb.append("\\\\\\");
					if(c == '\\') sb.append("\\\\\\");
				}else {
					if(c == '`') sb.append('\\');
					if(c == '\\') sb.append('\\');
				}
				
				sb.append(c);
			}
			return sb.toString(); 
		}
		return "";
	}
	
	private String escapeSQLParameter(String src, boolean inReg) {
		if(src != null) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < src.length(); i++) {
				char c = src.charAt(i);
				if(inReg) {
					if(c == '\'') sb.append("\\\\\\");
					if(c == '\\') sb.append("\\\\\\");
				}else {
					if(c == '\'') sb.append('\\');
					if(c == '\\') sb.append('\\');
				}
				
				sb.append(c);
			}
			return sb.toString(); 
		}
		return "";
	}
	
	private boolean isBlank(String src) {
		if(src == null) return true;
		for(int i = 0; i < src.length(); i++) {
			if(!Character.isWhitespace(src.charAt(i))) return false;
		}
		return true;
	}


	private static final Pattern HUMP_PATTERN = Pattern.compile("\\.(.)");

	public static String toHumpString(String src) {
		Matcher m = HUMP_PATTERN.matcher(src);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String find = m.group(1);
			m.appendReplacement(sb, find.toUpperCase());
		}
		m.appendTail(sb);
		return sb.toString();
	}


	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		/*String q = QueryBuilder.custom()
				.andEquivalent("name", "saj")
				.andGroup()
				.andEquivalent("age",12)
				.orEquivalent("sex", 0)
				.endGroup()
				.andEquivalent("title", "kkk")
				.withLimit(30)
				.build();

		//MysqlQueryParser p = new MysqlQueryParser();
		System.out.println(q);*/
	}
}
