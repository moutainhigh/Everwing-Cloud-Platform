package com.everwing.server.wy.web.controller.business.meterdata;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.common.enums.MeterEnum;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.wy.api.business.meterdata.TcMeterDataApi;
import com.everwing.coreservice.wy.api.quartz.business.MeterDataTaskApi;
import com.everwing.coreservice.wy.api.sys.TSysImportExportApi;
import com.everwing.server.wy.util.ImportExportHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @TODO 抄表历史数据控制器
 *
 */
@Controller
@RequestMapping("/meterData")
public class TcMeterDataController {

	@Autowired
	private TcMeterDataApi tcMeterDataApi;
	@Autowired
    private TSysImportExportApi tSysImportExportApi;
	@Autowired
	private CompanyApi companyApi;
	
	@Autowired
	private MeterDataTaskApi meterDataTaskApi;
	
	/**
	 * @TODO 分页展示,给抄表人员抄表
	 * @param req
	 * @param tcMeterData
	 * @return
	 */
	@RequestMapping(value="/listPageDatas",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageDatas(HttpServletRequest req, @RequestBody TcMeterData tcMeterData){
		return BaseDtoUtils.getDto(this.tcMeterDataApi.listPageDatas(CommonUtils.getCompanyId(req),tcMeterData));
	}
	
	/**
	 * @TODO 填写读取数据,批量保存
	 * @param req
	 * @param datas
	 * @return
	 */
	@RequestMapping(value="/batchModify/{operationType}",method=RequestMethod.POST)
	public @ResponseBody BaseDto batchModify(HttpServletRequest req , @RequestBody List<TcMeterData> datas,@PathVariable String operationType){
		return BaseDtoUtils.getDto(this.tcMeterDataApi.batchModify(CommonUtils.getCompanyId(req),datas,operationType));
	}
	
	
	/**
	 * @TODO 分页展示给审核页面
	 * @param req
	 * @param data
	 * @return
	 */
	@RequestMapping(value="/listPageDatasToAudit",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageDatasToAudit(HttpServletRequest req, @RequestBody TcMeterData data){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcMeterDataApi.listPageDatasToAudit(ctx.getCompanyId(),data));
	}
	
	/**
	 * @TODO 批量审核
	 * @param req
	 * @param datas
	 * @return
	 */
	@RequestMapping(value="/batchAudit/{auditStatus}",method=RequestMethod.POST)
	public @ResponseBody BaseDto batchAudit(HttpServletRequest req , @RequestBody List<TcMeterData> datas,@PathVariable Integer auditStatus){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcMeterDataApi.batchAudit(ctx.getCompanyId(),datas,auditStatus));
	}
	

	@RequestMapping(value="/modifyReading",method=RequestMethod.POST)
	public @ResponseBody BaseDto modifyReading(HttpServletRequest req , @RequestBody TcMeterData data){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcMeterDataApi.modifyReading(ctx.getCompanyId(),data));
	}
	
	
	@RequestMapping(value="/modifyReadingNew",method=RequestMethod.POST)
	public @ResponseBody BaseDto modifyReadingNew(HttpServletRequest req , @RequestBody TcMeterData data){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcMeterDataApi.modifyReadingNew(ctx.getCompanyId(),data));
	}
	
	
	
	@RequestMapping(value="/findByBuildingCode/{code}",method=RequestMethod.GET)
	public @ResponseBody BaseDto findByBuildingCode(HttpServletRequest req , @PathVariable String code){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcMeterDataApi.findByBuildingCode(ctx.getCompanyId(),code));
	}
	
	@RequestMapping(value="/findByTaskIdAndBuildingCode/{code}/{taskId}",method=RequestMethod.GET)
	public @ResponseBody BaseDto findByTaskIdAndBuildingCode(HttpServletRequest req , @PathVariable String code,@PathVariable String taskId){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcMeterDataApi.findByTaskIdAndBuildingCode(ctx.getCompanyId(),code,taskId));
	}
	
	
	
	/**
	 * 统计查询所有用户的抄表数据统计信息
	 * 		searchCode 查询数据类型   1 已抄表  2 未抄表  3 所有(必须参数)
	 *      serviceType 业务类型 1 ： 我的   2： 所有
	 * 		searchData  日期  格式 规定  年月  2017-06
	 * 		readingPerson  抄表人   可选参数
	 */
	@RequestMapping(value="/listPageReadingInfoByYear/{serviceType}",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageReadingInfoByYear(HttpServletRequest req ,@RequestBody TcMeterData tcMeterData,@PathVariable int serviceType){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		MessageMap msg;
		//封装查询参数
		if(CommonUtils.isEmpty(tcMeterData.getSearchData())){
			Date date=new Date();
			tcMeterData.setSearchData(String.valueOf(date.getYear()+1900));
		}
		if(CommonUtils.isEmpty(String.valueOf(tcMeterData.getMeterType()))){
			msg=new MessageMap(MessageMap.INFOR_ERROR,"参数[meterType]不能为空");
			return new BaseDto<>(msg);
		}
		return BaseDtoUtils.getDto(this.tcMeterDataApi.listPageReadingInfoByYear(ctx.getCompanyId(),tcMeterData,serviceType));
	}
	
	/**
	 * 抄表结果数据导入操作
	 *  @param type ：表类型：0 水表   1 电表
	 *  	   taskId 任务编号，导入操作基于任务
	 */
	@RequestMapping(value = "/importMeterReading/{type}/{taskId}", method = RequestMethod.POST)
	public @ResponseBody MessageMap importMeterReading(HttpServletRequest request,@RequestBody TSysImportExport tSysImportExportRequest,
				@PathVariable int type,@PathVariable String taskId){
		 MessageMap mm = new MessageMap();	

	        String batchNo = "";

	        String dirMessage = "";

		WyBusinessContext ctx = WyBusinessContext.getContext();

	        TSysImportExportList tSysImportExportListExist = null;

	        try {
	            if(tSysImportExportRequest == null){
	                throw new ECPBusinessException("参数[batchNo,fileType,moduleDescription]不能空");
	            }

	            if(StringUtils.isBlank(tSysImportExportRequest.getBatchNo())){
	                throw new ECPBusinessException("参数[batchNo]不能空");
	            }
	            if(StringUtils.isBlank(tSysImportExportRequest.getFileType())){
	                throw new ECPBusinessException("参数[fileType]不能空");
	            }
	            if(StringUtils.isBlank(tSysImportExportRequest.getModuleDescription())){
	                throw new ECPBusinessException("参数[moduleDescription]不能空");
	            }
	            if(StringUtils.isBlank(taskId)){
	            	 throw new ECPBusinessException("参数[taskId]不能空");
	            }
	            if(CommonUtils.isEmpty(String.valueOf(type))){
	            	throw new ECPBusinessException("参数[type]不能空");
	            }
	            
//	            dirMessage = PropertiesHelper.getInstance("config/commonConf.properties").getDefaultSystemValue("importExcelBaseDir") + File.separator + tSysImportExportRequest.getModuleDescription() + File.separator + "message" + File.separator;

	            batchNo = tSysImportExportRequest.getBatchNo();

	            TSysImportExportSearch condition = new TSysImportExportSearch();
	            condition.setBatchNo(tSysImportExportRequest.getBatchNo());
	            RemoteModelResult<List<TSysImportExportList>> remoteModelResult = tSysImportExportApi.findByCondtion(ctx.getCompanyId(),condition);
	            if(!remoteModelResult.isSuccess()){
	                throw new ECPBusinessException(remoteModelResult.getMsg());
	            }else{
	                List<TSysImportExportList>  tSysImportExportListList = remoteModelResult.getModel();
	                if(CollectionUtils.isEmpty(tSysImportExportListList)){
	                    throw new ECPBusinessException("请先上传文件");
	                }
	                tSysImportExportListExist = tSysImportExportListList.get(0);
	            }
//	            String excelPath = PropertiesHelper.getInstance("config/commonConf.properties").getDefaultSystemValue("importExcelBaseDir") + File.separator + tSysImportExportListExist.getModuleDescription() + File.separator + "excel" +File.separator + tSysImportExportListExist.getBatchNo() + "." + tSysImportExportListExist.getFileType();
	            RemoteModelResult<MessageMap> messageMapRemoteModelResult = this.tcMeterDataApi.importElectMeterReading(ctx,batchNo,null,type,taskId);
	            if(messageMapRemoteModelResult.isSuccess()){
	                mm = messageMapRemoteModelResult.getModel();
	            }else{
	                mm.setFlag(MessageMap.INFOR_ERROR);
	                mm.setMessage(messageMapRemoteModelResult.getMsg());
	            }
	        }catch (Exception e) {
	        	 mm.setFlag(MessageMap.INFOR_ERROR);
	             mm.setMessage(e.getMessage());
	             e.printStackTrace();
	         }finally {
	        	//导入后执行对导入状态的修改
	         	String stauts="";
	         	TSysImportExport tSysImportExport = new TSysImportExport();
	         	BeanUtils.copyProperties(tSysImportExportListExist,tSysImportExport);
	         	tSysImportExport.setEndTime(new Date());
	         	if(MessageMap.INFOR_SUCCESS.equals(mm.getFlag())){
	                 stauts = ImportExportEnum.succeed.name();
	            }else if(MessageMap.INFOR_WARNING.equals(mm.getFlag())) {
	             	 stauts = ImportExportEnum.partial_success.name();
	            }else {
	             	 stauts = ImportExportEnum.failed.name();
	            }
	         	
	         	tSysImportExport.setStatus(stauts);
	         	tSysImportExportApi.modify(CommonUtils.getCompanyId(request),tSysImportExport);
	             // 写入本地
	             ImportExportHelper.writeTxt(dirMessage,batchNo,mm.getMessage());
	        }
	        
	        return mm;
	}
	
	/**
	 * @TODO 分页展示抄表历史数据信息
	 * @param req
	 * @param tcMeterData
	 * @return
	 */
	@RequestMapping(value="/listPageReadingRecords",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageReadingRecords(HttpServletRequest req, @RequestBody TcMeterData tcMeterData){
		return BaseDtoUtils.getDto(this.tcMeterDataApi.listPageReadingRecords(CommonUtils.getCompanyId(req),tcMeterData));
	}
	
	/**
	 * 查询表的历史数据
	 */
	@RequestMapping(value="/listHistoryMeterData",method=RequestMethod.POST)
	public @ResponseBody BaseDto listHistoryMeterData(HttpServletRequest req,@RequestBody TcMeterData tcMeterData){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcMeterDataApi.listPageHistories(ctx, tcMeterData));
	}

	/**
	 * 查询表的历史数据
	 */
	@RequestMapping(value="/selectAbnormalData",method=RequestMethod.POST)
	public @ResponseBody BaseDto selectAbnormalData(HttpServletRequest req,@RequestBody TcMeterData meterData){
		return BaseDtoUtils.getDto(this.tcMeterDataApi.selectAbnormalData(CommonUtils.getCompanyId(req), meterData));
	}
	
	@RequestMapping(value="/selectAbnormalElectData",method=RequestMethod.POST)
	public @ResponseBody BaseDto selectAbnormalElectData(HttpServletRequest req,@RequestBody TcMeterData meterData){
		return BaseDtoUtils.getDto(this.tcMeterDataApi.selectAbnormalElectData(CommonUtils.getCompanyId(req), meterData));
	}
	
	/**
	 * 下载模板
	 */
	@RequestMapping(value = "/downloadTemplate/{meterType}", method=RequestMethod.GET)
	public void downloadTemplate(HttpServletResponse response, @PathVariable Integer meterType){
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		String fileName="";
		String filePath="";
		if(meterType.equals(MeterEnum.RECORD_METERTYPE_ELECT.getIntValue())){
			fileName = "电表抄表导入模板.xls";
			filePath = "excelTemplate/export/electmeter_reading_template.xls";
		}
		if(meterType.equals(MeterEnum.RECORD_METERTYPE_WARTER.getIntValue())){
			fileName="水表抄表导入模板.xls";
			filePath = "excelTemplate/export/watermeter_reading_template.xls";
		}
        try {
            response.setHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        OutputStream outputStream = null;
        InputStream in = null;
        try {
            outputStream = response.getOutputStream();
            Resource resource = new ClassPathResource(filePath);
            in = resource.getInputStream();

            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in != null){
                    in.close();
                }

                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {

            }
        }
	}
	
	@RequestMapping(value="/testScanSchedule",method=RequestMethod.POST)
	public @ResponseBody BaseDto testScanSchedule(HttpServletRequest req){
		RemoteModelResult<List<Company>> rslt = this.companyApi.queryAllCompany();
		String companyStr = "";
		if(rslt != null && rslt.isSuccess()){
			List<Company> list = rslt.getModel();
			for(Company c : list){
				if("09841dc0-204a-41f2-a175-20a6dcee0187".equals(c.getCompanyId())){
					companyStr = JSON.toJSONString(c);
					break;
				}
			}
		}
		return BaseDtoUtils.getDto(this.meterDataTaskApi.scanSchedule("09841dc0-204a-41f2-a175-20a6dcee0187", companyStr)); 
	}
	
	@RequestMapping(value="/testAutoCompelete",method=RequestMethod.POST)
	public @ResponseBody BaseDto testAutoCompelete(HttpServletRequest req){
		RemoteModelResult<List<Company>> rslt = this.companyApi.queryAllCompany();
		String companyStr = "";
		if(rslt != null && rslt.isSuccess()){
			List<Company> list = rslt.getModel();
			for(Company c : list){
				if("09841dc0-204a-41f2-a175-20a6dcee0187".equals(c.getCompanyId())){
					companyStr = JSON.toJSONString(c);
					break;
				}
			}
		}
		return BaseDtoUtils.getDto(this.meterDataTaskApi.autoCompleteTask("09841dc0-204a-41f2-a175-20a6dcee0187", companyStr)); 
	}
}
