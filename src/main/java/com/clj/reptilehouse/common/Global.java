package com.clj.reptilehouse.common;

public class Global
{
//	public final static String springmvc_SRC = "http://172.16.5.17:7878/springmvc";
	public final static String springmvc_SRC = "http://springmvc.yqzbw.com:8090";
	
	public final static int USER_END_TIME = 1; //用户终止时间默认一个月
	
	public final static String DEFAULT_SKINNAME = "default"; // 默认登录皮肤名称

	public final static String MENU_STYLE_DEFAULT = "index1"; // 默认菜单风格
	public final static String MENU_STYLE_2 = "index2"; // 菜单风格2
	public final static String MENU_STYLE_3 = "index3"; // 菜单风格3
	public static final String LOGIN_URL ="/login.jsp"; // 默认登录url;
	public final static String LOCAL_IP = "LOCAL_IP"; // 本地服务器IP地址
	public final static String REMOTE_IP = "REMOTE_IP"; // 客户端IP地址
	public final static String LOGIN_USER = "LOGIN_USER"; // 登录用户
	public final static String LOGIN_USER_MAIN_ORGAN = "LOGIN_USER_MAIN_ORGAN"; // 登录用户主机构
	public final static String LOGIN_USER_SUB_ORGAN = "LOGIN_USER_SUB_ORGAN"; // 登录用户所有子机构
	public final static String LOGIN_USER_SUB_ORGAN_IDS = "LOGIN_USER_SUB_ORGAN_IDS"; // 登录用户所有子机构IDs
	public final static String LOCAL_USER_PASSWD = "LOCAL_USER_PASSWD"; // 用户真实密码
	public final static String LOGIN_USER_POWCODEMAP = "LOGIN_USER_POWCODEMAP"; // 登录用户的权限编码集
	public final static String LOGIN_USER_POWURLMAP = "LOGIN_USER_POWURLMAP"; // 登录用户的权限链接内容集
	public final static String LOGIN_USER_MENUURLMAP = "LOGIN_USER_MENUURLMAP"; // 登录用户的菜单链接内容集

	public final static String CURRENT_ACCESS_URL = "CURRENT_ACCESS_URL"; // 登录用户当前访问链接
	public final static String CURRENT_LEFT_MENU_ROOT_CODE = "CURRENT_LEFT_MENU_ROOT_CODE"; // 当前访问左边菜单根节点树编码

	public final static String PARAM_TOPMENU_PARENT_TREE_CODE = "topMenuParentTreeCode"; // top菜单的父ID树编码的参数编码

	public final static String RES_ITEM_BACK_URL = "RES_ITEM_BACK_URL"; // 资源项管理页面返回链接
	public final static String PARAMETER_BACK_URL = "PARAMETER_BACK_URL"; // 参数管理页面返回链接

	public final static byte FALSE = 0; // 假
	public final static byte TRUE = 1; // 真
	public final static String USER_NAME = "USER_NAME"; // 用户名称

	public final static String USER_ADMIN = "admin";	 // 超级用户账号
	public final static String USER_DEVADMIN = "devadmin";	 // 开发管理员账号

	public final static int USER_TYPE_OP_MIN = 1; // 最低级别操作用户
	public final static int USER_TYPE_OP_MAX = 4; // 最高级别操作用户
	public final static int USER_TYPE_AD_MIN = 5; // 最低级别管理用户
	public final static int USER_TYPE_AD_MAX = 8; // 最高级别管理用户
	public final static int USER_TYPE_DEV = 9; // 开发用户

	// public final static int ROLE_TYPE_OP = 1; // 操作用户角色
	// public final static int ROLE_TYPE_AD = 5; // 管理用户角色
	// public final static int ROLE_TYPE_DV = 9; // 开发用户角色

	public final static int STATE_OFF = 0; // 停用状态
	public final static int STATE_ON = 1; // 启用状态

	// 资源组类型
	public final static int GROUP_TYPE_LIST = 0; // 列表类型
	public final static int GROUP_TYPE_TREE = 1; // 树状类型

	// 资源项(编码)类型
	public final static int ITEM_TYPE_STRING = 0; // 字符串
	public final static int ITEM_TYPE_INT = 1; // 整数

	// 参数类型
	public final static String PARAM_TYPE_STRING = "string"; // 字符串
	public final static String PARAM_TYPE_INT = "int"; // 整数
	public final static String PARAM_TYPE_FLOAT = "float"; // 浮点数
	public final static String PARAM_TYPE_DATETIME = "datatime"; // 日期时间
	public final static String PARAM_TYPE_DATE = "date"; // 日期
	public final static String PARAM_TYPE_TIME = "time"; // 时间

	// 以下是资源组编码宏定义
	public final static String RES_GRP_TYPE = "resGrpType"; // 资源组类型
	public final static String RES_CODE_TYPE = "resCodeType"; // 资源编码类型
	public final static String FUNC_MENU_TYPE = "funcMenuType"; // 功能菜单类型
	public final static String PARAM_VALUE_TYPE = "paramValueType"; // 参数值类型
	public final static String USER_TYPE = "userType"; // 用户类型
	public final static String ROLE_TYPE = "roleType"; // 角色类型
	public final static String ORGAN_TYPE = "organType"; // 机构类型
	
	public final static String SPLIT_KEY = ";";
	
	public final static byte[] AES_KEY = "klajfjkjieiwnadc_@#*!".getBytes();
	
	public final static String RESULT_SUCCESS = "success";
	public final static String RESULT_ERROR = "error";
	public final static String RESULT_OUTTIME = "outtime";
	public final static String RESULT_ERROR_OUTTIME = "errot/outtime";

	// 公共信息定义
	public final static String EXCEL_IMP_MDL_NAME = "EXCEL_IMP_MDL_NAME"; // EXCEL导入数据模版名称
	public final static String EXCEL_IMP_URL = "EXCEL_IMP_URL"; // EXCEL导入数据提交链接

	public final static String EXCEL_EXP_COL_NAMES = "EXCEL_EXP_COL_NAMES"; // EXCEL导出属性名称序列
	public final static String EXCEL_EXP_URL = "EXCEL_EXP_URL"; // EXCEL导出数据提交链接
	
	
	public final static String SERVICE_01 = "0601001";
	public final static String SERVICE_02 = "0601002";
	public final static String SERVICE_03 = "0601003";
	public final static String SERVICE_04 = "0601004";
	public final static String BILL_TYPE_M = "month";
	public final static String BILL_TYPE_D = "day";
	
	
}
