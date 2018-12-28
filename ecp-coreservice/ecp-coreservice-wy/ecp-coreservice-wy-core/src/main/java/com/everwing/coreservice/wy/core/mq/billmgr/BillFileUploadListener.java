package com.everwing.coreservice.wy.core.mq.billmgr;

import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.AnnexEnum;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.utils.SysConfig;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service("billFileUploadListener")
public class BillFileUploadListener extends ServiceRrcs implements MessageListener{

	
	private static final Logger logger = LogManager.getLogger(BillFileUploadListener.class);
	
	@Autowired
	private SysConfig sysConfig;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private FastDFSApi fastDFSApi;
	
	@Qualifier("redisDataOperator")
	@Autowired
	private SpringRedisTools springRedisTools;
	
	@Value("${queue.wy2wy.tBsProject.modify.key}")
	private String route_key_tBsProject_modify;
	
	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("监听队列: [账单zip包扫描打包及上传]  消息转换格式错误. 错误消息: {}. " , e.getMessage());
		}
		logger.info("billFileUploadListener开始上传整包!:{}",jsonStr);
		if(jsonStr != null){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			if(entity.getCompanyId() != null){
				File file = new File(sysConfig.getBillZipPath());  //E:\file\bill_zip
				
				if(!file.exists() || CommonUtils.isEmpty(file.listFiles())){
					logger.warn("定时任务: [账单zip包扫描打包及上传]  路径下未找到可打包的文件. 路径 : {}. ", sysConfig.getBillZipPath());
					return;
				}
				
				BaseDto returnDto = this.tBsProjectService.findById(entity.getCompanyId(), entity.getSupAttr());
				
				if(returnDto == null || null == returnDto.getObj()){
					logger.warn("定时任务: [账单zip包扫描打包及上传]  未找到对应项目. 公司id : {} , 项目id : {} ", entity.getCompanyId(), entity.getSupAttr());
					return;
				}
				
				TBsProject project = (TBsProject) returnDto.getObj();
				
				//E:\file\bill_zip\深圳市翔恒科技开发有限公司\桃源盛景园
				//按项目上传
				ZipOutputStream zos = null;
				FileInputStream fis = null;
				//1.打成zip包 
				
				for(File companyFile : file.listFiles()){ //E:\file\bill_zip下所有的公司级别
					File[] projectFiles = companyFile.listFiles();  //E:\file\bill_zip\深圳市翔恒科技开发有限公司    下项目级别
					if(CommonUtils.isEmpty(projectFiles)){
						logger.info("定时任务: [账单zip包扫描打包及上传]  路径下未找到可打包的项目文件. 路径:{} ", companyFile.getAbsolutePath());
						continue;
					}
					logger.info("项目文件列表:{}",projectFiles);
					//桃源盛景园文件夹下
					for(File projectFile : projectFiles){
						logger.info("开始将公司:{}下的:{}项目打包",companyFile.getName(),projectFile.getName());
						if(projectFile == null || projectFile.getName().endsWith(AnnexEnum.annex_file_type_zip.getStringV())){
							continue;
						}
						logger.info("projectFile:{}continue之后",projectFile);
						String zipFileName = projectFile.getName() + CommonUtils.getDateStr(project.getBillingTime(), "-yyyy年MM月");
						File zipProjectFile = new File(companyFile.getAbsolutePath() + File.separator + zipFileName + ".zip");
						
						try {
							zos = new ZipOutputStream(new FileOutputStream(zipProjectFile)); //公司目录下
							
							for(File zipFile : projectFile.listFiles()){
								ZipEntry entry = new ZipEntry(zipFile.getName());
								zos.putNextEntry(entry);
								
								fis = new FileInputStream(zipFile);
								byte[] b = new byte[fis.available()];
								fis.read(b);
								zos.write(b);
								fis.close();
								b = null;
								fis = null;
								zos.flush();
							}
							zos.flush();
							zos.close();
							zos = null;
							
							//2.上传
							
							//2.1 同步等待压缩包上传并修改附件状态, 异步修改项目的账单状态生成状态将本项目的is_gen_bill修改成已生成状态
							TBsProject tBsProject = new TBsProject();
							tBsProject.setId(entity.getSupAttr());
							tBsProject.setIsGenBill(BillingEnum.bill_is_gen_yes.getIntV());
							
							MqEntity modifyProject = new MqEntity();
							modifyProject.setCompanyId(entity.getCompanyId());
							entity.setData(tBsProject);
							this.amqpTemplate.convertAndSend(route_key_tBsProject_modify, entity);
							logger.info("修改项目状态mq投递完成");
							//2.2 上传
							RemoteModelResult<UploadFile> rslt = this.fastDFSApi.uploadFile(zipProjectFile);
							
							if(rslt.isSuccess()){
								
								//2.2.1 组装数据,插入物业公司ts_annex
								Annex annex = new Annex();
								annex.setAnnexId(rslt.getModel().getUploadFileId());
								annex.setAnnexName(zipFileName);
								annex.setAnnexType(AnnexEnum.annex_type_zip.getIntV());
								annex.setCompanyId(entity.getCompanyId());
								annex.setRelationId(project.getProjectId());
								annex.setAnnexAddress(rslt.getModel().getPath());
								annex.setFileType(AnnexEnum.annex_file_type_zip.getStringV());
								annex.setProjectId(project.getProjectId());
								annex.setAnnexTime(CommonUtils.getDateStr());
								annex.setIsUsed(AnnexEnum.annex_is_used_yes.getIntV());
								annex.setMd5(rslt.getModel().getMd5());
								annex.setUploadFileId(rslt.getModel().getUploadFileId());
								this.annexService.addAnnex(entity.getCompanyId(), annex);
								
								//2.2.2 删除该项目及项目下所有文件
								projectFile.delete();
								if(null != projectFile && projectFile.exists()){
									FileUtils.forceDelete(projectFile);
								}
								
								if(null != zipProjectFile && zipProjectFile.exists()){
									FileUtils.forceDeleteOnExit(zipProjectFile);
								}
								logger.info("zip 上传完成");
							}else{
								logger.info("定时任务: [账单zip包扫描打包及上传]  文件上传失败, 返回数据:{} ." + rslt.toString());
							}
						} catch ( Exception e) {
							e.printStackTrace();
							continue;
						}
					}
				}
			}else{
				logger.info("监听队列: [账单文件打包上传]  传入公司id为空. ");
			}
		}else{
			logger.info("监听队列: [账单文件打包上传]  传入数据为空. ");
		}
	}

	
	
	
}
