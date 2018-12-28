package com.everwing.coreservice.wy.core.mq.billmgr;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.utils.SysConfig;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import com.everwing.coreservice.wy.core.utils.ReportUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("billWriteListener")
public class BillWriteListener extends ServiceRrcs implements MessageListener{
	
	private static final Logger logger = LogManager.getLogger(BillWriteListener.class);
	
	@Autowired
	private SysConfig sysConfig;
	
	@Autowired
	private CompanyApi companyApi;
	
	@Qualifier("redisDataOperator")
	@Autowired
	private SpringRedisTools springRedisTools;
	
	@Value("${queue.task2Wy.bill.file.upload.key}")
	private String route_key_bill_upload_key;
	
	@Autowired
	private AmqpTemplate amqpTemplate;

	@Override
	public void onMessage(Message message) {
		
		
		String jsonStr = null;
		
		try {
 			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("监听队列: [账单生成]  消息转换格式错误. 错误消息: {}. " , e.getMessage());
		}

		File jasperFile= null;
		try {
			jasperFile = ResourceUtils.getFile("classpath:"+sysConfig.getBillFtlPath()+File.separator+ sysConfig.getBillFtlFileName());
		} catch (FileNotFoundException e) {
			logger.error("未找到报表文件，失败");
			return;
		}
		if(!jasperFile.exists()){
			logger.error("未找到账单模板,失败!");
			return;
		}
		logger.info("billWriteListener freemarker模板生成pdf收到消息:{}",jsonStr);
		if(null != jsonStr){
			
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			RemoteModelResult<Company> rslt = this.companyApi.queryCompany(entity.getCompanyId());
			if(!rslt.isSuccess()){
				return ;
			}
			List<String> buildingCodes=entity.getBuildingCodes();
			//按每栋起的消息队列, 对每栋进行html转pdf并打包
			int flagInt = entity.getOpr();
			String title = (flagInt == BillingEnum.mq_gen_bill_manaul_first.getIntV()) ? "账单手动生成":
								(flagInt == BillingEnum.mq_gen_bill_auto.getIntV()) ? "账单自动生成":"账单重新生成";
			if(null != entity.getData() && null != rslt.getModel()){
				//TODO 监测本项目是否打包完成,若已经打包完成,则调用消息队列进行项目级打包并上传文件
				BaseDto returnDto = this.tBsProjectService.findById(entity.getCompanyId(),entity.getProjectId());
				
				if(returnDto == null || returnDto.getObj() == null){
					logger.warn("定时任务: [账单zip包扫描打包及上传]  未找到对应项目. 公司id : {} , 项目id : {} ", entity.getCompanyId(), entity.getSupAttr());
					return;
				}
				
				TBsProject project = (TBsProject) returnDto.getObj();
				String dongStr = entity.getSupAttr();
				String dongName = dongStr.split("-")[0];
				String dongCode = dongStr.split("-")[1];
				List<Map<String,Object>> coll= null;
				try {
					coll = JSON.parse(entity.getData().toString(),List.class);
				} catch (ParseException e) {
					logger.error("数据传输错误,结束!");
					return;
				}
				logger.info("{}公司的:{}项目:{}栋数据准备完成！共:{}户",rslt.getModel().getCompanyName(),entity.getProjectName(),dongName,coll.size());
				String newPath = sysConfig.getBillZipPath() + File.separator + rslt.getModel().getCompanyName() + File.separator + entity.getProjectName();
				File folder=new File(newPath);
				if(!folder.exists()){
					folder.mkdirs();
				}
				String fileName = dongName + "-" + CommonUtils.getDateStr(new Date(), "yyyy年MM月") + ".pdf";
				String dongPdfFile=newPath+File.separator+fileName;
				if(!coll.isEmpty()&&coll.size()>0) {
					long start=System.currentTimeMillis(); //获取开始时间
					ReportUtils.generatePDF(jasperFile.getPath(), dongPdfFile, coll);
					long end=System.currentTimeMillis(); //获取结束时间
					logger.info("栋PDF程序生成时间： "+(end-start)+"ms");
				}else {
					return;
				}
				logger.info("{}公司的:{}项目pdf生成完成。路径:{}",rslt.getModel().getCompanyName(),entity.getProjectName(),newPath);

				//对项目下的本栋建筑的分单做打包完成标识
				TBsChargeBillHistory paramObj = new TBsChargeBillHistory();
				paramObj.setProjectId(project.getProjectId());
				paramObj.setBillingTime(project.getBillingTime());
				paramObj.setBuildingCode(dongCode);
				paramObj.setIsZipComplete(BillingEnum.bill_is_zip_yes.getIntV());
				this.tBsBillHistoryService.updateBilledBuilding(entity.getCompanyId(), paramObj,buildingCodes); //对本栋打上完成标识

				
				//查询本项目下是否还有未打包的房屋
				BaseDto countDto = this.tBsBillHistoryService.findNotZipByObj(entity.getCompanyId(),paramObj);
				if(countDto != null && (Integer)countDto.getObj() == 0){
					//打包完成
					MqEntity e = new MqEntity();
					e.setCompanyId(entity.getCompanyId());
					e.setProjectId(project.getProjectId());
					e.setSupAttr(project.getId());
					e.setOpr(flagInt);
					this.amqpTemplate.convertAndSend(route_key_bill_upload_key, e);
					
					//投递至: BillFileUploadListener
				}
				
				
			}else{
				logger.warn("监听队列: [账单文件写入] 传入数据为空, 本次消息执行完成. 数据:{}",entity.toString());
			}
		}
	}
}
