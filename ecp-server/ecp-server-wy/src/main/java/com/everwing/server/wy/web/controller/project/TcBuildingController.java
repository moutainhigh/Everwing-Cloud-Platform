package com.everwing.server.wy.web.controller.project;/**
 * Created by wust on 2017/4/18.
 */

import com.esotericsoftware.minlog.Log;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.wy.api.building.TcBuildingApi;
import com.everwing.server.wy.util.ImportExportHelper;
import com.everwing.server.wy.web.controller.sys.TSysImportExportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Function:
 * Reason:
 * Date:2017-4-18 09:08:59
 *
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/project/TcBuildingController")
public class TcBuildingController {

    @Autowired
    private SpringRedisTools springRedisTools;

    @Autowired
    private TcBuildingApi tcBuildingApi;



    /**
     * 分页查询建筑信息
     *
     * @param condition
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "分页查询资产", operationType = OperationEnum.Search)
    @RequestMapping(value = "/listPage", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPage(@RequestBody TcBuildingSearch condition) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tcBuildingApi.listPage(ctx, condition);
        if (result.isSuccess()) {
            baseDto = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    /**
     * 分页查询建筑未关联水表信息
     *
     * @param
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "分页查询建筑未关联水表信息", operationType = OperationEnum.Search)
    @RequestMapping(value = "/listPageUnrelated", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageUnrelated(@RequestBody TcBuildingSearch condition) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tcBuildingApi.listPageUnrelated(ctx, condition);
        if (result.isSuccess()) {
            baseDto = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }



    /**
     * 极速构造建筑树
     * @param projectCode
     * @return
     */
    @RequestMapping(value = "/getBuildingTreeWithProjectCode/{projectCode}", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto getBuildingTreeWithProjectCode(@PathVariable String projectCode) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        String jsonStr = DataDictionaryUtil.getBuildingTreeByProjectCode(ctx.getCompanyId(),projectCode);
        baseDto.setObj(jsonStr);
        baseDto.setMessageMap(mm);
        return baseDto;
    }



    /**
     * web端导入入口
     * 这部分代码，你只需要修改方法名和访问地址value的值即可
     *
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "导入建筑信息", operationType = OperationEnum.Import)
    @RequestMapping(value = "/importBuilding/{projectCode}", method = RequestMethod.POST)
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
            RemoteModelResult remoteModelResult = tcBuildingApi.importTcbuilding(ctx, tSysImportExportRequest);
            if (!remoteModelResult.isSuccess()) {
                mm.setFlag(MessageMap.INFOR_ERROR);
                mm.setMessage(remoteModelResult.getMsg());
            }
        }

        return mm;
    }


    /**
     * 新增建筑结构
     *
     * @param entity
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "新增建筑信息", operationType = OperationEnum.Insert)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap add(@RequestBody TcBuilding entity) {
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();


        // 检测是否正在导入
        mm = ImportExportHelper.checkDoingTask(ctx.getCompanyId(), "building");
        if (!MessageMap.INFOR_SUCCESS.equals(mm.getFlag())) {
            return mm;
        }

        entity.setCreaterId(ctx.getUserId());
        entity.setCreaterName(ctx.getStaffName());
        RemoteModelResult<MessageMap> result = tcBuildingApi.add(ctx, entity);
        if (result.isSuccess()) {
            mm = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    /**
     * 编辑建筑结构
     *
     * @param entity
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "修改建筑信息", operationType = OperationEnum.Modify)
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap modify(@RequestBody TcBuilding entity) {
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        // 检测是否正在导入
        mm = ImportExportHelper.checkDoingTask(ctx.getCompanyId(), "building");
        if (!MessageMap.INFOR_SUCCESS.equals(mm.getFlag())) {
            return mm;
        }

        entity.setModifyId(ctx.getUserId());
        entity.setModifyName(ctx.getStaffName());
        RemoteModelResult result = tcBuildingApi.modify(ctx, entity);
        if (result.isSuccess()) {
            return mm;
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }

    /**
     * 删除建筑结构
     *
     * @param buildingCode
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "删除建筑信息", operationType = OperationEnum.Delete)
    @RequestMapping(value = "/delete/{buildingCode}", method = RequestMethod.DELETE)
    public @ResponseBody
    MessageMap delete(@PathVariable String buildingCode) {
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        // 检测是否正在导入
        mm = ImportExportHelper.checkDoingTask(ctx.getCompanyId(), "building");
        if (!MessageMap.INFOR_SUCCESS.equals(mm.getFlag())) {
            return mm;
        }

        RemoteModelResult result = tcBuildingApi.delete(ctx.getCompanyId(), buildingCode);
        if (result.isSuccess()) {
            mm = result.getModel() == null ? null : (MessageMap) result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }

    /**
     * 获取指定条件的建筑信息
     *
     * @param tcBuildingSearch
     * @return
     */
    @RequestMapping(value = "/findBuildingByCondition", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto findBuildingByCondition(@RequestBody TcBuildingSearch tcBuildingSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult result = tcBuildingApi.findByCondition(ctx, tcBuildingSearch);
        if (result.isSuccess()) {
            baseDto.setLstDto((List) result.getModel());
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }





    /**
     * 统计资产面积
     * @param tcBuildingSearch
     * @return
     */
    @RequestMapping(value = "/collectAssetInfo", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap collectAssetInfo(@RequestBody TcBuildingSearch tcBuildingSearch) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<List<Map<String,String>>> result = tcBuildingApi.collectAssetInfo(ctx, tcBuildingSearch);
        if (result.isSuccess()) {
            List<Map<String,String>> map = result.getModel();
            mm.setObj(map);
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }








    /**
     * 根据项目id获取建筑
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/loadBuildingByProjectId/{projectId}", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto loadBuildingByProjectId(@PathVariable String projectId) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<List<TcBuilding>> result = this.tcBuildingApi.loadBuildingByProjectId(ctx.getCompanyId(), projectId);
        if (result.isSuccess()) {
            baseDto.setLstDto(result.getModel());
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }




    /**
     * 同步最新树到缓存
     * @return
     */
    @RequestMapping(value = "/syncBuildingTree", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap syncBuildingTree() {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<MessageMap> result = tcBuildingApi.syncBuildingTree(ctx);
        if (result.isSuccess()) {
            mm = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


















    /*********************************分割线**********************************************************/





    /**
     * 获取一条建筑信息
     *
     * @param buildingCode
     * @return
     */
    @RequestMapping(value = "/findByBuildingCode/{buildingCode}", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto findByBuildingCode(@PathVariable String buildingCode) {
        BaseDto baseDto = new BaseDto();
        TcBuildingList tcBuilding = null;
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
        tcBuildingSearch.setBuildingCode(buildingCode);
        RemoteModelResult result = tcBuildingApi.findByCondition(ctx, tcBuildingSearch);
        if (result.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_SUCCESS);
            List<TcBuildingList> list = result.getModel() == null ? null : (List<TcBuildingList>) result.getModel();
            tcBuilding = list.get(0);
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setT(tcBuilding);
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    @GetMapping("/loadBuildingByProjectId")
    public @ResponseBody
    BaseDto loadBuildingByProjectIdWithoutTree(String projectId) {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return BaseDtoUtils.getDto(this.tcBuildingApi.loadBuildingByProjectIdWithoutTree(ctx, projectId));
    }


    @PostMapping("/loadBuildingByPickUpTree")
    public @ResponseBody
    BaseDto loadBuildingByPickUpTree(String projectId, String buildingCode, String custId) {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return BaseDtoUtils.getDto(this.tcBuildingApi.loadBuildingByPickUpTree(ctx, projectId, buildingCode, custId));
    }


    @RequestMapping(value = "/countIsChargeObjByProject/{projectId}/{isChargeObj}", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto countIsChargeObjByProject(@PathVariable String projectId, @PathVariable String isChargeObj) {
        BaseDto baseDto = new BaseDto();
        MessageMap msgMap = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        try {
            RemoteModelResult<BaseDto> result = this.tcBuildingApi.countIsChargeObjByProject(ctx, projectId, isChargeObj);
            if (result.isSuccess()) {
                baseDto = result.getModel();
                msgMap = baseDto.getMessageMap();
            } else {
                msgMap.setFlag(MessageMap.INFOR_ERROR);
                msgMap.setMessage(result.getMsg());
            }
        } catch (Exception e) {
            msgMap.setFlag(MessageMap.INFOR_ERROR);
            msgMap.setMessage("系统错误!");
            Log.info(e.getMessage());
        }
        baseDto.setMessageMap(msgMap);
        return baseDto;
    }










    @RequestMapping(value = "/initaccount/{projectId}/{projectName}", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto initaccount(@PathVariable String projectId, @PathVariable String projectName) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        try {
            RemoteModelResult<BaseDto> result = this.tcBuildingApi.initaccount(ctx, projectId, projectName);
            if (result.isSuccess()) {
                baseDto = result.getModel();
                mm = baseDto.getMessageMap();
            } else {
                mm.setFlag(MessageMap.INFOR_ERROR);
                mm.setMessage(result.getMsg());
            }
        } catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("系统异常!");
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

}
