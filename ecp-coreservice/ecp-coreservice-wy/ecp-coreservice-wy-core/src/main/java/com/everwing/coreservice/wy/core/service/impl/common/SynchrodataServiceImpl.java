package com.everwing.coreservice.wy.core.service.impl.common;/**
 * Created by wust on 2018/12/19.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.common.enums.RabbitMQEnum;
import com.everwing.coreservice.common.wy.common.enums.SynchrodataEnum;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodata;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodataList;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodataSearch;
import com.everwing.coreservice.common.wy.service.common.SynchrodataService;
import com.everwing.coreservice.wy.dao.mapper.common.TSynchrodataMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.AmqpException;
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

    @Override
    public BaseDto listPage(WyBusinessContext ctx, TSynchrodataSearch tSynchrodataSearch) {
        BaseDto baseDto = new BaseDto<>();
        List<TSynchrodataList> list =  tSynchrodataMapper.listPage(tSynchrodataSearch);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
            for (TSynchrodataList tSynchrodataList : list) {
                tSynchrodataList.setOperationName(DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                        "mqOperationType",
                        tSynchrodataList.getOperation()));
                tSynchrodataList.setPriorityLevelName(DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                        "mqSyncPriorityLevel",
                        tSynchrodataList.getPriorityLevel()));
                tSynchrodataList.setStateName(DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                        "mqSyncState",
                        tSynchrodataList.getState()));
            }
        }
        baseDto.setLstDto(list);
        baseDto.setPage(tSynchrodataSearch.getPage());
        return baseDto ;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap syncData(WyBusinessContext ctx,List<String> ids) {
        MessageMap messageMap = new MessageMap();
        messageMap.setFlag(MessageMap.INFOR_SUCCESS);
        final List<TSynchrodata> modifyList = new ArrayList<>(ids.size());
        if(CollectionUtils.isNotEmpty(ids)){
            TSynchrodataSearch tSynchrodataSearch = new TSynchrodataSearch();
            for (String id : ids) {
                tSynchrodataSearch.setId(id);
                List<TSynchrodataList> tSynchrodataLists = tSynchrodataMapper.findByCondition(tSynchrodataSearch);
                if(CollectionUtils.isNotEmpty(tSynchrodataLists)){
                    TSynchrodataList tSynchrodataList = tSynchrodataLists.get(0);
                    tSynchrodataList.setState(SynchrodataEnum.state_done.getStringValue());
                    tSynchrodataList.setModifyId(ctx.getUserId());
                    tSynchrodataList.setModifyName(ctx.getStaffName());

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
                        obj.put(RabbitMQEnum.companyId.name(),ctx.getCompanyId());
                        obj.put(RabbitMQEnum.data.name(), JSONObject.toJSONString(map));
                        try {
                            this.amqpTemplate.convertAndSend(tSynchrodataList.getDestinationQueue(), obj);
                        }catch (AmqpException e){
                            throw new ECPBusinessException("推送失败");
                        }
                        modifyList.add(tSynchrodataList);
                        tSynchrodataMapper.batchModify(modifyList);
                    }
                }
            }
        }else{
            throw new ECPBusinessException("请选择数据");
        }
        return messageMap;
    }
}
