/**************************************************************
 * Copyright © 2015-2020 www.eidlink.com All rights reserved.
 *
 * 系统名称：金联汇通增值服务
 * 
 **************************************************************/
package com.clj.reptilehouse.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;


/**
 * 参数列别定义
 * 
 * @author lvsong
 * @date 2016/03/16
 */
public class CommonUtil {

	/**
	 * 业务接口类型
	 */
	public static final String BIZ_TYPE_REGEX = "0601001|0602001|00601003|0601004";

	/**
	 * 证件类型
	 */
	public static final String ID_TYPE_REGEX = "00|01|03|04|05|06|07|08|09|10|11|12|13|99";
	
	/**
	 * 姓名
	 */
	public static final String NAME_REGEX = "[\u4E00-\u9FA5]{2,}(?:·[\u4E00-\u9FA5]{2,})*";
	
	/**
	 * 住址格式
	 */
	public static final String LOCATION_REGEX = "[\u4e00-\u9fa5_a-zA-Z0-9-]*";

	/**
	 * 性别gender格式
	 * 1：男     2：女    5：女变男   6：男变女   9：未说明 
	 */
	public static final String GENDER_REGEX = "1|2|5|6|9";

	/**
	 * 银行账户类型
	 * 0：存折   1：借记卡   	2：贷记卡
	 */
	public static final String BANKTYPE_REGEX = "0|1|2";

	/**
	 * 文化程度类型
	 */
	public static final String EDU_LEV_REGEX = "10|11|19|20|21|28|29|30|31|38|39|40|41|42|48|49|50|51|59|60|61|62|63|68|69";

	/**
	 * 日期格式
	 * yyyy-MM-dd
	 */
	public static final String DATE_REGEX = "yyyy-MM-dd";

	/**
	 * 婚姻状况marriage格式
	 * 10:未婚；20：有配偶；21：初婚；22：再婚；23：复婚；30：丧偶；40：离婚；90：其他
	 */
	public static final String MARRIAGE_REGEX = "10|20|21|22|23|30|40|90";

	/**
	 * factor格式:sign_factor、encrypt_factor
	 * 长度为16位,格式：A-F、a-f、0-9
	 */
	public static final String FACTOR_REGEX = "[A-Fa-f0-9]{16}";
	
	/**
	 * factor格式:sign_factor、encrypt_factor
	 * 长度为20位,格式：A-F、a-f、0-9
	 */
	public static final String FACTOR_REGEX20 = "[A-Fa-f0-9]{20}";
	

	/**
	 * base64格式
	 * 格式：[a-z,A-Z,0-9,/,+]*[=]{0,}
	 */
	public static final String IMGAE_REGEX = "[a-z,A-Z,0-9,/,+]*[=]{0,}";

	/**
	 * mobile格式
	 * 长度为11位,格式：0-9
	 */
	public static final String MOBILE_REGEX = "[0-9]{11}";

	/**
	 * bankCardNum格式
	 * 格式：0-9
	 */
	public static final String BANKCARDNUM_REGEX = "^[0-9]*$";

	/**
	 * security_type格式
	 * 10:软KEY方式,30:加密机方式
	 */
	public static final String SECURITY_TYPE_REGEX = "10|30";

	/**
	 * 
	 * 校验BizType是否有效
	 *
	 * @param bizType 业务类型编码
	 * @return
	 */
	public static boolean isValidBizType(String bizType) {

		return bizType.matches(CommonUtil.BIZ_TYPE_REGEX);
	}

	/**
	 * 
	 * 根据bizType获取对应Model的beanId
	 *
	 * @param bizType 业务类型编码
	 * @return
	 */
	public static String getModelBeanId(String bizType) {
		String beanId = "";
		switch (bizType) {
		case "0601001":
			beanId = "simpleCheck";
			break;
		case "0601002":
			beanId = "moreCheck";
			break;
		case "0601003":
			beanId = "photoCheck";
			break;
		case "0601004":
			beanId = "cardCheck";
			break;
		}
		return beanId;
	}

	/**
	 * 
	 * 根据bizType获取对应的请求URL
	 *
	 * @param bizType 业务类型编码
	 * @return
	 */
	public static String getBizUrl(String bizType) {
		String url = "";
		switch (bizType) {
		case "0601001":
			url = "/identity/simple/";
			break;
		case "0601002":
			url = "/identity/more/";
			break;
		case "0601003":
			url = "/identity/photo/";
			break;
		case "0601004":
			url = "/identity/card/";
			break;
		}
		return url;
	}

	/**
	 * 
	 * 校验URI与bizType是否匹配
	 *
	 * @param uri 请求uri
	 * @param bizType 业务类型编码
	 * @return
	 */
//	public static boolean validateURIMatchBizType(String uri, String bizType) {
//
//		boolean flag = false;
//		String url = BizTypeEnum.getBizTypeURL(bizType);
//
//		if (uri.contains(url)) {
//			flag = true;
//		}
//		return flag;
//	}

	/**
	 * 将对象的参数排序后 按照"a=1&b=2"的方式 拼接成字符串
	 * 
	 * @param jb
	 * @return "a=1&b=2"
	 */
//	public static String getString(JSONObject jb) {
//
//		StringBuffer sb = new StringBuffer();
//		List<String> keys = new ArrayList<String>(jb.keySet());
//		Collections.sort(keys);
//		int i = 0;
//		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
//
//			String object = (String) iterator.next();
//			// 拼接串的时候排除 sign_type 和 sign 以及 '_decode'结尾的参数 、appkeyFacator、asid
//			if (GlobalConstant.SIGN.equals(object) || object.endsWith(GlobalConstant.DECODE_SUFFIX)
//					|| GlobalConstant.APP_KEY_FACTOR.equals(object) || GlobalConstant.ASID.equals(object)) {
//				continue;
//			}
//
//			String v = jb.get(object).toString();
//			if (!StringUtil.isNullOrEmpty(v) && v.startsWith("{") && v.endsWith("}")) {
//				JSONObject jb2 = JSONObject.fromObject(jb.get(object));
//				v = getString(jb2).toString();
//			}
//			if (!StringUtil.isNullOrEmpty(v) && !"null".equals(v.toLowerCase())) {
//				if (i != 0) {
//					sb.append("&");
//				}
//				sb.append(object).append("=").append(v);
//				i++;
//			}
//		}
//		return sb.toString();
//	}

	/**
	 * 
	 * 校验证件类型格式是否匹配
	 *
	 * @param idType  证件类型
	 * @return
	 */
	public static boolean isValidIdType(String idType){

		return idType.matches(CommonUtil.ID_TYPE_REGEX);
	}
	
	/**
	 * 
	 * 校验住址
	 *
	 * @param location 住址
	 * @return
	 */
	public static boolean isLocationType(String location){
		return location.matches(CommonUtil.LOCATION_REGEX);
	}
	
	/**
	 * 
	 * 校验姓名
	 *
	 * @param name  姓名
	 * @return
	 */
	public static boolean isNameType(String name){
		return name.matches(CommonUtil.NAME_REGEX);
	}

	/**
	 * 
	 * 校验出生日期是否正确
	 *
	 * @param dob  出生日期
	 * @return
	 */
	public static boolean isDobDate(String dob){

		String dateType = CommonUtil.DATE_REGEX;

		boolean convertSuccess = true;
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateType);
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(dob);
		} catch (Exception e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * 
	 * 校验性别格式是否匹配
	 *
	 * @param gender  性别
	 * @return
	 */
	public static boolean isValidGender(String gender){

		return gender.matches(CommonUtil.GENDER_REGEX);
	}

	/**
	 * 
	 * 校验银行账户类型是否匹配
	 *
	 * @param banktype  银行账户类型
	 * @return
	 */
	public static boolean isValidBanktype(String banktype){

		return banktype.matches(CommonUtil.BANKTYPE_REGEX);
	}

	/**
	 * 
	 * 校验文化程度类型是否匹配
	 *
	 * @param banktype  银行账户类型
	 * @return
	 */
	public static boolean isValidEdu_lev(String edu_lev){

		return edu_lev.matches(CommonUtil.EDU_LEV_REGEX);
	}

	/**
	 * 
	 * 校验婚姻状况格式是否匹配
	 *
	 * @param marriage  婚姻状况
	 * @return
	 */
	public static boolean isValidMarriage(String marriage){

		return marriage.matches(CommonUtil.MARRIAGE_REGEX);
	}

	/**
	 * 
	 * 校验factor是否匹配
	 *
	 * @param factor  加密因子
	 * @return
	 */
	public static boolean isValidFactor(String factor) {

		return factor.matches(CommonUtil.FACTOR_REGEX);
	}

	/**
	 * 
	 * 校验factor是否匹配
	 *
	 * @param factor  加密因子
	 * @return
	 */
	public static boolean isValidFactor20(String factor) {

		return factor.matches(CommonUtil.FACTOR_REGEX20);
	}
	
	/**
	 * 
	 * 校验image格式
	 *
	 * @param image 图片
	 * @return
	 */
	public static boolean isImageData(String image) {
		String image_r =  getStringNoBlank(image);
		return image_r.matches(CommonUtil.IMGAE_REGEX);
	}

	/**
	 * 
	 * 替换换行和空格
	 *
	 * @param image 图片
	 * @return
	 */
	public static String getStringNoBlank(String str) {      
		if(str!=null && !"".equals(str)) {      
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");      
			Matcher m = p.matcher(str);      
			String strNoBlank = m.replaceAll("");      
			return strNoBlank;      
		}else {      
			return str;      
		}           
	}  

	/**
	 * 
	 * 校验手机号码是否匹配
	 *
	 * @param mobile 手机号码
	 * @return
	 */
	public static boolean isMobileFactor(String mobile){

		return mobile.matches(CommonUtil.MOBILE_REGEX);
	}

	/**
	 * 
	 * 校验银行账号是否匹配
	 *
	 * @param bankCardNum 银行账号
	 * @return
	 */
	public static boolean isBankCardNumFactor(String bankCardNum){

		return bankCardNum.matches(CommonUtil.BANKCARDNUM_REGEX);
	}

	/**
	 * 
	 * 校验securityType是否匹配
	 *
	 * @param securityType  安全方式
	 * @return
	 */
	public static boolean isValidSecurityType(String securityType){

		return securityType.matches(CommonUtil.SECURITY_TYPE_REGEX);
	}

	
	 public static String getIpAddress(HttpServletRequest request) { 
		    String ip = request.getHeader("x-forwarded-for"); 
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getHeader("Proxy-Client-IP"); 
		    } 
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getHeader("WL-Proxy-Client-IP"); 
		    } 
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getHeader("HTTP_CLIENT_IP"); 
		    } 
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
		    } 
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getRemoteAddr(); 
		    } 
		    if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
		    	try {
					ip =  InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
		    }
		    return ip; 
		  } 

}
