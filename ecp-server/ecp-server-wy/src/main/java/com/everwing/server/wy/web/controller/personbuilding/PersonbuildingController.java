package com.everwing.server.wy.web.controller.personbuilding;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.PropertiesHelper;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.personbuilding.BindBuilding;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingNew;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.wy.api.personbuilding.PersonbuildingApi;
import com.everwing.coreservice.wy.api.sys.TSysImportExportApi;

/**
 * @describe 客户资产绑定业务控制器
 * @author QHC
 * @date 2017-04-10
 */
@Controller
@RequestMapping(value="/PersonBuildingNew")
public class PersonbuildingController {

	@Autowired
	private PersonbuildingApi PersonbuildingApi;//控制层调用api接口
	
	@Autowired
	private TSysImportExportApi tSysImportExportApi;
	
	
	@GetMapping("/findBuildingByCustId")
	public @ResponseBody BaseDto findBuildingByCustId(String custId){
		return  BaseDtoUtils.getDto( PersonbuildingApi.listBudildingByCustId(custId));
	}
	
	/**
	 * @describe 添加客户与建筑信息关联
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  获取数据源信息必须参数
              personBuildingNew 客户资产绑定信息
	 * @return 操作结果信息
	 */
	@RequestMapping(value="/addPersonBuildingNewRestful",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addPersonBuildingNewRestful(HttpServletRequest req,@RequestBody PersonBuildingNew personBuildingNew){
		return  this.PersonbuildingApi.addPersonBuildingNewRestful(CommonUtils.getCompanyId(req),personBuildingNew).getModel();
	}

	/**
	 * @describe 通过houseId查询房屋和人物的信息
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  获取数据源信息必须参数
	 * @return 操作结果信息
	 */
	@RequestMapping(value="/listAllPersonAndHouseByHouseId/{houseId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto listAllPersonAndHouseByHouseId(HttpServletRequest req,@PathVariable String houseId){
		return  this.PersonbuildingApi.listAllPersonAndHouseByHouseId(CommonUtils.getCompanyId(req), houseId).getModel();
	}
	

	
	@RequestMapping(value="/listAll",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto listAll(HttpServletRequest req){
		return  this.PersonbuildingApi.listAll(CommonUtils.getCompanyId(req)).getModel();
	}
	
	/**
	 * @describe 根据建筑结构id查询物品包和业主信息
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  获取数据源信息必须参数
	 * @return 操作结果信息
	 */
	@RequestMapping(value="/listPagePersonBuildingNew",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPagePersonBuildingNew(HttpServletRequest req,@RequestBody PersonBuildingNew personBuildingNew){
		return  this.PersonbuildingApi.listPagePersonBuildingNew(CommonUtils.getCompanyId(req), personBuildingNew).getModel();
	}
	
	
	
	/**
	 * @describe 通过houseId查询房屋和人物的信息
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  获取数据源信息必须参数
	 * @return 操作结果信息
	 */
	@RequestMapping(value="/listPersonBuildingNewone/{buildingStructureId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto listPersonBuildingNewone(HttpServletRequest req , @PathVariable String buildingStructureId){
		return  this.PersonbuildingApi.listPersonBuildingNewone(CommonUtils.getCompanyId(req),buildingStructureId).getModel();
	}
	
	
	/**
	 * @describe 企业客户资产绑定
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  获取数据源信息必须参数
              personBuildingNew 客户资产绑定信息
	 * @return 操作结果信息
	 */
	@RequestMapping(value="/addPersonBuildingEnterpriseNewRestful",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addPersonBuildingEnterpriseNewRestful(HttpServletRequest req,@RequestBody PersonBuildingNew personBuildingNew){
		return  this.PersonbuildingApi.addPersonBuildingEnterpriseNewRestful(CommonUtils.getCompanyId(req),personBuildingNew).getModel();
	}
	
	/**
	 * @describe 根据企业id查询与企业及企业关联的房屋的员工的关系链
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  获取数据源信息必须参数
	 * @return 操作结果信息
	 */
	@RequestMapping(value="/getRelationOfemplers",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getRelationOfemplers(HttpServletRequest req , @RequestBody PersonBuildingNew personBuildingNew){
		return  this.PersonbuildingApi.getRelationOfemplers(CommonUtils.getCompanyId(req),personBuildingNew).getModel();
	}

	/**
	 * @describe 根据客户id查询客户建筑关联
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  获取数据源信息必须参数
	 * @return 操作结果信息
	 */
	@RequestMapping(value="/selectPersonBuildingNewByCustId",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto selectPersonBuildingNewByCustId(HttpServletRequest req , @RequestBody PersonBuildingNew personBuildingNew){
		return  this.PersonbuildingApi.selectPersonBuildingNewByCustId(CommonUtils.getCompanyId(req),personBuildingNew).getModel();
	}
	
	/**
	 * @describe  查询个人关系集
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  获取数据源信息必须参数
	 * @return 操作结果信息
	 */
	@RequestMapping(value="/getRelationOfemplersByCustId/{custId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getRelationOfemplersByCustId(HttpServletRequest req ,@PathVariable String custId){
		return  this.PersonbuildingApi.getRelationOfemplersByCustId(CommonUtils.getCompanyId(req),custId).getModel();
	}
	
	
	/**
	 * @describe 根据关系id修改，删除（关系启用/停止）关系链信息
	 * @param CommonUtils.getCompanyId(req)  所有方法都需要此cookie值用于获取数据源信息 
	 * @author QHC
	 * @return MessageMap
	 */
	@RequestMapping(value="/updataPersonBuildingById",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap updataPersonBuildingById(HttpServletRequest req,@RequestBody PersonBuildingNew personBuildingNew){
		return this.PersonbuildingApi.updataPersonBuildingById(CommonUtils.getCompanyId(req),personBuildingNew).getModel();
	}
	
	
	
	/**
	 * @describe 根据关系id修改，删除（关系启用/停止）关系链信息
	 * @param CommonUtils.getCompanyId(req)  所有方法都需要此cookie值用于获取数据源信息 
	 * @author QHC
	 * @return MessageMap
	 */
	@RequestMapping(value="/deletePersonBuildingById",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap deletePersonBuildingById(HttpServletRequest req,@RequestBody List<PersonBuildingNew> personBuildingNews){
		return this.PersonbuildingApi.deletePersonBuildingById(CommonUtils.getCompanyId(req),personBuildingNews).getModel();
	}
	
	
	
	/**
	 * @describe  查询个人关系集
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  获取数据源信息必须参数
	 * @return 操作结果信息
	 */
	@RequestMapping(value="/getRelationBycustId/{custId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getRelationBycustId(HttpServletRequest req ,@PathVariable String custId){
		return  this.PersonbuildingApi.getRelationBycustId(CommonUtils.getCompanyId(req),custId).getModel();
	}
	

	/**
	 * @describe  查询个人关系集
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  获取数据源信息必须参数
	 * @return 操作结果信息
	 */
	@RequestMapping(value="/listPageHouseByenterpriseId",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto listPageHouseByenterpriseId(HttpServletRequest req ,@RequestBody PersonBuildingNew personBuildingNew){
		return  this.PersonbuildingApi.listPageHouseByenterpriseId(CommonUtils.getCompanyId(req),personBuildingNew).getModel();
	}
    
	
	/**
	 * @param CommonUtils.getCompanyId(req)  所有方法都需要此cookie值用于获取数据源信息 
	 * @author QHC
	 * @return MessageMap
	 */
	@RequestMapping(value="/deletePersonBuilding",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap deletePersonBuilding(HttpServletRequest req,@RequestBody PersonBuildingNew personBuildingNew){
		return this.PersonbuildingApi.deletePersonBuilding(CommonUtils.getCompanyId(req),personBuildingNew).getModel();
	}
	

	@RequestMapping(value="/importFile",method=RequestMethod.POST)
	@ResponseBody
  	public MessageMap importFile(HttpServletRequest req , @RequestBody Annex annex){
    	return this.PersonbuildingApi.importFile(CommonUtils.getCompanyId(req),annex).getModel();
    }

	
	/**
	 * @describe 导入环境迁移数据
	 * @param CommonUtils.getCompanyId(req)
	 * @param annex
	 */
	@RequestMapping(value="/inportOtherExcel",method=RequestMethod.POST)
	@ResponseBody
  	public MessageMap inportOtherExcel(HttpServletRequest req , @RequestBody Annex annex){
		return this.PersonbuildingApi.inportOtherExcel(CommonUtils.getCompanyId(req),annex).getModel();
    }
	
	/**
	 * @describe 根据建筑id获取该建筑关联关系,及对应的个人/企业客户集合
	 * @param CommonUtils.getCompanyId(req)
	 * @param buildingId
	 * @return 查询结果集
	 */
	@RequestMapping(value="/getInfosByBuildingId/{buildingId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getInfosByBuildingId(HttpServletRequest req,@PathVariable String buildingId){

		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		RemoteModelResult<BaseDto> result = PersonbuildingApi.getInfosByBuildingId(ctx, buildingId);
		if (result.isSuccess()) {
			baseDto = result.getModel();
		} else {
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(result.getMsg());
		}
		baseDto.setMessageMap(mm);
		return baseDto;


	}
	

	
	
	/*********************************************   资产绑定与解绑 start   *************************************************/
	
	/**
	 * @describe 资产绑定
	 * @param personBuildingNews(req)
	 * @param req
	 */
	@RequestMapping(value="/assetBinding",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap assetBinding(HttpServletRequest req,@RequestBody List<PersonBuildingNew> personBuildingNews){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return this.PersonbuildingApi.assetBinding(ctx,personBuildingNews).getModel();
	}
	
	
	/**
	 * @describe 解除资产绑定
	 * @param CommonUtils.getCompanyId(req)
	 * @param personBuildingNew
	 */
	@RequestMapping(value="/relieveAssetBinding",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap relieveAssetBinding(HttpServletRequest req,@RequestBody List<PersonBuildingNew> personBuildingNews){
		return this.PersonbuildingApi.relieveAssetBinding(CommonUtils.getCompanyId(req),personBuildingNews).getModel();
	}
	
	
	/*********************************************   资产绑定与解绑 end   *************************************************/
	
	
	/*********************************************   可视对讲   *************************************************/
	
	
	@RequestMapping(value="/getHouseNewByHouseId/{houseId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getHouseNewByHouseId(HttpServletRequest req,@PathVariable String houseId){
		return  this.PersonbuildingApi.getHouseNewByHouseId(CommonUtils.getCompanyId(req),houseId).getModel();
	}
	
	
	@RequestMapping(value="/getSipByStuctureId/{buildingStructureId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getSipByStuctureId(HttpServletRequest req,@PathVariable String buildingStructureId){
		return  this.PersonbuildingApi.getSipByStuctureId(CommonUtils.getCompanyId(req),buildingStructureId).getModel();
	}
	
	
	/**
	 * @describe 根据建筑结构id获取相关联的person
	 * @param CommonUtils.getCompanyId(req) 得到数据源链接
	 * @param buildingStruId 建筑结构id
	 * @return
	 */
	@RequestMapping(value="/getPersonIdByBuildingStru/{buildingStruId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getPersonIdByBuildingStru(HttpServletRequest req,@PathVariable String buildingStruId){
		return  this.PersonbuildingApi.getPersonIdByBuildingStru(CommonUtils.getCompanyId(req),buildingStruId).getModel();
	}
	
	
	/**
	 * @describe 根据客户id获取建筑信息
	 * @param CommonUtils.getCompanyId(req)
	 * @param custId
	 * @return
	 */
	@RequestMapping(value="/getPersonBuildingByCustId/{custId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getPersonBuildingByCustId(HttpServletRequest req,@PathVariable String custId){
		return  this.PersonbuildingApi.getPersonBuildingByCustId(CommonUtils.getCompanyId(req),custId).getModel();
	}
	
	/**
	 * @describe 根据客户id获取绑定资产数量
	 * @param CommonUtils.getCompanyId(req)
	 * @param custId
	 * @return
	 */
	@RequestMapping(value="/getPersonBuildingCountByCustId/{custId}",method=RequestMethod.GET)
	@ResponseBody
	public int getPersonBuildingCountByCustId(HttpServletRequest req,@PathVariable String custId){
		return  this.PersonbuildingApi.getPersonBuildingCountByCustId(CommonUtils.getCompanyId(req),custId);
	}
	
	
	/**
	 * @describe 查询家家sip以及室内机sip
	 * @param CommonUtils.getCompanyId(req)
	 * @param custId
	 * @return
	 */
	@RequestMapping(value="/getSipsByStuctureId/{buildingStructureId}",method=RequestMethod.GET)
	@ResponseBody
	public String getSipsByStuctureId(HttpServletRequest req,@PathVariable String buildingStructureId){
		return  this.PersonbuildingApi.getSipsByStuctureId(CommonUtils.getCompanyId(req),buildingStructureId);
	}
	
	
	/**
	 * @describe 根据客户id的集合查询建筑信息
	 * @param CommonUtils.getCompanyId(req)
	 * @param ids 
	 */
	@RequestMapping(value="/getPersonBuildingByCustIdList/{ids}",method=RequestMethod.GET)
	@ResponseBody
	public String getPersonBuildingByCustIdList(HttpServletRequest req,@PathVariable String ids){
		return  this.PersonbuildingApi.getPersonBuildingByCustIdList(CommonUtils.getCompanyId(req),ids);
	}
	
	
	
	@RequestMapping(value="/gteBuildingByPersonIdList/{ids}",method=RequestMethod.GET)
	@ResponseBody
	public String gteBuildingByPersonIdList(HttpServletRequest req,@PathVariable String ids){
		return  this.PersonbuildingApi.gteBuildingByPersonIdList(CommonUtils.getCompanyId(req),ids);
	}
	
	@RequestMapping(value="/getPersonBuildingCountByCustIdList/{ids}",method=RequestMethod.GET)
	@ResponseBody
	public int getPersonBuildingCountByCustIdList(HttpServletRequest req,@PathVariable String ids){
		return  this.PersonbuildingApi.getPersonBuildingCountByCustIdList(CommonUtils.getCompanyId(req),ids);
	}
	
	
	/**
	 * @describe 根据用户的选择为用户导出建筑的绑定信息
	 * @param req 获取cookie中存放的companyId
	 * @param isBindingAssets  导出绑定信息的类别（1.已绑定   2.未绑定  0.全部 ）
	 * @param tcBuilding  建筑信息
	 * @return
	 */
	@RequestMapping(value="/exportBuildingInfos/{isBindingAssets}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap exportBuildingInfos(HttpServletRequest req,@PathVariable String isBindingAssets, @RequestBody BindBuilding bindBuilding){
		return this.PersonbuildingApi.exportBuildingInfos(CommonUtils.getCompanyId(req),isBindingAssets,bindBuilding);
	}
	
	
	@PostMapping("/loadBuildingInfos/{projectId}")
	public @ResponseBody BaseDto loadBuildingInfos(HttpServletRequest req , @PathVariable String projectId){
		return BaseDtoUtils.getDto(this.PersonbuildingApi.loadBuildingInfos(CommonUtils.getCompanyId(req),projectId));
	}
	
	/**
     * web端导入入口
     * 这部分代码，你只需要修改方法名和访问地址value的值即可
     * @param request
     * @return
     */
    @SuppressWarnings("unused")
	@RequestMapping(value = "/importPersonBuilding/{projectId}", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap importPersonCustInfo(HttpServletRequest request,@RequestBody TSysImportExport tSysImportExportRequest,@PathVariable String projectId) {
        MessageMap mm = new MessageMap();

        String batchNo = "";

        String dirMessage = "";

		WyBusinessContext ctx = WyBusinessContext.getContext();

        TSysImportExportList tSysImportExportListExist = null;
        try {
            if(tSysImportExportRequest == null){
                throw new ECPBusinessException("参数[batchNo,fileType,moduleDescription]不能空");
            }

            if(StringUtils.isEmpty(tSysImportExportRequest.getBatchNo())){
                throw new ECPBusinessException("参数[batchNo]不能空");
            }
            if(StringUtils.isEmpty(tSysImportExportRequest.getFileType())){
                throw new ECPBusinessException("参数[fileType]不能空");
            }
            if(StringUtils.isEmpty(tSysImportExportRequest.getModuleDescription())){
                throw new ECPBusinessException("参数[moduleDescription]不能空");
            }

            dirMessage = PropertiesHelper.getInstance("config/commonConf.properties").getDefaultSystemValue("importExcelBaseDir") + File.separator + tSysImportExportRequest.getModuleDescription() + File.separator + "message" + File.separator;

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

            String excelPath = PropertiesHelper.getInstance("config/commonConf.properties").getDefaultSystemValue("importExcelBaseDir") + File.separator + tSysImportExportListExist.getModuleDescription() + File.separator + "excel" +File.separator + tSysImportExportListExist.getBatchNo() + "." + tSysImportExportListExist.getFileType();
            MessageMap msg = PersonbuildingApi.importFile(ctx, batchNo, excelPath,projectId).getModel(); 
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
        }
        return mm;
    }
	
	
}
