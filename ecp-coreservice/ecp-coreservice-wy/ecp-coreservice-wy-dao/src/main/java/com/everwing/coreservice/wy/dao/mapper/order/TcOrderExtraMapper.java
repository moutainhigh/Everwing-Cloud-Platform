package com.everwing.coreservice.wy.dao.mapper.order;

import com.everwing.coreservice.common.wy.dto.OrderSearchDto;
import com.everwing.coreservice.common.wy.entity.order.TcOrder;

import java.util.List;
import java.util.Map;

public interface TcOrderExtraMapper {

	List<Map> listPageDatas (OrderSearchDto orderSearchDto);

    void updatePrinciplePersonByOrderCode(TcOrder order);
}