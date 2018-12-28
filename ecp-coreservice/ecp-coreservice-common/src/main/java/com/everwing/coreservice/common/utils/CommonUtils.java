package com.everwing.coreservice.common.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.context.WyBusinessContext;

/**
 * @TODO common utils
 * @author wsw
 * @createTime 2016年12月12日17:07:15
 */
@Component
public class CommonUtils implements ApplicationContextAware {
	private static final Logger logger= LoggerFactory.getLogger(CommonUtils.class);
	private static ApplicationContext applicationContext;

	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private static final String LOG_STR = "当前时间 : %s , 日志内容 : %s";
	
	private static Calendar c = Calendar.getInstance();
	
	public static boolean onlyOneExist(Object... params) {
		int existCounter = 0;
		for(Object obj:params){
			if (obj instanceof String) {
				String str = (String) obj;
				if (str != null && !"".equals(str.trim())) {
					existCounter++ ;
				}
			} else if (obj != null) {
				existCounter++ ;
			}
		}
		return existCounter == 1;
	}
	
	public static String getRequestBasePath(HttpServletRequest request) {
		URL url = null;
		try {
			url = new URL(request.getRequestURL().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return url.getProtocol() + "://" + url.getAuthority() + request.getContextPath();
	}
	
	public static void throwECPException(ReturnCode rc) throws ECPBusinessException{
		throw new ECPBusinessException(rc);
	}
	
	public static RemoteModelResult returnSuccess(){
		return new RemoteModelResult();
	}
	
	public static boolean notBlank(Object object) {
		if(object instanceof String){
			return StringUtils.isNotBlank((String) object);
		}else{
			return object != null;
		}
	}
	
	public static void isAnyNull(Object... params) {
		logger.info("校验参数组：{}",Arrays.toString(params));
		if (params != null) {
			for (Object obj : params) {
				if (obj == null) {
					throw new ECPBusinessException(ReturnCode.PF_PARAMS_MISSING);
				} else if (obj instanceof String) {
					if (StringUtils.isBlank((String) obj)) {
						throw new ECPBusinessException(ReturnCode.PF_PARAMS_MISSING);
					}
				}
			}
		}else{
			throw new ECPBusinessException(ReturnCode.PF_PARAMS_MISSING);
		}
	}
	
	
	/**
	 * 
	 * @TODO 获取UUID
	 * @Author wsw
	 * @params
	 * @return String
	 * @createDate 下午1:48:59
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	/**
	 * @TODO 字符串转换成double
	 * @Author wsw
	 * @params
	 * @return double
	 * @createDate 下午2:06:25
	 */
	public static double null2Double(Object obj) {
		if (isEmpty(obj))
			return 0.0;

		return CommonUtils.getScaleNumber(Double.valueOf(obj.toString()),2);
	}

	public static BigDecimal null2BigDecimal(Object obj){
		if (isEmpty(obj))
			return new BigDecimal(0.0);

		return new BigDecimal(CommonUtils.getScaleNumber(Double.valueOf(obj.toString()),2));
	}

	/**
	 * @TODO 对象为空串时,转换成null
	 * @param obj
	 * @return
	 */
	public static String empty2Null(Object obj) {
		if (isEmpty(obj)) {
			return null;
		}
		return null2String(obj);
	}

	/**
	 * @TODO 将传入的Map的值都换成null,去掉空串
	 * @param paramMap
	 * @return
	 */
	public static Map<String, Object> changeValues(Map<String, Object> paramMap) {
		if (isEmpty(paramMap)) {
			return new HashMap<String, Object>();
		}
		for (String key : paramMap.keySet()) {
			paramMap.put(key, empty2Null(paramMap.get(key)));
		}
		return paramMap;
	}

	public static List<String> getCodes(String code, String regex) {
		List<String> codes = new ArrayList<String>();
		if (isEmpty(code))
			return codes;

		String[] codeArr = code.split(regex);
		String s = codeArr[0];
		for (int i = 1; i < codeArr.length; i++) {
			s += "_" + codeArr[i];
			codes.add(s);
		}
		return codes;
	}

	/**
	 * @TODO 字符串转换成int
	 * @Author wsw
	 * @params
	 * @return int
	 * @createDate 下午2:06:33
	 */
	public static int null2Int(Object obj) {
		if (isEmpty(obj))
			return 0;

		return Integer.parseInt(null2String(obj));
	}

	public static boolean isEquals(String str1, String str2) {
		if(null == str1 || null == str2){
			return false;
		}
		return (str1 == str2) || str1.equals(str2);
	}
 
	/**
	 * @TODO 对象转换成字符串
	 * @Author wsw
	 * @params
	 * @return String
	 * @createDate 下午2:06:47
	 */
	public static String null2String(Object obj) {
		if(obj instanceof Double){
			return String.valueOf(obj);
		}
		if (null == obj || "".equals(((String) obj).trim()) || "null".equals(((String) obj).trim()))
			return Constants.STR_EMPTY;

		return obj.toString().trim();

	}
	
	public static boolean paramsHasNull(Object... objs){
		for(Object obj : objs){
			if(isEmpty(obj))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @TODO 判断空
	 * @Author wsw
	 * @params
	 * @return boolean
	 * @createDate 下午2:07:04
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {

		if (null == obj)
			return true;
		else {
			if (obj instanceof String)
				return Constants.STR_EMPTY.equals(null2String(obj))
						|| "null".equals(null2String(obj));

			else if (obj instanceof Collection)
				return ((Collection) obj).isEmpty();

			else if (obj instanceof Map)
				return ((Map) obj).isEmpty();

			else if (obj instanceof Object[])
				return ((Object[]) obj).length == 0;
			
			else if(obj instanceof Number)
				return ((Number)obj).doubleValue() == 0;
			
			else
				return false;
		}
	}

	/**
	 * @TODO 判断非空
	 * @Author wsw
	 * @params
	 * @return boolean
	 * @createDate 下午2:07:16
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * @TODO 时间转换成字符串
	 * @Author wsw
	 * @params
	 * @return String
	 * @createDate 下午2:07:24
	 */
	public static String getDateStr(Date date, String format) {
		if (isEmpty(date))
			date = new Date();

		if (isEmpty(format))
			format = FORMAT;

		return new SimpleDateFormat(format).format(date);
	}

	public static String getDateStr(Date date) {
		return getDateStr(date, null);
	}

	public static String getDateStr() {
		return getDateStr(null, null);
	}

	
	/**
	 * @describe 对时间进行月份的增加
	 * @param date 需要增加月份的时间
	 * @param num 增加月份数
	 * @return 最终获得的时间
	 * @author QHC
	 */
	public static Date addMonth(Date date,int num){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, num);
		return calendar.getTime();
	}
	
	


	/**
	 * @describe 对时间进行日的增加
	 * @param date 需要增加天数的时间
	 * @param num 增加的天数
	 * @return 最终获得的时间
	 * @author QHC
	 */
	public static Date addDay(Date date,int num){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, num);
		return calendar.getTime();
	}

	/**
	 * @describe 对时间进行日的减少
	 * @param date 需要减少天数的时间
	 * @param num 减少的天数
	 * @return 最终获得的时间
	 * @author QHC
	 */
	public static Date reduceDay(Date date,int num){
		long dateNum = date.getTime();
		Date returnDate = new Date( (dateNum - (1000*3600*24)*num) / (1000*3600*24) );
		return returnDate;
	}


	/**
	 * @TODO 字符串解析时间
	 * @Author wsw
	 * @params
	 * @return Date
	 * @createDate 下午2:07:37
	 */
	public static Date getDate(String str, String format) {
		if (isEmpty(str))
			return null;

		if (isEmpty(format))
			format = FORMAT;
		try {
			return new SimpleDateFormat(format).parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date getDate(String dateStr) {
		return getDate(dateStr, null);
	}

	public static Date getDate(){
		return getDate(null, null);
	}

	/**
	 * 
	 * @TODO 字符串转换成List<String>
	 * @Author wsw
	 * @params
	 * @return List<String>
	 * @createDate 下午3:23:24
	 */
	public static List<String> str2List(String str, String regex) {
		if (isEmpty(regex) || isEmpty(str))
			return null;

		return new ArrayList<String>(Arrays.asList(str.split(regex)));

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CommonUtils.applicationContext = applicationContext;
	}
	public static ApplicationContext getApplicationContext() throws BeansException {
		return applicationContext;
	}
	
	
	public static String getCompanyIdByCurrRequest(){
		return getCompanyId(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest());
	}

	/**
	 * 没必要再封装一次去获取，直接BusinessContext.getContext().getCompanyId()
	 * @param req
	 * @return
	 */
	@Deprecated
	public static String getCompanyId(HttpServletRequest req){
		WyBusinessContext ctx = null;
		try {
			return WyBusinessContext.getContext().getCompanyId();
		} catch (Exception e) {

		}
        return Constants.STR_ZERO;
	}

	/**
	 * 没必要再封装一次去获取，直接BusinessContext.getContext().getLoginName()
	 * @param req
	 * @return
	 */
	@Deprecated
	public static String getLoginName(HttpServletRequest req){
		WyBusinessContext ctx = null;
		try {
			return WyBusinessContext.getContext().getLoginName();
		} catch (Exception e) {

		}
		 return Constants.STR_ZERO;
	}
	
	/**
	 * @TODO 补充字符串   如补足成 TASK00005
	 * @param oldStr   5
	 * @param fillChar  0
	 * @param totalLength   5  - >>    00005
	 * @return
	 */
	public static String complete(String oldStr , char fillChar , int totalLength){
		if(CommonUtils.isEmpty(oldStr)){
			oldStr = "";
		}
		
		if(oldStr.length() < totalLength){
			int length = totalLength - oldStr.length();
			for(int i = 0 ; i < length ; i++){
				if(oldStr.length() > totalLength){
					break;
				}
				oldStr = fillChar + oldStr;
			}
		}else{
			oldStr = oldStr.substring(oldStr.length() - totalLength);
		}
		return oldStr;
	}
	
	/**
	 * @TODO 补充字符串   如补足成 TASK00005
	 * @param oldStr   5
	 * @param fillChar  0
	 * @param totalLength   5  - >>    00005
	 * @return
	 */
	public static String completeToRight(String oldStr , char fillChar , int totalLength){
		if(CommonUtils.isEmpty(oldStr)){
			oldStr = "";
		}
		
		int oldLength = totalLength;
		try {
			oldLength = oldStr.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(oldLength < totalLength){
			int length = totalLength - oldLength;
			for(int i = 0 ; i < length ; i++){
				if(oldLength > totalLength){
					break;
				}
				oldStr += fillChar;
			}
		}else{
			oldStr = oldStr.substring(oldStr.length() - totalLength);
		}
		return oldStr;
	}
	
	public static List<File> getAllFilesOfDir(String dirPath) {
		File dir = new File(dirPath);
		List<File> fileList = new ArrayList<File>();
		if (dir.isDirectory()) {// 安全检查
			for (File file : dir.listFiles()) {
				if (file.isDirectory()) {
					fileList.addAll(getAllFilesOfDir(file.getAbsolutePath()));
				} else {
					fileList.add(file);
				}
			}
		} else {
			return fileList;
		}
		return fileList;
	}
	
	public static Date changeDays(Date startDate , int dayCount){
		if(null == startDate){
			return null;
		}
		c.setTime(startDate);
		c.add(Calendar.DATE, dayCount);
		return c.getTime();
	}
	
	public static Date changeMonths(Date startDate , int monthCount){
		if(null == startDate)
			return null;
		c.setTime(startDate);
		c.add(Calendar.MONTH, monthCount);
		return c.getTime();
	}
	
	/**
	 * @TODO 获取传入时间的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDayInMonth(Date date){
		if(date == null){
			date = new Date();
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1);
		
		return c.getTime();
	}
	
	/**
	 * @TODO 获取传入月份的最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDayInMonth(Date date){
		if(null == date) date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
		return c.getTime();
	}
	
	@SuppressWarnings("deprecation")
	public static Date setCurrentDate(Date date){
		if(null == date){
			return null;
		}
		if(date.compareTo(new Date()) >= 0) {
			c.setTime(date);
		}else{
			c.setTime(new Date());
		}
		int year = c.get(Calendar.YEAR);
		
		c.setTime(date);
		c.set(Calendar.MONTH, (date.compareTo(new Date()) >= 0) ? date.getMonth() : new Date().getMonth());
		c.set(Calendar.YEAR, year);
		return c.getTime();
	}



	public static String list2inSql(List<?> list){
		String str = "";
		Object[] objs = list.toArray();
		for (int i = 0; i < objs.length; i++) {
			str += (i != 0) ? ", " : "" ;
			str += "'" + objs[i] + "'";
		}
		return str;
	}
	
	/**
	 * @TODO 获取两个时间的相差天数(此方法如果跨年是不行的，废弃   qhc 2018-06-22)
	 * @param date1
	 * @param date2
	 * @return
	 */
	@Deprecated
	public static int getDiffDays(Date date1 , Date date2){
		if(null == date1 || null == date2){
			return 0;
		}
		c.setTime(date1);
		int day1 = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(date2);
		int day2 = c.get(Calendar.DAY_OF_YEAR);
		return day1 - day2;
	}
	
	/**
	 * 获取两个时间的相差天数
	 * @param date1  较小的时间
	 * @param date2  较大的时间
	 * @return
	 */
	public static int getDiffDaysNew(Date date1 , Date date2){
		int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
	}


	public static String log(String logStr){
		return String.format(LOG_STR , getDateStr() , logStr);
	}
	
	/**
	 * 获取某一天到本月结束的相隔天数
	 * @param date
	 * @param day
	 * @return
	 */
	public static int getDiffDaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        int maxMountDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
        
		return maxMountDay;
	}

	public static double getSum(Object... objs) {
		if(isEmpty(objs))
			return 0.0;
		double d = 0.0;
		for(Object obj : objs){
			d += null2Double(obj);
		}
		return null2Double(d);
	}
	
	public static double getGap(Object o1 , Object o2){
		return null2Double(o1) - null2Double(o2);
	}
	
	public static double getTax(double amount, double taxRax){
		return new BigDecimal((amount * (taxRax / 100)) / (1 + (taxRax / 100))).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static double getScaleNumber(double d , int scale){
		return new BigDecimal(d).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static BigDecimal formatBigDecimal(double d , int scale){
		return new BigDecimal(d).setScale(scale,BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal formatBigDecimal(double d){
		return formatBigDecimal(d,2);
	}
	  //                               88.14           0                 0         88.14账户最大扣取额  
	public static double calKf(Double total, Double minsNum,  Double ncd , Double currKf){
		total = getGap(total, minsNum);
		ncd = null2Double(ncd);
		currKf = null2Double(currKf);
		if(getSum(ncd,currKf) > total){
			ncd = total;
		}else{
			ncd += currKf;
		}
		return getScaleNumber(ncd, 2); 
	}
	
	public static String getScaleString(double d, int scale){
		return new BigDecimal(d).setScale(scale,BigDecimal.ROUND_HALF_UP).toString();
	}
	
	public static void clearLists(List... lists){
		if(isNotEmpty(lists)){
			for(List l : lists){
				if(null != l)
					l.clear();
			}
		}
	}
}
