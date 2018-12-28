package com.everwing.server.wy.web.controller.business.electmeter;

import com.esotericsoftware.minlog.Log;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ChangeElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeterSearch;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.wy.api.business.electmeter.TcElectMeterApi;
import com.everwing.coreservice.wy.api.sys.TSysImportExportApi;
import com.everwing.server.wy.util.ImportExportHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/ElectricityMeter")
public class TcElectMeterController {

	@Autowired
	private TcElectMeterApi tcElectMeterApi;
	@Autowired
    private TSysImportExportApi tSysImportExportApi;
	/**
	 * 新增电表信息
	 * @param req
	 * @param electMeter
	 * @return
	 */
	@RequestMapping(value="/addElectMeter",method=RequestMethod.POST)
	public @ResponseBody BaseDto addelectmeter(HttpServletRequest req,@RequestBody ElectMeter electMeter){
		electMeter.setCreateid(CommonUtils.getLoginName(req));
		return BaseDtoUtils.getDto(tcElectMeterApi.addelectmeter(CommonUtils.getCompanyId(req), electMeter));
	}
	
	/**
	 * 查询电表信息
	 * @param req
	 * @param electMeterSearch
	 * @return
	 */
	@RequestMapping(value="/listPageElectricityMeter",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageElectmeterByCompany(HttpServletRequest req,@RequestBody ElectMeterSearch electMeterSearch){
		return BaseDtoUtils.getDto(tcElectMeterApi.listPageElectmeterByCompany(CommonUtils.getCompanyId(req), electMeterSearch));
	}
	
	/**
	 * 启停电表(该方法只是批量更改状态)
	 * @param req
	 * @param electMeter
	 * @return
	 */
	@RequestMapping(value="/startOrstopElectMeter",method=RequestMethod.POST)
	public @ResponseBody BaseDto startOrstopElectMeter(HttpServletRequest req,@RequestBody ElectMeter electMeter){
		electMeter.setModifyid(CommonUtils.getLoginName(req));
		return BaseDtoUtils.getDto(tcElectMeterApi.startOrstopElectMeter(CommonUtils.getCompanyId(req), electMeter));
	}
	
	@RequestMapping(value="/editSave",method=RequestMethod.POST)
	public @ResponseBody BaseDto editSave(HttpServletRequest req,@RequestBody ElectMeter electMeter){
		electMeter.setModifyid(CommonUtils.getLoginName(req));
		return BaseDtoUtils.getDto(tcElectMeterApi.editSave(CommonUtils.getCompanyId(req), electMeter));
	}
	
	@RequestMapping(value="/delElect",method=RequestMethod.POST)
	public @ResponseBody BaseDto delElect(HttpServletRequest req,@RequestBody ElectMeter electMeter){
		return BaseDtoUtils.getDto(tcElectMeterApi.delElect(CommonUtils.getCompanyId(req), electMeter));
	}
	
	
	/**
	 * 变更
	 */
	@RequestMapping(value="/electchange",method=RequestMethod.POST)
	public @ResponseBody BaseDto electchange(HttpServletRequest req,@RequestBody ChangeElectMeter changeElectMeter){
		changeElectMeter.getAfterelectricty().setModifyid(CommonUtils.getLoginName(req));
		return BaseDtoUtils.getDto(tcElectMeterApi.changeElectMeter(CommonUtils.getCompanyId(req), changeElectMeter));
	}
	
	@RequestMapping(value="/startOrStop",method=RequestMethod.POST)
	public @ResponseBody BaseDto startOrStop(HttpServletRequest req,@RequestBody ElectMeter electMeter){
		electMeter.setModifyid(CommonUtils.getLoginName(req));
		return BaseDtoUtils.getDto(tcElectMeterApi.startOrStop(CommonUtils.getCompanyId(req), electMeter));
	}
	
	/**
	 * 下载模板
	 */
	@RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
	public void downloadTemplate(HttpServletResponse response) {
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileName = "电表导入模板.xls";
        String filePath = "excelTemplate/export/electmeter_template.xls";
        
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
	
	
	/**
	 * 导入
	 */
	@RequestMapping(value="/importElectMeter",method=RequestMethod.POST)
	public @ResponseBody MessageMap importElectter(HttpServletRequest request,@RequestBody TSysImportExportSearch tSysImportExportRequest){
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

	            if(StringUtils.isBlank(tSysImportExportRequest.getProjectCode())){
	                throw new ECPBusinessException("参数[projectCode]不能空");
	            }

	            ctx.setProjectId(tSysImportExportRequest.getProjectCode());
	            ctx.setProjectName(tSysImportExportRequest.getProjectName());

	            // TODO 从配置读取
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
	            RemoteModelResult<MessageMap> messageMapRemoteModelResult = this.tcElectMeterApi.importElectMeter(ctx,batchNo,null);
	            if(messageMapRemoteModelResult.isSuccess()){
	                mm = messageMapRemoteModelResult.getModel();
	            }else{
	                mm.setFlag(MessageMap.INFOR_ERROR);
	                mm.setMessage(messageMapRemoteModelResult.getMsg());
	            }

	        } catch (Exception e) {
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
	 * 统计电表
	 */
	@RequestMapping(value="/countMeters",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto countMeters(HttpServletRequest req,@RequestBody ElectMeter entity){
		return BaseDtoUtils.getDto(this.tcElectMeterApi.countMeters(CommonUtils.getCompanyId(req),entity));
		
	}
	
	/**
	 * 查询过滤掉已经被占用为收费对象的建筑
	 */
	@RequestMapping(value="/listPageFilterRelationBuild",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageFilterRelationBuild(HttpServletRequest req,@RequestBody TcBuildingSearch entity){
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx =null;
		try {
			ctx = WyBusinessContext.getContext();
			String projectId = entity.getProjectId();
			if(StringUtils.isEmpty(projectId)){
				return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"项目编号不能为空!"));
			}
			RemoteModelResult<BaseDto> result = this.tcElectMeterApi.listPageFilterRelationBuild(ctx, entity);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
			
		} catch (Exception e) {
			Log.info(CommonUtils.log(e.getMessage()));
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			baseDto.setMessageMap(msgMap);
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}


	/**
	 * @describe
	 * @author ZYF
	 * @date 2018-12-12
	 */
	@RequestMapping(value="/listPageElectMeterByBuildingCode",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageElectMeterByBuildingCode(@RequestBody ElectMeterSearch electMeterSearch){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		RemoteModelResult<BaseDto> result =tcElectMeterApi.listPageElectMeterByBuildingCode(WyBusinessContext.getContext(),electMeterSearch);
		if (!result.isSuccess()) {
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(result.getMsg());
		} else {
			baseDto = result.getModel();
		}
		baseDto.setMessageMap(mm);
		return baseDto;
	}
}
