package com.everwing.coreservice.platform.core.service.impl;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.service.CommonService;
import com.everwing.coreservice.platform.core.util.Resources;
import com.everwing.coreservice.platform.util.MapperResources;
import com.google.common.base.CaseFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class CommonServiceImpl extends Resources implements CommonService {
	@Autowired
	private SqlSessionTemplate sqlSession;
	private Map<String,String> mapperMap = new HashMap<String,String>(); //存储每个类对应的Mapper的全称
	
	
	@Override
	public  RemoteModelResult<Integer> deleteByPrimaryKey(Class clazz, Object id) {
		return new RemoteModelResult(sqlSession.insert(mapperMap.get(clazz.getSimpleName())+".deleteByPrimaryKey",id));
	}

	@Override
	public  RemoteModelResult<Integer> insertSelective(Object obj) {
		return new RemoteModelResult(sqlSession.insert(mapperMap.get(obj.getClass().getSimpleName())+".insertSelective",obj));
	}
	
	@Override
	public  RemoteModelResult<Integer> deleteByExample(Class clazz, Object example) {
		String sqlFullName = mapperMap.get(clazz.getSimpleName()) + ".deleteByExample";
		int result = sqlSession.delete(sqlFullName, example);
		
		return new RemoteModelResult(result);
	}
	
	@Override
	public  RemoteModelResult<Integer> updateByExample(Object obj, Object example) {
		HashMap map = new HashMap();
		map.put("record", obj);
		map.put("example", example);
		return new RemoteModelResult(sqlSession.update(mapperMap.get(obj.getClass().getSimpleName())+".updateByExample",map));
	}
	
	@Override
	public  RemoteModelResult<Integer> updateByExampleSelective(Object obj, Object example) {
		HashMap map = new HashMap();
		map.put("record", obj);
		map.put("example", example);
		return new RemoteModelResult(sqlSession.update(mapperMap.get(obj.getClass().getSimpleName())+".updateByExampleSelective",map));
	}

	@Override
	public  RemoteModelResult<Integer> updateByPrimaryKey(Object obj) {
		return new RemoteModelResult(sqlSession.update(mapperMap.get(obj.getClass().getSimpleName())+".updateByPrimaryKey",obj));
	}

	@Override
	public  RemoteModelResult<Integer> updateByPrimaryKeySelective(Object obj) {
		return new RemoteModelResult(sqlSession.update(mapperMap.get(obj.getClass().getSimpleName())+".updateByPrimaryKeySelective",obj));
	}

	@Override
	public <T> RemoteModelResult<List<T>> selectListByExample(Class<T> clazz, Object example)
			throws ECPBusinessException {
		String sqlFullName = mapperMap.get(clazz.getSimpleName()) + ".selectByExample";
		List result = sqlSession.selectList(sqlFullName, example);
		
//		if (result.size() == 0) {
//			throw new ECPBusinessException(ReturnCode.PF_DATA_NOT_EXITS);
//		}
		return new RemoteModelResult(result);
	}

	@Override
	public <T> RemoteModelResult<T> selectOneByExample(Class<T> clazz, Object example)
			throws ECPBusinessException {
		String sqlFullName = mapperMap.get(clazz.getSimpleName()) + ".selectByExample";
		T result = sqlSession.selectOne(sqlFullName, example);
		
//		if (result == null) {
//			throw new ECPBusinessException(ReturnCode.PF_DATA_NOT_EXITS);
//		}
		return new RemoteModelResult(result);
	}

	private <T> RemoteModelResult<Object> selectByExample(Class<T> clazz, Object example, boolean isList) throws ECPBusinessException {
		String sqlFullName = mapperMap.get(clazz.getSimpleName()) + ".selectByExample";
		Object result = isList ? sqlSession.selectList(sqlFullName, example) : sqlSession.selectOne(sqlFullName, example);
		
//		if (result instanceof List && ((List) result).size() == 0) {
//			throw new ECPBusinessException(ReturnCode.PF_DATA_NOT_EXITS);
//		}
		return new RemoteModelResult(result);
	}
	
	@Override
	public <T> RemoteModelResult<T> selectByPrimaryKey(Class<T> clazz, Object id) throws ECPBusinessException{
		String sqlFullName = mapperMap.get(clazz.getSimpleName()) + ".selectByPrimaryKey";
		Object obj = sqlSession.selectOne(sqlFullName, id);
		
//		if(obj==null){
//			throw new ECPBusinessException(ReturnCode.PF_DATA_NOT_EXITS);
//		}
		return new RemoteModelResult(obj);
	}
	
	@PostConstruct
	private void initMap() throws Exception{
		for(Field f : MapperResources.class.getDeclaredFields()){
			String simpleName = f.getType().getSimpleName();
			if(simpleName.endsWith("Mapper")){
				f.setAccessible(true);
				String key = simpleName.replace("Mapper", "");
				String val = f.getType().getName();
				mapperMap.put(key, val);
			}
		}
	}


	@Override
	@Deprecated
	public <T> RemoteModelResult<List<T>> commonQuery(Class<T> clazz, List<Object[]> criteriaList,
			String orderByString, int limitStart, int limitEnd) {
		// 转换class名为表名
		String tableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
				clazz.getSimpleName());
		// 转换字段名为表名
		for (Object[] col : criteriaList) {
			col[1] = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, (String) col[1]);
		}
	
		// 调用Mapper
		List<Map<String, Object>> resultList = commonMapper.selectBySql(tableName, criteriaList,
				orderByString, limitStart, limitEnd);
		if (resultList.size() == 0) {
			throw new ECPBusinessException(ReturnCode.PF_DATA_NOT_EXITS);
		}
		System.err.println("result: " + resultList);
	
		// 封装返回的resultMap
		ArrayList<T> objList = new ArrayList<T>();
		for (Map<String, Object> map : resultList) {
			HashMap<String, Object> newMap = new HashMap<String, Object>();
			
			for (Entry<String, Object> en : map.entrySet()) {
				newMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, en.getKey()),
						en.getValue());
			}
	
			T obj = null;
			try {
				obj = clazz.newInstance();
				BeanUtils.populate(obj, newMap);
			} catch (Exception e) {
				e.printStackTrace();
				RemoteModelResult remoteRslt = new RemoteModelResult();
				remoteRslt.setCode(ReturnCode.SYSTEM_ERROR.getCode());
	            remoteRslt.setMsg(ReturnCode.SYSTEM_ERROR.getDescription());
				return remoteRslt;
			}
			objList.add(obj);
		}
	
		// 返回RemoteModelResult
		return new RemoteModelResult(objList);
	}
}