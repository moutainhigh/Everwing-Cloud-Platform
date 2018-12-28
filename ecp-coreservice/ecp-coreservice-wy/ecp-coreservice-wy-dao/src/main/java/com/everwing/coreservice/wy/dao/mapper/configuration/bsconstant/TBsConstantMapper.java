package com.everwing.coreservice.wy.dao.mapper.configuration.bsconstant;

import com.everwing.coreservice.common.wy.entity.configuration.bsconstant.TBsConstant;

import java.util.List;

public interface TBsConstantMapper {

	/**
	 * 单个新增
	 * @param tBsConstant
	 * @return
	 */
	int singleAdd(TBsConstant tBsConstant);
	
	/**
	 * 根据常量名称查询
	 * @param name
	 * @param projectId
	 * @return
	 */
	TBsConstant getTBsConByName(String name,String projectId);
	
	List<TBsConstant> listPageConstants(TBsConstant tBsConstant);
	
	/**
	 * 更新
	 */
	int updateConstant(TBsConstant tBsConstant);
	
	/**
	 * 删除常量
	 */
	int delConstant(String id);
	
	/**
	 * 根据表类型和项目编号查询(不分页)
	 */
	List<TBsConstant>  getConstantsByProIdAndType(TBsConstant tBsConstant);
}
