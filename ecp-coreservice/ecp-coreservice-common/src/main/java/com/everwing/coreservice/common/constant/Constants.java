package com.everwing.coreservice.common.constant;

import java.util.HashMap;
import java.util.Map;


public class Constants {

	
	public static final String STR_EMPTY = "";
	
	public static final String STR_AUTO_GENER = "system";
	
	/**-------------------------------   是否标记    -------------------------------*/
	public static final String STR_YES = "Y";
	
	public static final String STR_NO = "N";
	
	public static final String STR_YES_CN = "是";
	
	/**-------------------------------   redis    -------------------------------*/
	public static final String REDIS_PORT = "REDIS.PORT";
	
	public static final String REDIS_HOST = "REDIS.HOST";
	
	/**-------------------------------   常用返回    -------------------------------*/
	public static final String RETURN_SUCCESS = "{\"state\":\"success\"}";
	
	public static final String RETURN_FAILED = "{\"state\":\"failed\"}";
	
	public static final String RETURN_ERROR = "{\"state\":\"error\"}";
	
	/**-------------------------------   魔鬼数字    -------------------------------*/
	public static final String STR_ONE = "1";
	
	public static final String STR_TWO = "2";

	public static final String STR_THREE = "3";

	public static final String STR_FOUR = "4";

	public static final String STR_FIVE = "5";

	public static final String STR_SIX = "6";

	public static final String STR_SEVEN = "7";

	public static final String STR_EIGHT = "8";
	
	public static final String STR_NINE = "9";
	
	public static final String STR_ZERO = "0";

	
	/**-------------------------------   计费类型      -------------------------------*/
	public static final String BILLING_WY = "物业管理费";
	public static final String BILLING_BT = "本体基金";
	public static final String BILLING_WATER = "水费";
	public static final String BILLING_ELECT = "电费";
	public static final String BILLING_COMMON = "通用";
	
	public static final String BILLING_WY_STR = "wy";
	public static final String BILLING_BT_STR = "bt";
	public static final String BILLING_WATER_STR = "water";
	public static final String BILLING_ELECT_STR = "elect";
	public static final String BILLING_ALL_STR = "all";
	
	public static final String PAY_STR_SINGLE = "single";
	public static final String PAY_STR_ALL = "all";
	
	/**-------------------------------   生效无效      -------------------------------*/
	public static final String IS_USED_USING = "false";
	public static final String IS_USED_STOP = "true";
	
	/**-------------------------------   符号      -------------------------------*/
	public static final String STR_COMMA = ",";
	
	public static final String STR_UNDERLINE = "_";
	
	public static final String STR_ANON = "anon";
	
	public static final String STR_COMMON = "common";
	
	public static final String STR_VERT_LINE = "|";
	
	/**-------------------------------   建筑类型      -------------------------------*/
	public static final String TYPE_HOUSE = "house";
	public static final String TYPE_HOUSE_CH = "住宅";
	
	public static final String TYPE_STORE = "store";
	public static final String TYPE_STORE_CH = "商铺";
	
	public static final String TYPE_PARKSPACE = "parkspace";
	public static final String TYPE_PARKSPACE_CH = "车位";	
	
	
	/**-------------------------------   证件类型      -------------------------------*/
	public static final String CARD_TYPE_ZERO = "身份证";
	public static final String CARD_TYPE_ONE = "护照";
	
	public static final String ENCRY_KEY = "jZ5$x!6yeAo1Qe^r";
	/**-------------------------------   格式转换类型      -------------------------------*/
	public static final String STR_YYYYMMDD = "yyyyMMdd";
	
	public static final String STR_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	/**-------------------------------   国籍      -------------------------------*/
	public static final String NATIONAL_CHINA = "中国";
	
	public static final String NATIONAL_OTHER = "外籍";

	//水/电表 公共区域的子表
	public static final Integer METER_TYPE_CHILDREN = 12;
	
	public static Map<Integer, String> statusMap = new HashMap<Integer, String>();
	
	public static Map<Integer, String> payTypeMap = new HashMap<Integer, String>(); 
	
	public static Map<Integer,String> feeTypeMap = new HashMap<Integer,String>();
	
	static{
		statusMap.put(0, "收款");
		statusMap.put(1, "退款");
		statusMap.put(2, "减免");
		statusMap.put(3, "回退");
		
		payTypeMap.put(1, "现金");
		payTypeMap.put(2, "微信");
		payTypeMap.put(3, "银联");
		payTypeMap.put(4, "混合支付");
		payTypeMap.put(5, "托收");
		payTypeMap.put(6, "银行");
		payTypeMap.put(7, "支付宝");
		payTypeMap.put(8, "微信小程序");

		feeTypeMap.put(0, "通用账户");
		feeTypeMap.put(1, "物业管理费");
		feeTypeMap.put(2, "本体基金");
		feeTypeMap.put(3, "水费");
		feeTypeMap.put(4, "电费");
	}
	
	
	
	
	
}
