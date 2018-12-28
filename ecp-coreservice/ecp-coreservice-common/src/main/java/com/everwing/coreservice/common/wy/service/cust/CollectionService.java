package com.everwing.coreservice.common.wy.service.cust;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.dto.TBcCollectionDto;
import com.everwing.coreservice.common.wy.entity.cust.TBcCollection;

/**
 * 银行托收service
 * @author DELL shiny
 * @create 2017/9/14
 */
public interface CollectionService {

    /**
     * 通过资产code查询托收列表
     * @param companyId 数据源切换公司Id
     * @param tBcCollectionDto 带客户id和分页的对象
     * @return 托收列表
     */
    BaseDto queryListByBuildingCode(String companyId,TBcCollectionDto tBcCollectionDto);

    /**
     * 通过项目id查询银行list
     * @param companyId 数据源切换公司id
     * @param projectId 项目id
     * @return 银行list
     */
    BaseDto queryBanksByProjectId(String companyId, String projectId);

    /**
     * 通过项目id加载收费项
     * @param companyId 数据源切换公司Id
     * @param projectId 项目id
     * @return  收费项list
     */
    BaseDto queryChargingItemsByProjectId(String companyId, String projectId);


    /**
     * 办理托收
     * @param companyId 数据源切换公司id
     * @param tBcCollection 托收实体
     * @return 办理托收结果
     */
    BaseDto insert(String companyId,TBcCollection tBcCollection);

    /**
     * 更新托收
     * @param companyId 数据源切换公司id
     * @param tBcCollection 托收实体
     * @return 更新托收结果
     */
    BaseDto update(String companyId,TBcCollection tBcCollection);

    /**
     * 批量删除托收
     * @param companyId 数据源切换公司id
     * @param ids 多个托收id,逗号分隔
     * @return 删除托收结果
     */
    BaseDto batchDelete(String companyId,String ids);

    /**
     * 批量生效托收
     * @param companyId 数据源切换公司id
     * @param ids 多个托收id,逗号分隔
     * @return 生效托收结果
     */
    BaseDto batchEffective(String companyId, String ids);

    /**
     * 批量失效托收
     * @param companyId 数据源切换公司id
     * @param ids 多个托收id,逗号分隔
     * @return 失效托收结果
     */
    BaseDto batchUnEffective(String companyId, String ids);

    /**
     * 通过客户id失效托收(供调用)
     * @param companyId 切换数据源用
     * @param custId 客户id
     * @param buildingCode 建筑编码
     * @return
     */
    BaseDto unEffectiveByCustIdAndBuildingCode(String companyId,String custId,String buildingCode);
}
