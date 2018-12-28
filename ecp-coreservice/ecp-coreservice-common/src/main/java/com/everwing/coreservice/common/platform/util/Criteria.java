package com.everwing.coreservice.common.platform.util;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.exception.ReturnCodeAware;
import com.everwing.coreservice.common.platform.service.CommonService;
import com.everwing.coreservice.common.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Criteria<T>  {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private Class<T> clazz;
	private T obj;// 单表查询时使用
	private List<Object[]> criteriaList = new ArrayList<Object[]>();
	private CommonService commonService = CommonUtils.getApplicationContext().getBean(CommonService.class);
	private String orderStr;
	private int limitStart = -1;
	private int limitEnd = -1;

	private static final String AND = "and";
	private static final String OR = "or";
	private static final String EQUAL = "=";
	private static final String NOT_EQUAL = "<>";
	private static final String LESS_THAN = "<";
	private static final String LESS_THAN_OR_EQUAL = "<=";
	private static final String GREATER_THAN = ">";
	private static final String GREATER_THAN_OR_EQUAL = ">=";
	private static final String IN = "in";

	public Criteria(Class<T> clazz) {
		super();
		
		this.clazz = clazz;
	}

	public Criteria(Class<T> clazz, T obj) {
		super();
		this.clazz = clazz;
		this.obj = obj;
	}


	private Criteria<T> andEqual(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.AND, key, Criteria.EQUAL, val });
		return this;
	}

	public Criteria<T> andNotEqual(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.AND, key, Criteria.NOT_EQUAL, val });
		return this;
	}

	public Criteria<T> andLessThan(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.AND, key, Criteria.LESS_THAN, val });
		return this;
	}

	public Criteria<T> andLessThanOrEqual(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.AND, key, Criteria.LESS_THAN_OR_EQUAL, val });
		return this;
	}

	public Criteria<T> andGreaterThan(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.AND, key, Criteria.GREATER_THAN, val });
		return this;
	}

	public Criteria<T> andGreaterThanOrEqual(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.AND, key, Criteria.GREATER_THAN_OR_EQUAL, val });
		return this;
	}

	public Criteria<T> andIn(String key, List<?> val) {
		criteriaList.add(new Object[] { Criteria.AND, key, Criteria.IN, val });
		return this;
	}

	public Criteria<T> andIsNull(String key) {
		criteriaList.add(new Object[] { Criteria.AND, key, "is", key, null });
		return this;
	}

	public Criteria<T> andIsNotNull(String key) {
		criteriaList.add(new Object[] { Criteria.AND, key, "is", key, null });
		return this;
	}

	public Criteria<T> orEqual(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.OR, key, Criteria.EQUAL, val });
		return this;
	}

	public Criteria<T> orNotEqual(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.OR, key, Criteria.NOT_EQUAL, val });
		return this;
	}

	public Criteria<T> orLessThan(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.OR, key, Criteria.LESS_THAN, val });
		return this;
	}

	public Criteria<T> orLessThanOrEqual(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.OR, key, Criteria.LESS_THAN_OR_EQUAL, val });
		return this;
	}

	public Criteria<T> orGreaterThan(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.OR, key, Criteria.GREATER_THAN, val });
		return this;
	}

	public Criteria<T> orGreaterThanOrEqual(String key, Object val) {
		criteriaList.add(new Object[] { Criteria.OR, key, Criteria.GREATER_THAN_OR_EQUAL, val });
		return this;
	}

	public Criteria<T> orIn(String key, List<?> list) {
		criteriaList.add(new Object[] { Criteria.OR, key, Criteria.IN, key, list });
		return this;
	}

	public Criteria<T> orIsNull(String key) {
		criteriaList.add(new Object[] { Criteria.OR, key, "is", key, null });
		return this;
	}

	public Criteria<T> orIsNotNull(String key) {
		criteriaList.add(new Object[] { Criteria.OR, key, "is", key, null });
		return this;
	}

	public RemoteModelResult<List<T>> listResult() {
		return query();
	}

	public RemoteModelResult<T> singleResult() {
		setLimit(0, 1);
		RemoteModelResult<List<T>> rmr = query();
		
		RemoteModelResult<T> singleRmr = new RemoteModelResult<T>();
		singleRmr.setModel(rmr.getModel() == null ? null : rmr.getModel().get(0));
		singleRmr.setCode(rmr.getCode());
		singleRmr.setMsg(rmr.getMsg());
		
		return singleRmr;
	}

	private RemoteModelResult<List<T>> query() {
		Map<String, Object> objMap;
		if (obj != null) {// 把Object对象属性转为equal条件
			try {
				objMap = objectToMapWithoutNullValue(obj);
				for (Entry<String, Object> en : objMap.entrySet()) {
					andEqual(en.getKey(), en.getValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		RemoteModelResult<List<T>> rmr = null;
		try{
			rmr = commonService.commonQuery(clazz, criteriaList, orderStr, limitStart, limitEnd);
		}catch (ECPBusinessException e) {
			rmr = new RemoteModelResult<List<T>>();
			ReturnCodeAware ex = (ReturnCodeAware) e;
            String errCode = ex.getErrorCode();
            String errDesc = ex.getErrorDescription();
            Object[] args = ex.getArgs();
            String errMsg = "";
            if(args == null){
                errMsg = errDesc;
            }else{
                errMsg = MessageFormat.format(errDesc, args);/*appContext.getMessage(errCode, args, Locale.CHINA);*/
            }
            rmr.setCode(errCode);
            rmr.setMsg(errMsg);
            logger.error(errMsg);
            System.err.println(errMsg);
		}
		return rmr;
	}

	public Map<String, Object> objectToMapWithoutNullValue(Object obj) throws Exception {
		if (obj == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();

		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (key.compareToIgnoreCase("class") == 0) {
				continue;
			}
			Method getter = property.getReadMethod();
			Object value = getter != null ? getter.invoke(obj) : null;
			if (value != null) {// without null value
				map.put(key, value);
			}
		}
		return map;
	}

	public String getOrderStr() {
		return orderStr;
	}

	public Criteria<T> setOrderStr(String orderStr) {
		this.orderStr = orderStr;
		return this;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public Criteria<T> setLimit(int limitStart, int limitEnd) {
		this.limitStart = limitStart;
		this.limitEnd = limitEnd;
		return this;
	}

	public int getLimitEnd() {
		return limitEnd;
	}


}
