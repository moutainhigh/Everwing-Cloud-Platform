package com.everwing.server.wy.web.controller.configuration.tbsassetaccount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.wy.api.configuration.tbcassetacount.TBsAssetAccountApi;
import com.everwing.coreservice.wy.api.sys.TSysImportExportApi;
import com.everwing.server.wy.util.ImportExportHelper;

@Controller
@RequestMapping(value="/tBsAssetAccount")
public class TBsAssetAccountController {

	@Autowired
	private TBsAssetAccountApi  tBsAssetAccountApi;
	
	@Autowired
	private TSysImportExportApi tSysImportExportApi;
	
	
	@RequestMapping(value="/queryTotalArrears",method=RequestMethod.POST)
	public @ResponseBody BaseDto queryTotalArrears(){
		WyBusinessContext ctx =null;
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			ctx = WyBusinessContext.getContext();
			List<TcBuildingList> list = new ArrayList<TcBuildingList>();
			TcBuildingList tcBuildingList = new TcBuildingList();
			tcBuildingList.setProjectId("1001");
			tcBuildingList.setBuildingCode("1001_20170727172034178_10000_10008_10000_10001_10013_10002");
			list.add(tcBuildingList);
			RemoteModelResult<BaseDto> result = this.tBsAssetAccountApi.queryTotalArrears(list);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统错误!");
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
	@PostMapping("/importBeforeDatas/{oprUserId}")
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Billing,businessName="导入旧数据",operationType= OperationEnum.Modify)
	public @ResponseBody MessageMap importBeforeDatas(HttpServletRequest request,@RequestBody TSysImportExport tSysImportExportRequest, @PathVariable("oprUserId") String oprUserId){
		MessageMap msgMap = new MessageMap();
		
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
            
            MessageMap msg = this.tBsAssetAccountApi.importBeforeDatas(ctx,batchNo,null,oprUserId).getModel();//后面不会用到excelpath
            RemoteModelResult<MessageMap> messageMapRemoteModelResult = new RemoteModelResult<MessageMap>(msg);
            if(messageMapRemoteModelResult.isSuccess()){
                msgMap = messageMapRemoteModelResult.getModel();
            }else{
            	msgMap.setFlag(MessageMap.INFOR_ERROR);
            	msgMap.setMessage(messageMapRemoteModelResult.getMsg());
            }
            
        }catch (Exception e) {
            msgMap.setFlag(MessageMap.INFOR_ERROR);
            msgMap.setMessage(e.getMessage());
            e.printStackTrace();
        }finally {
        	//导入后执行对导入状态的修改
        	String stauts="";
        	TSysImportExport tSysImportExport = new TSysImportExport();
        	BeanUtils.copyProperties(tSysImportExportListExist,tSysImportExport);
        	tSysImportExport.setEndTime(new Date());
        	 if(MessageMap.INFOR_SUCCESS.equals(msgMap.getFlag())){
                 stauts = ImportExportEnum.succeed.name();
             }else if(MessageMap.INFOR_WARNING.equals(msgMap.getFlag())) {
            	 stauts = ImportExportEnum.partial_success.name();
             }else {
            	 stauts = ImportExportEnum.failed.name();
             }
        	
        	tSysImportExport.setStatus(stauts);
        	tSysImportExportApi.modify(CommonUtils.getCompanyId(request),tSysImportExport);

            // 写入本地
            ImportExportHelper.writeTxt(dirMessage,batchNo,msgMap.getMessage());
        }
        
		return msgMap;
	}

	
}
