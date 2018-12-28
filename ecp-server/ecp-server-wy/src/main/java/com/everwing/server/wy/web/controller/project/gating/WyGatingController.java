package com.everwing.server.wy.web.controller.project.gating;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SipUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.dto.GatingUserDto;
import com.everwing.coreservice.common.wy.dto.WhiteListDto;
import com.everwing.coreservice.common.wy.entity.gating.BuildingGate;
import com.everwing.coreservice.common.wy.entity.gating.Gating;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.platform.api.PlatformGatingApi;
import com.everwing.coreservice.wy.api.gating.GatingApi;
import com.everwing.coreservice.wy.api.sys.TSysProjectApi;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/wyGating")
public class WyGatingController {

	private static final Logger logger= LogManager.getLogger(WyGatingController.class);

	@Autowired
	private SipUtils sipUtils;

	@Autowired
	private PlatformGatingApi platformGatingApi;

	@Autowired
	private GatingApi gatingApi;

	@Autowired
	private TSysProjectApi tSysProjectApi;

	@RequestMapping(value = "loadByCondition",method = RequestMethod.POST)
	public @ResponseBody BaseDto loadByUserId(@RequestBody GatingUserDto gatingUserDto){
		String companyId= WyBusinessContext.getContext().getCompanyId();
		TSysProjectSearch tSysProjectSearch=new TSysProjectSearch();
		tSysProjectSearch.setLoginName(gatingUserDto.getLoginName());
		tSysProjectSearch.setCompanyId(gatingUserDto.getCompanyId());
		RemoteModelResult<BaseDto> remoteModelResult=tSysProjectApi.findByCondition(WyBusinessContext.getContext(),tSysProjectSearch);
		if(remoteModelResult.isSuccess()){
			BaseDto baseDto=remoteModelResult.getModel();
			if(baseDto!=null){
				List<TSysProjectList> tSysProjectLists=baseDto.getLstDto();
				Collection<String> pids=Collections2.transform(tSysProjectLists, new Function<TSysProjectList, String>() {
					@Override
					public String apply(TSysProjectList tSysProjectList) {
						return tSysProjectList.getCode();
					}
				});
				List<String> projectIds= new ArrayList<>(pids);
				gatingUserDto.setProjectIds(projectIds);
				return BaseDtoUtils.getDto(gatingApi.listPageByGating(companyId,gatingUserDto));
			}
		}
		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"参数错误!"));
	}

	@RequestMapping(value="/insertWhiteList",method = RequestMethod.POST)
	public @ResponseBody BaseDto insertWhiteList(@RequestBody WhiteListDto whiteListDto){
		if(whiteListDto.isAnyNull()){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"参数错误!"));
		}else {
			return BaseDtoUtils.getDto(gatingApi.insertWhiteList(whiteListDto));
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/listPageGating",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageGating(HttpServletRequest req , @RequestBody Gating gating){
		return BaseDtoUtils.getDto(this.gatingApi.listPageGating(CommonUtils.getCompanyId(req), gating));
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getGatingState",method=RequestMethod.POST)
	public @ResponseBody BaseDto getGatingState(HttpServletRequest req , @RequestBody Gating gating){
		return BaseDtoUtils.getDto(this.gatingApi.getGatingState(CommonUtils.getCompanyId(req), gating));
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getAccountDetail/{gatingCode}",method=RequestMethod.GET)
	public @ResponseBody BaseDto getAccountDetail(HttpServletRequest req , @PathVariable("gatingCode") String gatingCode){
		return BaseDtoUtils.getDto(this.gatingApi.getAccountDetail(CommonUtils.getCompanyId(req), gatingCode));
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/updateGating",method=RequestMethod.POST)
	public @ResponseBody BaseDto updateGating(HttpServletRequest req , @RequestBody Gating gating){
		return BaseDtoUtils.getDto(this.gatingApi.updateGateing(CommonUtils.getCompanyId(req), gating));
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/deleteGating/{id}",method=RequestMethod.POST)
	public @ResponseBody BaseDto deleteGating(HttpServletRequest req ,@PathVariable String id){
		return BaseDtoUtils.getDto(this.gatingApi.deleteGating(CommonUtils.getCompanyId(req), id));
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/BuildingGateList/{gateId}",method=RequestMethod.GET)
	public @ResponseBody BaseDto BuildingGateList(HttpServletRequest req ,@PathVariable String gateId){
		return BaseDtoUtils.getDto(this.gatingApi.listBuildingGateByGateId(CommonUtils.getCompanyId(req), gateId));
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/batchInsertBuildingGate",method=RequestMethod.POST)
	public @ResponseBody BaseDto batchInsertBuildingGate(HttpServletRequest req ,@RequestBody List<BuildingGate> bgs){
		return BaseDtoUtils.getDto(this.gatingApi.batchInsertBuildingGate(CommonUtils.getCompanyId(req), bgs));
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/addNewGating",method=RequestMethod.POST)
	public @ResponseBody BaseDto addNewGating(HttpServletRequest req ,@RequestBody List<Gating> gatings){
		return BaseDtoUtils.getDto(this.gatingApi.addNewGating(CommonUtils.getCompanyId(req), gatings));
	}

	@RequestMapping(value = "restart",method = RequestMethod.POST)
	public @ResponseBody BaseDto restart(HttpServletRequest request,@RequestBody Gating gating){
		String gatingCode=gating.getGatingCode();
		logger.info("重启门控机开始，门控机状态同步到平台gatingCode:{}",gatingCode);
		RemoteModelResult remoteModelResult=platformGatingApi.updateStatus(gatingCode,null,"0",null);
		if(remoteModelResult.isSuccess()){
			logger.info("重启门控机同步到平台成功!");
			logger.info("修改门控机物业数据开始");
			RemoteModelResult<BaseDto> baseDtoRemoteModelResult=this.gatingApi.updateStatus(CommonUtils.getCompanyId(request),gatingCode,"0",null,null);
			if(baseDtoRemoteModelResult.isSuccess()){
				logger.info("修改门控机物业平台数据成功!");
				sipUtils.sipGating(gatingCode,"restart",new HashMap<String, String>());
				return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"重启成功！"));
			}else {
				logger.info("修改门控机物业平台数据失败!");
			}
			return BaseDtoUtils.getDto(baseDtoRemoteModelResult);
		}
		logger.info("重启门控机开始，门控机状态同步到平台失败！");
		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"重启失败!"));
	}

	@RequestMapping(value = "enableVideo",method = RequestMethod.POST)
	public @ResponseBody BaseDto enableVideo(HttpServletRequest request,@RequestBody Gating gating){
		String gatingCode=gating.getGatingCode();
		String videosState=String.valueOf(gating.getVideosState());
		logger.info("打开或者关闭门控机视频，同步到平台,参数gatingCode:{}videoState{}",gatingCode,videosState);
		RemoteModelResult remoteModelResult=platformGatingApi.updateStatus(gatingCode,null,null,videosState);
		if(remoteModelResult.isSuccess()){
			logger.info("打开或者关闭门控机视频同步到平台成功!");
			logger.info("修改门控机视频状态写入物业");
			RemoteModelResult<BaseDto> baseDtoRemoteModelResult=gatingApi.updateStatus(CommonUtils.getCompanyId(request),gatingCode,null,videosState,null);
			if(baseDtoRemoteModelResult.isSuccess()){
				logger.info("修改门控机视频状态写入物业成功!");
				Map<String,String> parameters=new HashMap<>(1);
				if("0".equals(videosState)){
					parameters.put("value","on");
				}else if("1".equals(videosState)){
					parameters.put("value","off");
				}
				sipUtils.sipGating(gatingCode,"enabledvideo",parameters);
			}else {
				logger.info("修改门控机视频状态写入物业失败!");
			}
			return BaseDtoUtils.getDto(baseDtoRemoteModelResult);

		}
		logger.info("打开或者关闭门控机视频，同步到平台失败！");
		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"修改门控机视频状态失败"));
	}

	@RequestMapping(value = "forceUpdate",method = RequestMethod.POST)
	public @ResponseBody BaseDto forceUpdate(@RequestBody Gating gating){
		String gatingCode=gating.getGatingCode();
		logger.info("强制更新同步到sip服务器gatingCode:{}",gatingCode);
		boolean flag=sipUtils.sipGating(gatingCode,"swUpdate",new HashMap<String, String>());
		if(flag) {
			return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS, "升级成功!"));
		}
		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"升级失败!"));
	}

	@RequestMapping(value = "updateHouseData",method = RequestMethod.POST)
	public @ResponseBody BaseDto updateHouseData(@RequestBody Gating gating){
		String gatingCode=gating.getGatingCode();
		boolean flag=sipUtils.sipGating(gatingCode,"housUpdate",new HashMap<String, String>());
		if(flag){
			return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS, "更新房屋数据成功!"));
		}
		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"更新房屋数据失败!"));
	}

	@RequestMapping(value = "checkTime",method = RequestMethod.POST)
	public @ResponseBody BaseDto checkTime(@RequestBody Gating gating){
		String gatingCode=gating.getGatingCode();
		boolean flag=sipUtils.sipGating(gatingCode,"checkTime",new HashMap<String, String>());
		if(flag){
			return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS, "校时成功!"));
		}
		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"校时失败!"));
	}

}
