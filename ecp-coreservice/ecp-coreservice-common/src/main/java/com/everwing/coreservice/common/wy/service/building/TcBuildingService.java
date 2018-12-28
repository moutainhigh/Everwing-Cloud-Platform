package com.everwing.coreservice.common.wy.service.building;/**
 * Created by wust on 2017/4/18.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017-4-18 08:39:38
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public interface TcBuildingService {

    /**
     * 查询指定建筑类型下面的所有资产
     * @param ctx
     * @param condition
     * @return
     */
    BaseDto listPage(WyBusinessContext ctx, TcBuildingSearch condition);

    BaseDto listPageUnrelated(WyBusinessContext ctx, TcBuildingSearch condition);


    List<TcBuildingList> findAllBuildingNodeByCondition(WyBusinessContext ctx, TcBuildingSearch condition);



    /**
     * 查询指定条件的建筑信息
     * @param ctx
     * @param condition
     * @return
     */
    List<TcBuildingList> findByCondition(WyBusinessContext ctx, TcBuildingSearch condition);



    /**
     * 添加建筑
     * @param entity
     * @return
     */
    MessageMap add(WyBusinessContext ctx, TcBuilding entity);

    /**
     * 修改建筑
     * @param entity
     * @return
     */
    MessageMap modify(WyBusinessContext ctx, TcBuilding entity);

    /**
     * 删除建筑
     * @param compnayId
     * @param guids
     * @return
     */
    MessageMap delete(String compnayId,String guids);


    /**
     * 根据项目编码查询建筑信息
     * @param companyId
     * @param projectId
     * @return
     */
    List<TcBuilding> loadBuildingByProjectId(String companyId, String projectId);


    /**
     * 汇总资产信息
     * @param ctx
     * @param tcBuildingSearch
     * @return
     */
    List<Map<String,String>> collectAssetInfo(WyBusinessContext ctx, TcBuildingSearch tcBuildingSearch);


    /**
     * 同步最新树
     * @param ctx
     * @return
     */
    MessageMap syncBuildingTree(WyBusinessContext ctx);























    /**
     * 根据项目编号统计建筑的收费对象
     * @param ctx
     * @param projectId
     * @return
     */
    BaseDto countIsChargeObjByProject(WyBusinessContext ctx, String projectId, String isChargerObj);


    
    BaseDto initaccount(WyBusinessContext ctx, String projectId, String projectName);

    BaseDto loadBuildingByProjectIdWithoutTree(String companyId, String projectId);

	BaseDto loadBuildingByPickUpTree(String companyId, String projectId, String buildingCode,
			String custId);

	BaseDto listPageBuildingInEntery(String companyId, CustomerSearch customerSearch);
}
