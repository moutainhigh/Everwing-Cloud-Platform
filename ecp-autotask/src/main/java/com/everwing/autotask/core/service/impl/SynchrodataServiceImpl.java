package com.everwing.autotask.core.service.impl;/**
 * Created by wust on 2018/12/19.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.autotask.core.dao.TSynchrodataMapper;
import com.everwing.autotask.core.service.SynchrodataService;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.enums.RabbitMQEnum;
import com.everwing.coreservice.common.wy.common.enums.SynchrodataEnum;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodata;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodataList;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodataSearch;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2018/12/19
 * @author wusongti@lii.com.cn
 */
@Service("synchrodataServiceImpl")
public class SynchrodataServiceImpl implements SynchrodataService {
    @Autowired
    private TSynchrodataMapper tSynchrodataMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;



    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap syncData(String companyId) {
        MessageMap messageMap = new MessageMap();

        TSynchrodataSearch tSynchrodataSearch = new TSynchrodataSearch();
        tSynchrodataSearch.setState(SynchrodataEnum.state_draft.getStringValue());
        List<TSynchrodataList> tSynchrodataLists = tSynchrodataMapper.findByCondition(tSynchrodataSearch);
        if(CollectionUtils.isEmpty(tSynchrodataLists)){
            return messageMap;
        }

        for (TSynchrodataList tSynchrodataList : tSynchrodataLists) {
            tSynchrodataList.setState(SynchrodataEnum.state_done.getStringValue());

            String tableName = tSynchrodataList.getTableName();
            String tableFieldName = tSynchrodataList.getTableFieldName();
            String tableFieldValue = tSynchrodataList.getTableFieldValue();
            String sql = "SELECT * FROM " + tableName + " WHERE " + tableFieldName + " = '" + tableFieldValue + "'";
            Map parameterMap = new HashMap(1);
            parameterMap.put("sql",sql);
            List<Map> mapList = tSynchrodataMapper.findDestinationTableDataBySQL(parameterMap);
            if(CollectionUtils.isNotEmpty(mapList)){
                Map map = mapList.get(0);
                JSONObject obj = new JSONObject();
                obj.put(RabbitMQEnum.opt.name(), tSynchrodataList.getOperation());
                obj.put(RabbitMQEnum.companyId.name(),companyId);
                obj.put(RabbitMQEnum.data.name(), JSONObject.toJSONString(map));
                this.amqpTemplate.convertAndSend(tSynchrodataList.getDestinationQueue(), obj);

                List<TSynchrodata> modifyList = new ArrayList<>(1);
                modifyList.add(tSynchrodataList);
                tSynchrodataMapper.batchModify(modifyList);
            }
        }
        return messageMap;
    }
}
