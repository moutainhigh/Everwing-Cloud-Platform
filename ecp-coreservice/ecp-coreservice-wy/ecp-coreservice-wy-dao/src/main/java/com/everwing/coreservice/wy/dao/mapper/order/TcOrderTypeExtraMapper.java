package com.everwing.coreservice.wy.dao.mapper.order;

import com.everwing.coreservice.common.wy.entity.order.TcOrderType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TcOrderTypeExtraMapper {

	@Select("SELECT * FROM tc_order_type WHERE parent_id = #{typeId}")
	@ResultMap("com.everwing.coreservice.wy.dao.mapper.order.TcOrderTypeMapper.BaseResultMap")
	List<TcOrderType> subType(@Param("typeId") String typeId);
	
	@Select("SELECT * FROM tc_order_type WHERE level = 1")
	@ResultMap("com.everwing.coreservice.wy.dao.mapper.order.TcOrderTypeMapper.BaseResultMap")
	List<TcOrderType> levelOneType();
	
}