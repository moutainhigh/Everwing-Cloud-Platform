package com.everwing.coreservice.common.wy.service.gating;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.wy.dto.GatingDTO;
import com.everwing.coreservice.common.wy.dto.GatingLogStatisticsDTO;
import com.everwing.coreservice.common.wy.dto.GatingUserDto;
import com.everwing.coreservice.common.wy.dto.ProjectGatingDTO;
import com.everwing.coreservice.common.wy.entity.gating.BuildingGate;
import com.everwing.coreservice.common.wy.entity.gating.Gating;
import com.everwing.coreservice.common.wy.entity.gating.WhiteList;

import java.util.List;
import java.util.Map;

public interface GatingService {

	BaseDto listPageGating(String companyId, Gating gating);

	BaseDto listAllGatingByKey(String companyId, Gating gating);

	BaseDto getGatingInfoById(String companyId, String id);

	BaseDto addNewGating(String companyId, List<Gating> gatingList);

	BaseDto getGatingByKey(String companyId, String string, String companyIdStr);

	BaseDto updateGateing(String companyId, Gating gating);

	BaseDto deleteGating(String companyId, String id);

	BaseDto listBuildingGateByGateId(String companyId, String gatingId);

	BaseDto batchInsertBuildingGate(String companyId, List<BuildingGate> list);

	BaseDto getGatingState(String companyId, Gating gating);

	BaseDto getAccountDetail(String companyId, String gatingCode);

	/**
	 * 门控机登录
	 * @param companyId 公司id
	 * @param account 账号
	 * @return app通用返回结果
	 */
	LinphoneResult queryLogin(String companyId, Account account);

	LinphoneResult queryIndoorLogin(String companyId, Account account);

	/**
	 * 更新门控机在线状态
	 * @param companyId 公司id
	 * @param gatingCode 门控机编码
	 * @param onlineState 门控机在线状态
	 * @param videosState 视频开启状态
	 * @return 封装返回结果
	 */
	BaseDto updateStatus(String companyId, String gatingCode, String onlineState,String videosState,String version);

    List<ProjectGatingDTO> queryProjectGating(String companyId);

    List<GatingDTO> queryByProjectId(String companyId,String projectId);

    List<GatingLogStatisticsDTO> queryLogStatistics(String companyId, String mkAccountName);

    BaseDto listPageByCondition(String companyId,GatingUserDto gatingUserDto);

	BaseDto batchInsertWhiteList(String dataSourceCompanyId, List<WhiteList> whiteLists);

    List<Map<String,String>> getWhiteListByCPG(String companyId, String projectId, String gatingId);

	RemoteModelResult getGatingStruct(String companyId, String projectId);

	RemoteModelResult getBuildingsByApartmentId(String companyId, String projectId, String apartmentId);

	RemoteModelResult getGatingDataByBuildingId(String companyid, String buildingId);

	RemoteModelResult<BaseDto> deleteWhiteList(String companyId,String userId);
}
