package com.everwing.coreservice.platform.dao.mapper.extra;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonMapper {

	List<Map<String, Object>> selectBySql(@Param("tableName") String tableName,
			@Param("criteriaArray") List<Object[]> criteriaArray,
			@Param("orderByString") String orderByString, @Param("limitStart") int limitStart,
			@Param("limitEnd") int limitEnd);

}