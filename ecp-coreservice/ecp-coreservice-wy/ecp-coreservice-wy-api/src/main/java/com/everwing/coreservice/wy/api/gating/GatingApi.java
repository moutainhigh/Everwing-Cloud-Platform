package com.everwing.coreservice.wy.api.gating;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.dto.*;
import com.everwing.coreservice.common.wy.entity.gating.BuildingGate;
import com.everwing.coreservice.common.wy.entity.gating.Gating;
import com.everwing.coreservice.common.wy.entity.gating.WhiteList;
import com.everwing.coreservice.common.wy.service.gating.GatingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("gatingApi")
public class GatingApi {

	@Autowired
	private GatingService gatingService;

	public RemoteModelResult<BaseDto> listPageGating(String companyId, Gating gating) {
		return new RemoteModelResult<BaseDto>(this.gatingService.listPageGating(companyId,gating));
	}

	public RemoteModelResult<BaseDto> listAllGatingByKey(String companyId,Gating gating) {
		return new RemoteModelResult<BaseDto>(this.gatingService.listAllGatingByKey(companyId,gating));
	}

	public RemoteModelResult<BaseDto> getGatingInfoById(String companyId,String id) {
		return new RemoteModelResult<BaseDto>(this.gatingService.getGatingInfoById(companyId,id));
	}

	public RemoteModelResult<BaseDto> addNewGating(String companyId,List<Gating> gatingList) {
		return  new RemoteModelResult<BaseDto>(this.gatingService.addNewGating(companyId,gatingList));
	}

	public RemoteModelResult<BaseDto> getGatingByKey(String companyId,String string, String companyIdStr) {
		return  new RemoteModelResult<BaseDto>(this.gatingService.getGatingByKey(companyId,string,companyIdStr));
	}

	public RemoteModelResult<BaseDto> updateGateing(String companyId,Gating gating) {
		return  new RemoteModelResult<BaseDto>(this.gatingService.updateGateing(companyId,gating));
	}

	public RemoteModelResult<BaseDto> deleteGating(String companyId, String id) {
		return  new RemoteModelResult<BaseDto>(this.gatingService.deleteGating(companyId,id));
	}

	public RemoteModelResult<BaseDto> listBuildingGateByGateId(String companyId, String gateId) {
		return new RemoteModelResult<BaseDto>(this.gatingService.listBuildingGateByGateId(companyId,gateId));
	}

	public RemoteModelResult<BaseDto> batchInsertBuildingGate(String companyId,List<BuildingGate> list) {
		return new RemoteModelResult<BaseDto>(this.gatingService.batchInsertBuildingGate(companyId,list));
	}

	public RemoteModelResult<BaseDto> getGatingState(String companyId,Gating gating) {
		return new RemoteModelResult<BaseDto>(this.gatingService.getGatingState(companyId,gating));
	}

	public RemoteModelResult<BaseDto> getAccountDetail(String companyId,String gatingCode) {
		return new RemoteModelResult<BaseDto>(this.gatingService.getAccountDetail(companyId,gatingCode));

	}

	public RemoteModelResult<BaseDto> listPageByGating(String companyId, GatingUserDto gatingUserDto) {
		return new RemoteModelResult<BaseDto>(gatingService.listPageByCondition(companyId,gatingUserDto));
	}

	@NoExceptionProxy
    public LinphoneResult login(Account account) {
		return gatingService.queryLogin(account.getCompanyId(),account);
    }

	@NoExceptionProxy
	public LinphoneResult interiorLogin(Account account) {
		return gatingService.queryIndoorLogin(account.getCompanyId(),account);
	}

	public RemoteModelResult<BaseDto> updateStatus(String companyId, String gatingCode, String onlineState,String videosState,String version) {
		return new RemoteModelResult<>(gatingService.updateStatus(companyId,gatingCode,onlineState,videosState,version));
	}


	/****------wyapp-----****/
	public RemoteModelResult<List<ProjectGatingDTO>> queryProjectGating(String companyId) {
        return new RemoteModelResult<>(gatingService.queryProjectGating(companyId));
    }

    public RemoteModelResult<List<GatingDTO>> queryByProjectId(String companyId, String projectId) {
        return new RemoteModelResult<>(gatingService.queryByProjectId(companyId,projectId));

    }

    public RemoteModelResult<List<GatingLogStatisticsDTO>> queryLogStatistics(String companyId, String mkAccountName) {
        return new RemoteModelResult<>(gatingService.queryLogStatistics(companyId,mkAccountName));
    }

    public RemoteModelResult<BaseDto> insertWhiteList(WhiteListDto whiteListDto) {
		Boolean all=whiteListDto.getAll();
		String dataSourceCompanyId=WyBusinessContext.getContext().getCompanyId();
		String createBy= WyBusinessContext.getContext().getUserId();
		List<WhiteList> whiteLists=new ArrayList<>();
		if(all!=null&&all){
			if(StringUtils.isEmpty(whiteListDto.getProjectId())){
				MessageMap messageMap=new MessageMap(MessageMap.INFOR_ERROR,"请先选择项目!");
				BaseDto baseDto=new BaseDto(messageMap);
				return new RemoteModelResult<>(baseDto);
			}else {
				RemoteModelResult result=gatingService.deleteWhiteList(dataSourceCompanyId,whiteListDto.getUserId());
				if(result.isSuccess()){
					RemoteModelResult<List<GatingDTO>> remoteModelResult=queryByProjectId(whiteListDto.getCompanyId(),whiteListDto.getProjectId());
					if(remoteModelResult.isSuccess()){
						List<GatingDTO> gatingDTOS=remoteModelResult.getModel();
						for(int i=0;i<gatingDTOS.size();i++){
							WhiteList whiteList=new WhiteList();
							whiteList.setId(UUID.randomUUID().toString());
							whiteList.setUserId(whiteListDto.getUserId());
							whiteList.setGatingCode(gatingDTOS.get(i).getGatingCode());
							whiteList.setCompanyId(whiteListDto.getCompanyId());
							whiteList.setProjectId(whiteListDto.getProjectId());
							whiteList.setCreateBy(createBy);
							whiteLists.add(whiteList);
						}
					}
				}else {
					MessageMap messageMap=new MessageMap(MessageMap.INFOR_ERROR,"清空白名单关系失败!");
					BaseDto baseDto=new BaseDto(messageMap);
					return new RemoteModelResult<>(baseDto);
				}
			}

		}else {
			String[] gatingCodes=whiteListDto.getGatingCodes().split(",");
			String[] projectIds=whiteListDto.getProjectIds().split(",");
			for(int i=0;i<gatingCodes.length;i++){
				WhiteList whiteList=new WhiteList();
				whiteList.setId(UUID.randomUUID().toString());
				whiteList.setUserId(whiteListDto.getUserId());
				whiteList.setGatingCode(gatingCodes[i]);
				whiteList.setCompanyId(whiteListDto.getCompanyId());
				whiteList.setProjectId(projectIds[i]);
				whiteList.setCreateBy(createBy);
				whiteLists.add(whiteList);
			}
		}
		return new RemoteModelResult<>(gatingService.batchInsertWhiteList(dataSourceCompanyId,whiteLists));
    }

    public RemoteModelResult<List<Map<String,String>>> queryByCPG(String companyId, String projectId, String gatingId) {
		List<Map<String,String>> whiteLists=gatingService.getWhiteListByCPG(companyId,projectId,gatingId);
		return new RemoteModelResult<>(whiteLists);
    }


	public RemoteModelResult getGatingStruct(String companyId, String projectId) {
		return gatingService.getGatingStruct(companyId,projectId);
	}

	public RemoteModelResult getBuildingsByApartmentId(String companyId, String projectId, String apartmentId) {
		return  gatingService.getBuildingsByApartmentId(companyId,projectId,apartmentId);
	}

	public RemoteModelResult getGatingDataByBuildingId(String companyid, String buildingId) {
		return  gatingService.getGatingDataByBuildingId(companyid,buildingId);
	}

	public RemoteModelResult<BaseDto> deleteWhiteList(String userId) {
		return gatingService.deleteWhiteList(CommonUtils.getCompanyIdByCurrRequest(),userId);
	}
}
