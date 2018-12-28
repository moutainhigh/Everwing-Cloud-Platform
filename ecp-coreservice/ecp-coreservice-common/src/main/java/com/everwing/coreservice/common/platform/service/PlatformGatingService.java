package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.MkjLog;

public interface PlatformGatingService {
    /**
     * 通过手机号获取资产对应的门口机列表
     * @param accountId 登录用户唯一标识
     * @param mobile 用户手机号码 
     * @return 统一返回结果(门口机列表)
     */
    LinphoneResult queryGatingListByMobile(String accountId, String mobile);

    /**
     * 根据资产信息获取对应的门口机列表
     * @param buildingId 资产唯一id
     * @return 邻音统一返回结果
     */
    LinphoneResult queryGatingListByBId(String buildingId);

    /**
     * 获取建筑结构
     * @param gatingId 门控机主键id
     * @param companyId  公司id
     * @return 通用返回结果
     */
    LinphoneResult queryBuildings(String companyId, String gatingId);

    /**
     * 上传门控机日志
     * @param logId 标识log
     * @param companyId 公司id
     * @param projectId 项目id
     * @param toBuildingCode 房间code
     * @param fromBuildingCode 呼叫房间sip账号
     * @param gatingCode 门控机编码
     * @param gatingAccount 门控机账号
     * @param type  开门类型
     * @param createTime 开门时间
     * @return 通用返回结果
     */
    RemoteModelResult insertLog(String logId,String companyId, String projectId, String toBuildingCode, String fromBuildingCode, String gatingCode, String gatingAccount, String type, String createTime);

    /**
     * 更新门控机状态
     * @param gatingCode 门控机编码
     * @param onlineState  门控机在线状态
     * @param videosState 视频开启状态
     * @return 通用返回结果
     */
    RemoteModelResult updateStatus(String gatingCode,String version, String onlineState, String videosState);

    RemoteModelResult queryLogStatistics(String companyId, String mkAccountName,int pageNo,int pageSize);

	BaseDto queryLogsByObj(MkjLog log);

    RemoteModelResult queryMkLogData(String companyId,String gatingName,String logDate);

    RemoteModelResult checkUpdate(String version, String type);

    String getCompanyByBuildingId(String buildingId);


}
