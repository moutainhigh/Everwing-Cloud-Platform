package com.everwing.server.wy.web.controller.cust.enterprise;

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNewSearch;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.wy.api.cust.enterprise.EnterpriseCustApi;
import com.everwing.coreservice.wy.api.sys.TSysImportExportApi;
import com.everwing.server.wy.web.controller.sys.TSysImportExportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings({ "deprecation", "rawtypes" })
@Controller
@RequestMapping(value="/EnterpriseCustNew")
public class EnterpriseCustController {

	@Autowired
	private EnterpriseCustApi enterpriseCustApi;
	
	@Autowired
    private TSysImportExportApi tSysImportExportApi;

    @Autowired
    private SpringRedisTools springRedisTools;
	
	//查询所有企业客户信息
	@RequestMapping(value="/listAllEnterpriseCustNewRestful",method=RequestMethod.GET)
    public @ResponseBody BaseDto listAllEnterpriseCustNewRestful(HttpServletRequest req){
		return BaseDtoUtils.getDto(this.enterpriseCustApi.listAllEnterpriseCust(CommonUtils.getCompanyId(req)));
	}
	
	/**
     * 根据id查询企业客户基本信息
     * @param enterpriseId
     * @return
     */
	@RequestMapping(value="/getEnterpriseCustNewByIdRestful/{enterpriseId}",method=RequestMethod.GET)
    public @ResponseBody BaseDto getEnterpriseCustNewByIdRestful(HttpServletRequest req , @PathVariable("enterpriseId") String enterpriseId){
		return BaseDtoUtils.getDto(this.enterpriseCustApi.getEnterpriseById(CommonUtils.getCompanyId(req),enterpriseId));
	}


    
    @RequestMapping(value="/listPageEnterpriseCustNewRestful",method=RequestMethod.POST)
    public @ResponseBody BaseDto listPageEnterpriseCustNewRestful(HttpServletRequest req , @RequestBody EnterpriseCustNew enterpriseCustNew){
    	return BaseDtoUtils.getDto(this.enterpriseCustApi.listPageEnterpriseCust(CommonUtils.getCompanyId(req),enterpriseCustNew));
    }
    
    @RequestMapping(value="/listPageByParams",method=RequestMethod.POST)
    public @ResponseBody BaseDto listPageByParams(HttpServletRequest req , @RequestBody EnterpriseCustNew enterpriseCustNew){
    	return BaseDtoUtils.getDto(this.enterpriseCustApi.listPageParams(CommonUtils.getCompanyId(req),enterpriseCustNew));
    }
    
    /**
     * 根据条件查询企业客户信息
     * @param enterpriseCustNew
     */
    @RequestMapping(value="/findEnterpriseCustNewRestful",method=RequestMethod.POST)
    public @ResponseBody BaseDto findEnterpriseCustNewRestful(HttpServletRequest req , @RequestBody EnterpriseCustNew enterpriseCustNew){
    	return BaseDtoUtils.getDto(this.enterpriseCustApi.findEnterpriseCust(CommonUtils.getCompanyId(req),enterpriseCustNew));
    }
    

    

    
    @RequestMapping(value="/listPageEnterpriseCustNewHelp",method=RequestMethod.POST)
    public @ResponseBody BaseDto listPageEnterpriseCustNewHelp(HttpServletRequest req , @RequestBody EnterpriseCustNew enterpriseCustNew){
    	return BaseDtoUtils.getDto(this.enterpriseCustApi.listPageEnterpriseCustNewHelp(CommonUtils.getCompanyId(req), enterpriseCustNew));
    }

    /**
     * 根据企业名称查询企业信息
     * @param enterpriseCustNew
     * @return
     */
    @RequestMapping(value="/listPageEnterpriseByName",method=RequestMethod.POST)
    public @ResponseBody BaseDto listPageEnterpriseByName(HttpServletRequest req , @RequestBody EnterpriseCustNew enterpriseCustNew){
    	return BaseDtoUtils.getDto(this.enterpriseCustApi.listPageEnterpriseByName(CommonUtils.getCompanyId(req), enterpriseCustNew));
    }

    @RequestMapping(value="/listPageEnterprise",method=RequestMethod.POST)
    public @ResponseBody BaseDto listPageEnterprise(HttpServletRequest req , @RequestBody EnterpriseCustNew enterpriseCustNew){
    	return BaseDtoUtils.getDto(this.enterpriseCustApi.listPageEnterprise(CommonUtils.getCompanyId(req), enterpriseCustNew));
    }
    
   



    @RequestMapping(value="/getEnterpriseCustNewByName/",method=RequestMethod.POST)
    @ResponseBody
    public BaseDto getPersonCustNewByName(HttpServletRequest req , @RequestBody EnterpriseCustNew enterpriseCustNew){
        return BaseDtoUtils.getDto(this.enterpriseCustApi.getEnterpriseCustNewByName(CommonUtils.getCompanyId(req), enterpriseCustNew));
    }



    /**  ------------------------------------------ 分割线 -----------------------------------------------------**/

    /**
     * 通过企业名称 ，编号地址等条件查询业主的信息 分页
     * @param enterpriseCustNewSearch
     */
    @RequestMapping(value="/listPageEnterpriseCustNewByCondition",method=RequestMethod.POST)
    public @ResponseBody BaseDto listPageEnterpriseCustNewByCondition(HttpServletRequest req ,  @RequestBody EnterpriseCustNewSearch enterpriseCustNewSearch){
        return BaseDtoUtils.getDto(this.enterpriseCustApi.listPageEnterpriseCustNewByCondition(CommonUtils.getCompanyId(req),enterpriseCustNewSearch));
    }




    /**
     * 根据id删除企业客户基本信息
     * @param enterpriseId
     */
    @RequestMapping(value="/deleteEnterpriseCustNewRestful/{enterpriseId}",method=RequestMethod.GET)
    public @ResponseBody BaseDto deleteEnterpriseCustNewRestful(HttpServletRequest req , @PathVariable String enterpriseId){
        return BaseDtoUtils.getDto(this.enterpriseCustApi.deleteEnterpriseCust(CommonUtils.getCompanyId(req),enterpriseId));
    }

    /**
     * 修改企业客户基本信息
     * @param enterpriseCustNew
     */
    @RequestMapping(value="/updateEnterpriseCustNewRestful",method=RequestMethod.POST)
    public @ResponseBody BaseDto updateEnterpriseCustNewRestful(HttpServletRequest req , @RequestBody EnterpriseCustNew enterpriseCustNew){
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return BaseDtoUtils.getDto(this.enterpriseCustApi.updateEnterpriseCust(ctx,enterpriseCustNew));
    }

    /**
     * 添加企业客户基本信息
     * @param enterpriseCustNew
     */
    @RequestMapping(value="/insertEnterpriseCustNew",method=RequestMethod.POST)
    public @ResponseBody BaseDto insertEnterpriseCustNew(HttpServletRequest req , @RequestBody EnterpriseCustNew enterpriseCustNew){
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return BaseDtoUtils.getDto(this.enterpriseCustApi.insertEnterprise(ctx,enterpriseCustNew));
    }


    /**
     * web端导入入口
     * 这部分代码，你只需要修改方法名和访问地址value的值即可
     *
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "导入企业客户", operationType = OperationEnum.Import)
    @RequestMapping(value = "/importEnpriseCustNew/{projectCode}", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap importBuilding(@PathVariable String projectCode) {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        MessageMap mm = new MessageMap();
        Object obj = springRedisTools.getByKey(String.format(TSysImportExportController.UPLOAD_FILE_KEY,ctx.getLoginName()));
        if(obj != null){
            springRedisTools.deleteByKey(String.format(TSysImportExportController.UPLOAD_FILE_KEY,ctx.getLoginName()));
            String batchNo = obj.toString();
            TSysImportExportSearch tSysImportExportRequest = new TSysImportExportSearch();
            tSysImportExportRequest.setProjectCode(projectCode);
            tSysImportExportRequest.setBatchNo(batchNo);
            RemoteModelResult remoteModelResult = enterpriseCustApi.importEnpriseCustNew(ctx, tSysImportExportRequest);
            if (!remoteModelResult.isSuccess()) {
                mm.setFlag(MessageMap.INFOR_ERROR);
                mm.setMessage(remoteModelResult.getMsg());
            }
        }

        return mm;
    }



}
