package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.order.TcOrderComplete;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/6/5.
 */
public interface TcOrderCompleteMapper {

    /**
     * 查找未计费的产权变更单
     */
    List<Map<String,Object>> getNoBill(String buildCode, String projectId);

    TcOrderComplete findById(String id);

    void singleUpdate(TcOrderComplete tcOrderComplete);
}
