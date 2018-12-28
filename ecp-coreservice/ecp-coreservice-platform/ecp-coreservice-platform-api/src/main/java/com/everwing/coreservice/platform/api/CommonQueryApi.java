package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.util.Criteria;
import com.everwing.coreservice.platform.api.util.ServiceResources;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description 通用数据操作API-底层封装了Mybatis-Generator生成的Example、Mapper操作
 * @author MonKong
 * @date 2017年4月7日
 */
@Service
public class CommonQueryApi extends ServiceResources {

	
	public <T> RemoteModelResult<List<T>> selectListByExample(Class<T> clazz, Object example) throws ECPBusinessException{
		return commonService.selectListByExample(clazz, example);
	}

	public <T> RemoteModelResult<T> selectOneByExample(Class<T> clazz, Object example) throws ECPBusinessException{
		return commonService.selectOneByExample(clazz, example);
	}

	public <T> RemoteModelResult<T> selectByPrimaryKey(Class<T> clazz, Object id) throws ECPBusinessException{
		return commonService.selectByPrimaryKey(clazz, id);
	}
	
	public RemoteModelResult<Integer> deleteByExample(Class clazz, Object example) {
		return commonService.deleteByExample(clazz, example);
	}
	
	public RemoteModelResult<Integer> updateByExample(Object obj, Object example) {
		return commonService.updateByExample(obj, example);
	}
	
	public RemoteModelResult<Integer> updateByExampleSelective(Object obj, Object example) {
		return commonService.updateByExampleSelective(obj, example);
	}

	public RemoteModelResult<Integer> updateByPrimaryKey(Object obj) {
		return commonService.updateByPrimaryKey(obj);
	}

	public RemoteModelResult<Integer> updateByPrimaryKeySelective(Object obj) {
		return commonService.updateByPrimaryKeySelective(obj);
	}

	public RemoteModelResult<Integer> insertSelective(Object obj) {
		return commonService.insertSelective(obj);
	}

	public RemoteModelResult<Integer> deleteByPrimaryKey(Class clazz, Object id) {
		return commonService.deleteByPrimaryKey(clazz, id);
	}
	

	@NoExceptionProxy
	@Deprecated
	public <T> Criteria<T> queryFrom(Class<T> clazz) {
		return new Criteria<T>(clazz);
	}

	@Deprecated
	@NoExceptionProxy
	public <T> Criteria<T> queryFrom(Class<T> clazz, T obj) {
		return new Criteria<T>(clazz, obj);
	}
}