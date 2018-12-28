package com.everwing.server.wy.web.controller.project;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectMeterSearch;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.wy.api.building.TcBuildingApi;
import com.everwing.coreservice.wy.api.building.TcSurfaceElectApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/project/TcSurfaceElectController")
public class TcSurfaceElectController {

  @Autowired
    TcSurfaceElectApi  tcSurfaceElectApi;


    @Autowired
    private TcBuildingApi tcBuildingApi;

    /**
     * 查询电表信息
     *
     * @param
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "listPageElectMeterInfos", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageElectMeterInfos(@RequestBody TcElectMeterSearch  electMeter) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tcSurfaceElectApi.listPageElectMeterInfos(ctx, electMeter);
        if (!result.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        } else {
            baseDto = result.getModel();
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }
    /**
     * 查询表级别为非终端表
     *
     * @param
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "listPageParentInfos", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageParentInfos(@RequestBody TcElectMeterSearch  electMeter) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tcSurfaceElectApi.listPageParentInfos(ctx, electMeter);
        if (!result.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        } else {
            baseDto = result.getModel();
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    /**
     * 删除电表信息
     */
    @RequestMapping(value = "deleteSurface/{Id}", method = RequestMethod.GET)
    public  @ResponseBody
    MessageMap deleteSurface(HttpServletRequest request, @PathVariable String Id)    {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult result =tcSurfaceElectApi.delete(ctx, Id);
        if (result.isSuccess()) {
            mm = result.getModel() == null ? null : (MessageMap) result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    /**
     * 修改水表信息
     * @param
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "修改水表信息", operationType = OperationEnum.Modify)
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap modify(@RequestBody TcElectMeter  tcElectMeter) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        tcElectMeter.setModifyid(ctx.getUserId());
        tcElectMeter.setModifyName(ctx.getStaffName());
        RemoteModelResult result =tcSurfaceElectApi.modify(ctx, tcElectMeter);
        if (result.isSuccess()) {
            return mm;
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }

    /**
     * 新增水表信息
     * @param
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "新增水表信息", operationType = OperationEnum.Insert)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap add(@RequestBody TcElectMeter  tcElectMeter) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        tcElectMeter.setCreateid(ctx.getUserId());
        tcElectMeter.setCreaterName(ctx.getStaffName());
        RemoteModelResult<MessageMap> result =tcSurfaceElectApi.add(ctx, tcElectMeter);
        if (result.isSuccess()) {
            mm = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }




    /**
     *
     *建筑树
     * @param
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "details", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto aaa() {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        String projectId = "21";
        RemoteModelResult<List<TcBuilding>> result = this.tcBuildingApi.loadBuildingByProjectId(ctx.getCompanyId(), projectId);
        if (result.isSuccess()) {

            final List<TcBuilding> tcBuildings = result.getModel();
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tcBuildings)){
                JSONArray jsonArray = new JSONArray(10000);
                JSONObject rootJSONObject = new JSONObject();
                rootJSONObject.put("buildingCode","jzjg");
                rootJSONObject.put("name","万科魅力之");

                final Map<String,List<TcBuilding>> groupBuildingByPidMap  = groupBuildingByPid(tcBuildings);
                List<TcBuilding> childrenList = groupBuildingByPidMap.get(rootJSONObject.getString("buildingCode"));
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(childrenList)){
                    final JSONArray children = new JSONArray(100);
                    for (TcBuilding tcBuilding : childrenList) {
                        String buildingCode = tcBuilding.getBuildingCode();
                        JSONObject child = new JSONObject();
                        child.put("buildingCode",buildingCode);
                        child.put("name",tcBuilding.getBuildingName());

                        lookupChildrenNode(groupBuildingByPidMap,child,buildingCode);

                        children.add(child);
                    }
                    rootJSONObject.put("items",children);
                }

                jsonArray.add(rootJSONObject);
                baseDto.setObj(jsonArray.toJSONString());
            }


        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);

        return baseDto;
    }

    /**
     * 递归寻找子节点
     * @param groupBuildingByPidMap
     * @param pid
     */
    private void lookupChildrenNode(final Map<String,List<TcBuilding>> groupBuildingByPidMap,final JSONObject child,String pid){
        List<TcBuilding> childrenList = groupBuildingByPidMap.get(pid);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(childrenList)){
            JSONArray jsonArray = new JSONArray(100);
            for (TcBuilding tcBuilding : childrenList) {
                String buildingCode = tcBuilding.getBuildingCode();
                JSONObject childTemp = new JSONObject();
                childTemp.put("buildingCode",buildingCode);
                childTemp.put("name",tcBuilding.getBuildingName());
                lookupChildrenNode(groupBuildingByPidMap,childTemp,buildingCode);
                jsonArray.add(childTemp);
            }
            child.put("items",jsonArray);
        }
    }


    /**
     * 根据pid分组建筑结构数据
     * @param tcBuildings
     * @return
     */
    private Map<String,List<TcBuilding>> groupBuildingByPid(final List<TcBuilding> tcBuildings){
        Map<String,List<TcBuilding>> resultMap = new HashMap<>(tcBuildings.size());
        if(CollectionUtils.isNotEmpty(tcBuildings)){
            for (TcBuilding tcBuilding : tcBuildings) {
                String pid = tcBuilding.getPid();
                if(resultMap.containsKey(pid)){
                    List<TcBuilding> tcBuildingsTemp = resultMap.get(pid);
                    tcBuildingsTemp.add(tcBuilding);
                    resultMap.put(pid,tcBuildingsTemp);
                }else{
                    List<TcBuilding> tcBuildingsTemp = new ArrayList<>(100);
                    tcBuildingsTemp.add(tcBuilding);
                    resultMap.put(pid,tcBuildingsTemp);
                }
            }
        }
        return resultMap;
    }
}
