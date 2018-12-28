package com.everwing.coreservice.wy.api.building;/**
 * Created by wust on 2017/4/18.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.house.TcHouseList;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.building.TcBuildingImportService;
import com.everwing.coreservice.common.wy.service.building.TcBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017-4-18 08:50:45
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
@Component
public class TcBuildingApi {
    @Autowired
    private TcBuildingService tcBuildingService;

    @Autowired
    private TcBuildingImportService tcBuildingImportService;

    public RemoteModelResult<BaseDto> listPage(WyBusinessContext ctx, TcBuildingSearch condition){
        BaseDto pageResultDto = tcBuildingService.listPage(ctx,condition);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }

    public RemoteModelResult<BaseDto> listPageUnrelated(WyBusinessContext ctx, TcBuildingSearch condition){
        BaseDto pageResultDto = tcBuildingService.listPageUnrelated(ctx,condition);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }

    public RemoteModelResult<List<TcBuildingList>> findAllBuildingNodeByCondition(WyBusinessContext ctx, TcBuildingSearch condition){
        List<TcBuildingList> list = tcBuildingService.findAllBuildingNodeByCondition(ctx,condition);
        RemoteModelResult<List<TcBuildingList>> result = new RemoteModelResult<>();
        result.setModel(list);
        return result;
    }


    public RemoteModelResult<List<TcBuildingList>> findByCondition(WyBusinessContext ctx, TcBuildingSearch condition){
        List<TcBuildingList> list = tcBuildingService.findByCondition(ctx,condition);
        RemoteModelResult<List<TcBuildingList>> result = new RemoteModelResult<>();
        result.setModel(list);
        return result;
    }


    /**
     * 根据项目编号来统计计费建筑
     * @param ctx
     * @param projectId
     * @return
     */
    public RemoteModelResult<BaseDto> countIsChargeObjByProject(WyBusinessContext ctx, String projectId, String isChargeObj){
    	
    	return new RemoteModelResult<BaseDto>(this.tcBuildingService.countIsChargeObjByProject(ctx, projectId,isChargeObj));
    }


    /**
     * 添加
     * @param entity
     * @return
     */
    public RemoteModelResult<MessageMap> add(WyBusinessContext ctx, TcBuilding entity){
        MessageMap messageMap =tcBuildingService.add(ctx,entity);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;
    }

    /**
     * 修改
     * @param entity
     * @return
     */
    public RemoteModelResult<MessageMap> modify(WyBusinessContext ctx, TcBuilding entity){
        MessageMap messageMap = tcBuildingService.modify(ctx,entity);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;
    }

    /**
     * 删除
     * @param compnayId
     * @param guids
     * @return
     */
    public RemoteModelResult<MessageMap> delete(String compnayId,String guids){
        MessageMap messageMap =tcBuildingService.delete(compnayId,guids);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;
    }

    /**
     * 统计资产信息
     * @param ctx
     * @param tcBuildingSearch
     * @return
     */
    public RemoteModelResult<List<Map<String,String>>> collectAssetInfo(WyBusinessContext ctx, TcBuildingSearch tcBuildingSearch){
        List<Map<String,String>> map = tcBuildingService.collectAssetInfo(ctx,tcBuildingSearch);
        RemoteModelResult<List<Map<String,String>>> result = new RemoteModelResult<>();
        result.setModel(map);
        return result;
    }


    /**
     * 导入
     * @param ctx
     * @param tSysImportExportRequest
     * @return
     */
    public RemoteModelResult importTcbuilding(WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest){
        tcBuildingImportService.doImport(ctx,tSysImportExportRequest);
        RemoteModelResult result = new RemoteModelResult<>();
        return result;
    }


	public RemoteModelResult<List<TcBuilding>> loadBuildingByProjectId(String companyId,String projectId) {
		RemoteModelResult<List<TcBuilding>> result = new RemoteModelResult<>();
		result.setModel(this.tcBuildingService.loadBuildingByProjectId(companyId,projectId));
		return result;
	}


    /**
     * 同步最新树
     * @param ctx
     * @return
     */
    public RemoteModelResult<MessageMap> syncBuildingTree(WyBusinessContext ctx) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(this.tcBuildingService.syncBuildingTree(ctx));
        return result;
    }







	/**
	 * 初始化账户
	 */
	public RemoteModelResult<BaseDto> initaccount(WyBusinessContext ctx, String projectId, String projectName){
		return new RemoteModelResult<BaseDto>(this.tcBuildingService.initaccount(ctx, projectId, projectName));
	}


    public RemoteModelResult<BaseDto> loadBuildingByPickUpTree(WyBusinessContext ctx, String projectId, String buildingCode, String custId){
        return new RemoteModelResult<BaseDto>(this.tcBuildingService.loadBuildingByPickUpTree(ctx.getCompanyId(), projectId, buildingCode,  custId));
    }

    public RemoteModelResult<BaseDto> loadBuildingByProjectIdWithoutTree(WyBusinessContext ctx, String projectId){
        return new RemoteModelResult<BaseDto>(this.tcBuildingService.loadBuildingByProjectIdWithoutTree(ctx.getCompanyId(),projectId));
    }

    public RemoteModelResult<BaseDto> listPageBuildingInEntery(String companyId, CustomerSearch customerSearch){
        BaseDto pageResultDto = tcBuildingService.listPageBuildingInEntery(companyId,customerSearch);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }
}
