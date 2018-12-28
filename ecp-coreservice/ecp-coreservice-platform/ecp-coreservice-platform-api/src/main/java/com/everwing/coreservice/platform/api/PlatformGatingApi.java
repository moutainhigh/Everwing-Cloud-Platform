package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.entity.extra.AppPkgDto;
import com.everwing.coreservice.common.platform.entity.generated.MkjLog;
import com.everwing.coreservice.common.platform.service.PlatformGatingService;
import com.everwing.coreservice.common.wy.dto.GatingLogStatisticsDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PlatformGatingApi {

    @Resource(name = "platformGatingService")
    private PlatformGatingService platformGatingService;

    @NoExceptionProxy
    public LinphoneResult getByMobile(String accountId, String mobile) {
        return platformGatingService.queryGatingListByMobile(accountId,mobile);
    }

    @NoExceptionProxy
    public LinphoneResult gatingList(String buildingId) {
        return platformGatingService.queryGatingListByBId(buildingId);
    }

    @NoExceptionProxy
    public LinphoneResult buildings(String gatingId, String companyId) {
        return platformGatingService.queryBuildings(companyId,gatingId);
    }

    public RemoteModelResult addLog(String logId,String companyId, String projectId, String toBuildingCode, String fromBuildingCode, String gatingCode, String gatingAccount, String type, String createTime) {
        return platformGatingService.insertLog(logId,companyId,projectId,toBuildingCode,fromBuildingCode,gatingCode,gatingAccount,type,createTime);
    }

    /**
     * 更新门控机状态
     * @param gatingCode 门控机编码
     * @param version    门控机当前版本
     * @param onlineState     在线状态 0 在线1 离线
     * @param videosState 视频开启状态 0 开启 1 关闭
     * @return 通用返回结果
     */
    public RemoteModelResult updateStatus(String gatingCode,String version, String onlineState, String videosState) {
        return platformGatingService.updateStatus(gatingCode,version,onlineState,videosState);
    }

    public RemoteModelResult<List<GatingLogStatisticsDTO>> queryLogStatistics(String companyId, String mkAccountName,int pageNo,int pageSize) {
        return platformGatingService.queryLogStatistics(companyId,mkAccountName,pageNo,pageSize);
    }

    public RemoteModelResult<AppPkgDto> checkUpdate(String version, String type) {
        return platformGatingService.checkUpdate(version,type);
    }

    public RemoteModelResult queryMkLogData(String companyId,String gatingAccountName, String logDate ) {
        return platformGatingService.queryMkLogData(companyId,gatingAccountName,logDate);
    }


    public RemoteModelResult<BaseDto> queryLogsByObj(MkjLog log) {
        return new RemoteModelResult<BaseDto>(this.platformGatingService.queryLogsByObj(log));
    }

    public RemoteModelResult<String> getCompanyByBuildingId(String buildingId) {
        return  new RemoteModelResult<String>(platformGatingService.getCompanyByBuildingId(buildingId));

    }


}
