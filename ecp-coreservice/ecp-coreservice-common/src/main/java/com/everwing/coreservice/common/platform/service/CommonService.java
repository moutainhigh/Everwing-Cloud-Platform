package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;

import java.util.List;

public interface CommonService {

	RemoteModelResult<Integer> deleteByPrimaryKey(Class clazz, Object id);

	public RemoteModelResult<Integer> insertSelective(Object obj);

	public RemoteModelResult<Integer> updateByPrimaryKey(Object obj);

	public RemoteModelResult<Integer> updateByPrimaryKeySelective(Object obj);

	<T> RemoteModelResult<List<T>> selectListByExample(Class<T> clazz, Object example)
			throws ECPBusinessException;

	<T> RemoteModelResult<T> selectOneByExample(Class<T> clazz, Object example)
			throws ECPBusinessException;

	<T> RemoteModelResult<T> selectByPrimaryKey(Class<T> clazz, Object id)
			throws ECPBusinessException;

	RemoteModelResult<Integer> deleteByExample(Class clazz, Object example);

	RemoteModelResult<Integer> updateByExampleSelective(Object obj, Object example);

	RemoteModelResult<Integer> updateByExample(Object obj, Object example);
	
	@Deprecated
	public <T> RemoteModelResult<List<T>> commonQuery(Class<T> clazz, List<Object[]> criteriaList,
			String orderByString, int limitStart, int limitEnd);


}
