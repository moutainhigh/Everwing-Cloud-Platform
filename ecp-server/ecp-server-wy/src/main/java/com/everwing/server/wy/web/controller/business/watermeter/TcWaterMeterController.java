package com.everwing.server.wy.web.controller.business.watermeter;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcHydroMeterOperationRecord;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.platform.api.CommonQueryApi;
import com.everwing.coreservice.wy.api.business.watermeter.TcWaterMeterApi;
import com.everwing.coreservice.wy.api.sys.TSysImportExportApi;
import com.everwing.server.wy.util.ImportExportHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
 * @describe 水表基础数据管理控制器
 * @author QHC
 * @date 2017-05-04
 */
@Controller
@RequestMapping(value="/waterMeter")
public class TcWaterMeterController {

	@Autowired
	private TcWaterMeterApi waterMeterApi;
	@Autowired
	private CommonQueryApi commonQueryApi;
	
	@Autowired
    private TSysImportExportApi tSysImportExportApi;
	
	private Logger logger=Logger.getLogger(TcWaterMeterController.class);
	
	/**
	 * @describe 加载现有水表信息
	 * @author QHC
	 *        orderColum  排序字段
	 * @date 2017-05-04
	 */
	@RequestMapping(value="/listPageWaterMeterInfos",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto<String, Object> listPageWaterMeterInfos(HttpServletRequest req,@RequestBody TcWaterMeter tcWaterMeter){
		BaseDto<String, Object> baseDto=waterMeterApi.listPageWaterMeterInfos(CommonUtils.getCompanyId(req),tcWaterMeter).getModel();
		return baseDto;
	}
	
	/**
	 * @describe 新增水表信息
	 * @author QHC
	 * @date 2017-05-04
	 */
	@RequestMapping(value="/addWaerMeterInfo",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addWaerMeterInfo(HttpServletRequest req,@RequestBody TcWaterMeter tcWaterMeter){
		return waterMeterApi.addWaerMeterInfo(CommonUtils.getCompanyId(req), tcWaterMeter).getModel();
	}
	
	/**
	 * @describe 修改水表信息
	 * @author QHC
	 * @date 2017-05-04
	 */
	@RequestMapping(value="/updateWaerMeterInfo",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap updateWaerMeterInfo(HttpServletRequest req,@RequestBody TcWaterMeter tcWaterMeter){
		return waterMeterApi.updateWaerMeterInfo(CommonUtils.getCompanyId(req), tcWaterMeter).getModel();
	}
	
	
	/**
	 * @describe 单个水表的启用和停用
	 * @author QHC
	 * @date 2017-05-04
	 * @param meterId String 水表id
	 * 			state int 当前水表的状态0:启用,1：停用
	 */
	@RequestMapping(value="/startStopWaerMeterByOne/{meterId}/{state}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap startStopWaerMeterByOne(HttpServletRequest req,@RequestBody TcHydroMeterOperationRecord tcHydroMeterOperationRecord,
			@PathVariable String meterId,@PathVariable int state){
		return waterMeterApi.startStopWaerMeterByOne(CommonUtils.getCompanyId(req), tcHydroMeterOperationRecord,meterId,state).getModel();
	}
	
/**
	 * @describe 删除水表基础数据信息操作
	 * @author QHC
	 * @date 2017-05-04
	 */
	@RequestMapping(value="/deleteWaterMeterInfos/{ids}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap deleteWaterMeterInfos(HttpServletRequest req,@PathVariable String ids){
		return waterMeterApi.deleteWaterMeterInfos(CommonUtils.getCompanyId(req),ids).getModel();
	}
	
	
	/**
	 * @describe 水表的更换操作
	 * @author QHC
	 * @date 2017-05-04
	 */
	@RequestMapping(value="/replaceWaerMeterByOne",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap replaceWaerMeterByOne(HttpServletRequest req,@RequestBody TcHydroMeterOperationRecord tcHydroMeterOperationRecord){
		return waterMeterApi.replaceWaerMeterByOne(CommonUtils.getCompanyId(req), tcHydroMeterOperationRecord).getModel();
	}
	
	
	/**
	 * @describe 水表详情信息中对附件的加载(根据水表id加载其关联附件信息)
	 * @author QHC
	 * @date 2017-05-04
	 */
	@RequestMapping(value="/loadEnclosureInfoByid",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto<String, Object> loadEnclosureInfoByid(HttpServletRequest req,@RequestBody TcWaterMeter tcWaterMeter){
		return waterMeterApi.loadEnclosureInfoByid(CommonUtils.getCompanyId(req), tcWaterMeter).getModel();
	}
	
	/**
	 * @describe 水表详情信息中对水表历史抄表记录的加载
	 * @author QHC
	 * @date 2017-05-04
	 */
	@RequestMapping(value="/listPageLoadMeterReadingRecordById",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto<String, Object> listPageLoadMeterReadingRecordById(HttpServletRequest req,@RequestBody TcWaterMeter tcWaterMeter){
		return waterMeterApi.listPageLoadMeterReadingRecordById(CommonUtils.getCompanyId(req), tcWaterMeter).getModel();
	}
	
	/**
	 * @describe 根据条件加载符合条件的水表信息(更换水表时选择新水表)
	 * @author QHC
	 * @date 2017-05-04
	 */
	@RequestMapping(value="/listPageloadWaterMeterForChange",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto<String, Object> listPageloadWaterMeterForChange(HttpServletRequest req,@RequestBody TcWaterMeter tcWaterMeter){
		return waterMeterApi.listPageloadWaterMeterForChange(CommonUtils.getCompanyId(req), tcWaterMeter).getModel();
	}
	
	/**
	 * @describe 管理员手动添加水表信息时校验水表编号是否已存在
	 * @author QHC
	 * @date 2017-05-04
	 */
	@RequestMapping(value="/checkWaterMeterCode/{code}",method=RequestMethod.GET)
	@ResponseBody
	public MessageMap checkWaterMeterCode(HttpServletRequest req,@PathVariable String code){
		return waterMeterApi.checkWaterMeterCode(CommonUtils.getCompanyId(req), code).getModel();
	}
	
	
	/**
     * web端导入入口
     * 这部分代码，你只需要修改方法名和访问地址value的值即可
     * @param request
     * @return
     */
    @RequestMapping(value = "/importWaterMeterInfo", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap importWaterMeterInfo(HttpServletRequest request,@RequestBody TSysImportExport tSysImportExportRequest) {
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

//            dirMessage = PropertiesHelper.getInstance("config/commonConf.properties").getDefaultSystemValue("importExcelBaseDir") + File.separator + tSysImportExportRequest.getModuleDescription() + File.separator + "message" + File.separator;

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

//            String excelPath = PropertiesHelper.getInstance("config/commonConf.properties").getDefaultSystemValue("importExcelBaseDir") + File.separator + tSysImportExportListExist.getModuleDescription() + File.separator + "excel" +File.separator + tSysImportExportListExist.getBatchNo() + "." + tSysImportExportListExist.getFileType();
            MessageMap msg = waterMeterApi.importWaterMeter(ctx,batchNo,null).getModel();//后面不会用到excelpath
            RemoteModelResult<MessageMap> messageMapRemoteModelResult =new RemoteModelResult<MessageMap>(msg);
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
	 * @describe 用户通过用户id查询用户信息（调用平台的接口）
	 * @author QHC
	 * @date 2017-05-24
	 */
	@RequestMapping(value="/selectByPrimaryKey/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Account selectByPrimaryKey(HttpServletRequest req,@PathVariable String id){
		RemoteModelResult<Account> result= commonQueryApi.selectByPrimaryKey(Account.class, id);
		Account account=result.getModel();
		return account;
		
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/countMeters",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto countMeters(HttpServletRequest req,@RequestBody TcWaterMeter entity){
		return BaseDtoUtils.getDto(this.waterMeterApi.countMeters(CommonUtils.getCompanyId(req),entity));
	}
	
	/**
	 * @describe 根据用户选择的表的级别，查询其父表code信息提供选择
	 * @author QHC
	 * @date 2017-05-24
	 */
	@RequestMapping(value="/listPageWaterMeterByLevel",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto<String, Object> listPageWaterMeterByLevel(HttpServletRequest req,@RequestBody TcWaterMeter waterMeter){
		BaseDto<String, Object> baseDto=waterMeterApi.listPageWaterMeterByLevel(CommonUtils.getCompanyId(req),waterMeter).getModel();
		return baseDto;
	}
	
	
	/**
	 * 导入模板下载
	 */
	@RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
	public void downloadTemplate(HttpServletResponse response) {
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileName = "水表导入模板.xls";
        String filePath = "excelTemplate/export/water_meter_template.xls";
        
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
     * 加载建筑树,一次性加载
     * @param req
     * @param projectId
     * @return
     */
	@RequestMapping(value="/loadBuildingAndMeter/{projectId}/{meterType}",method=RequestMethod.POST)
    public @ResponseBody BaseDto loadBuildingAndMeter(HttpServletRequest req, @PathVariable String projectId, @PathVariable String meterType){
    	BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
    	baseDto = this.waterMeterApi.loadBuildingAndMeter(ctx.getCompanyId(), projectId,meterType).getModel();
        return baseDto;
    }



	/**
	 * @describe
	 * @author ZYF
	 * @date 2018-12-12
	 */
	@RequestMapping(value="/listPageWaterMeterByBuildingCode",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageWaterMeterByBuildingCode(@RequestBody TcWaterMeter tcWaterMeter){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		RemoteModelResult<BaseDto> result =waterMeterApi.listPageWaterMeterByBuildingCode(WyBusinessContext.getContext(),tcWaterMeter);
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
