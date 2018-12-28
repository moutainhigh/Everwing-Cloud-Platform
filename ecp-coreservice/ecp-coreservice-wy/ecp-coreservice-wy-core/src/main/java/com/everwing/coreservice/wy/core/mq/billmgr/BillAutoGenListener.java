package com.everwing.coreservice.wy.core.mq.billmgr;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @TODO 监听平台按物业公司起的消息队列 : 账单自动生成
 * @author DELL
 *
 */
@Service("billAutoGenListener")
public class BillAutoGenListener extends ServiceRrcs implements MessageListener{
	
	private static final Logger logger = LogManager.getLogger(BillAutoGenListener.class);

	@Value("${queue.task2Wy.bill.gen.key}")
	private String route_key_gen_bill;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("监听队列: [账单自动生成]  消息转换失败. ");
		}

		if(jsonStr != null){
			logger.info("billAutoGenListener接收到接收到生成账单消息:{}",jsonStr);
			MqEntity entity = JSONObject.parseObject(jsonStr,MqEntity.class);
			if(entity.getCompanyId() != null){
				
				BaseDto dto = this.tBsProjectService.findCanGenBillProject(entity.getCompanyId());
				
				if(CommonUtils.isNotEmpty(dto.getLstDto())){
					for(Object obj : dto.getLstDto()){
						TBsProject pro = (TBsProject) obj;
						
						//判断当前项目下是否已经打包完全,如果打包完全则不再投入消息队列
						TBsChargeBillHistory paramObj = new TBsChargeBillHistory();
						paramObj.setProjectId(pro.getProjectId());
						paramObj.setBillingTime(pro.getBillingTime());
						BaseDto countDto = this.tBsBillHistoryService.findNotZipByObj(entity.getCompanyId(),paramObj); 
						if(countDto != null && (Integer)countDto.getObj() == 0){
							//如果打包完全则不再投入消息队列
							break;
						}
						
						MqEntity e = new MqEntity();
						e.setCompanyId(entity.getCompanyId());
						e.setData(pro);
						e.setOpr(BillingEnum.mq_gen_bill_auto.getIntV());
						this.amqpTemplate.convertAndSend(route_key_gen_bill, e);
						
						//投递至BillMgrListener
						logger.info("监听队列: [账单自动生成]  投递至公司{}, {}项目完成.",entity.getCompanyId(), pro.getProjectName());
					}
				}else{
					logger.info("监听队列: [账单自动生成]  当前公司下,未找到可进行账单生成的项目,程序运行完成. 公司id:{} .",entity.getCompanyId());
				}
				
			}else{
				logger.info("监听队列: [账单自动生成]  传入消息为空, 程序运行完成. ");
			}
		}else{
			logger.info("监听队列: [账单自动生成]  传入消息为空, 程序运行完成. ");
		}
	}
}
