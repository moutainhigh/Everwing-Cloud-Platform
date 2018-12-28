package com.everwing.server.gating.controller.gating;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.AllowedNull;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.platform.api.LinPhoneApi;
import com.everwing.coreservice.platform.api.PlatformGatingApi;
import com.everwing.coreservice.wy.api.gating.GatingApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门控机接口
 */
@RestController
@RequestMapping("/gating/api/{version}")
public class GatingController {

	private static final Logger logger= LogManager.getLogger(GatingController.class);

	@Autowired
	private GatingApi gatingApi;

	@Autowired
	private PlatformGatingApi platformGatingApi;

	@Autowired
	private AccountApi accountApi;

	@Autowired
	private LinPhoneApi linPhoneApi;

	@PostMapping("login")
	@ApiVersion(1.0)
	public LinphoneResult login(String gatingAccount, String password){
		RemoteModelResult<Account> remoteModelResult=accountApi.queryByAccountNameAndPsw(gatingAccount, Dict.ACCOUNT_TYPE_ANDROID_MKJ.getIntValue(),password);
		if(remoteModelResult.isSuccess()){
			Account account=remoteModelResult.getModel();
			if(account==null){
				return new LinphoneResult(ResponseCode.LOGIN_FAILURE);
			}
			return gatingApi.login(account);
		}else {
			return new LinphoneResult(ResponseCode.LOGIN_FAILURE);
		}
	}

	@PostMapping("buildings")
	@ApiVersion(1.0)
	public LinphoneResult buildings(String gatingId, String companyId){
		return platformGatingApi.buildings(gatingId,companyId);
	}

	@PostMapping("checkUpdate")
	@ApiVersion(1.0)
	public LinphoneResult checkUpdate(String version, @AllowedNull String type){
		if(type==null){
			type="7";
		}
		return linPhoneApi.checkUpdate(version,type);
	}

	@PostMapping("addLog")
	@ApiVersion(1.0)
	public LinphoneResult addLog(String logId,String companyId,String projectId,String toBuildingCode,String fromBuildingCode,String gatingCode,
								 String gatingAccount,String type,String createTime){

		RemoteModelResult remoteModelResult=platformGatingApi.addLog(logId,companyId,projectId,toBuildingCode,fromBuildingCode,gatingCode,gatingAccount,type,createTime);
		if(remoteModelResult.isSuccess()){
			return new LinphoneResult(remoteModelResult.getModel());
		}
		return new LinphoneResult(remoteModelResult);
	}

	@PostMapping(value = "updateState")
	@ApiVersion(1.0)
	public LinphoneResult updateStatus(String companyId, String gatingCode, String version, String onlineState, String videosState){
		logger.info("同步门控机状态到物业开始!");
		RemoteModelResult<BaseDto> baseDtoRemoteModelResult=gatingApi.updateStatus(companyId,gatingCode,onlineState,videosState,version);
		if(baseDtoRemoteModelResult.isSuccess()){
			logger.info("同步门控机状态到物业成功!");
			logger.info("修改平台门控机状态开始!");
			RemoteModelResult remoteModelResult= platformGatingApi.updateStatus(gatingCode,version,onlineState,videosState);
			if(remoteModelResult.isSuccess()){
				logger.info("修改平台门控机状态成功!");
				return new LinphoneResult(ReturnCode.API_RESOLVE_SUCCESS);
			}
			logger.info("修改平台门控机状态失败!");
			return new LinphoneResult(remoteModelResult);
		}
		logger.info("同步物业门控机状态失败!");
		return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
	}
}
