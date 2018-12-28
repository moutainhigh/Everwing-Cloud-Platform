package com.everwing.coreservice.wy.core.task;/**
 * Created by wust on 2017/5/27.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.common.enums.RabbitMQEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 *
 * Function:推送建筑数据到平台的任务
 * Reason:
 * Date:2017/5/27
 * @author wusongti@lii.com.cn
 */
public class Wy2platform4buildingTask implements Runnable{
    static Logger logger = LogManager.getLogger(Wy2platform4buildingTask.class);

    private AmqpTemplate amqpTemplate;
    private List<?> tcBuildingList;
    private String opt;
    private String companyId;
    @Value("${queue.wy2platform.building.key}")
    private String ROUTE_KEY;


    public Wy2platform4buildingTask(List<?> tcBuildingList,String opt,String companyId){
        this.amqpTemplate  = ((AmqpTemplate) SpringContextHolder.getBean("amqpTemplate"));
        this.tcBuildingList  = tcBuildingList;
        this.opt = opt;
        this.companyId = companyId;
    }

    @Override
    public void run() {
        if(!CollectionUtils.isEmpty(tcBuildingList)){
            JSONObject obj = new JSONObject();
            obj.put(RabbitMQEnum.opt.name(), opt);
            obj.put(RabbitMQEnum.companyId.name(),companyId);
            obj.put(RabbitMQEnum.data.name(), tcBuildingList);
            this.amqpTemplate.convertAndSend(ROUTE_KEY, obj);
            logger.info("推送建筑信息到平台{}",tcBuildingList);
        }else{
            logger.info("没有建筑信息需要推送");
        }
    }
}
