package com.everwing.coreservice.wy.core.service.impl.gating;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.constant.MqConstants;
import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.extra.IdGen;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.RC4;
import com.everwing.coreservice.common.wy.dto.GatingDTO;
import com.everwing.coreservice.common.wy.dto.GatingLogStatisticsDTO;
import com.everwing.coreservice.common.wy.dto.GatingUserDto;
import com.everwing.coreservice.common.wy.dto.ProjectGatingDTO;
import com.everwing.coreservice.common.wy.entity.gating.BuildingGate;
import com.everwing.coreservice.common.wy.entity.gating.Gating;
import com.everwing.coreservice.common.wy.entity.gating.WhiteList;
import com.everwing.coreservice.common.wy.service.gating.GatingService;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.platform.api.IdGenApi;
import com.everwing.coreservice.wy.dao.mapper.gating.GatingMapper;
import com.everwing.coreservice.wy.dao.mapper.gating.WhiteListMapper;
import com.everwing.coreservice.wy.dao.mapper.gating.buildinggate.BuildingGateMapper;
import com.everwing.utils.TokenGenUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("gatingService")
public class GatingServiceImpl implements GatingService{
	
	private static final Logger LOGGER = Logger.getLogger(GatingServiceImpl.class);


	@Value("${queue.gating.key}")
	private String ROUTE_KEY;

	@Value("${queue.buildingGate.key}")
	private String BG_ROUTE_KEY;

	@Autowired
	private GatingMapper gatingMapper;
	@Autowired
	private AccountApi accountApi;

	@Autowired
	private IdGenApi idGenApi;
	
	@Autowired
	private BuildingGateMapper buildingGateMapper;

	@Autowired
	private SpringRedisTools springRedisTools;
	
	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	private WhiteListMapper whiteListMapper;

	@Value("${linphonePkgUrlPreffix}")
	private String linphonePkgUrlPreffix;

	@Value("${linphoneTokenKeyPreffix}")
	private String linphoneTokenKeyPreffix;

	@Value("${linphoneTokenExpireTime}")
	private String linphoneTokenExpireTime;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageGating(String companyId, Gating gating) {
		return new BaseDto(this.gatingMapper.listPageGating(gating), gating.getPage());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listAllGatingByKey(String companyId, Gating gating) {
		return new BaseDto(this.gatingMapper.listAllGatingByKey(gating),null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getGatingInfoById(String companyId, String id) {
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(this.gatingMapper.backgroundGetGatingByid(id));
		return returnDto;
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto addNewGating(String companyId, List<Gating> gatingList) {

		for (Gating gating : gatingList) {
			
			RemoteModelResult<IdGen> rst = this.idGenApi.queryMaxId(3);
			String accountName = "MK";
			if(rst.isSuccess() && CommonUtils.isNotEmpty(rst.getModel())){
				String num = String.valueOf(rst.getModel().getId());
				accountName = accountName.concat(num);
			}
			
			String gatingCode = createGatingAccount(accountName,companyId);
			gating.setVideosState(0);
			gating.setOnlineState(0);
			gating.setFacilityState("未使用");
			gating.setId(gatingCode);
			gating.setAccountName(accountName);
			gating.setGatingCode(gatingCode);
			gating.setCompanyId(companyId);
			gatingMapper.insertGating(gating);
		}
		
		
		//发送消息队列
		MqEntity entity = new MqEntity(MqConstants.OPR_BATCH_ADD, gatingList);
		this.amqpTemplate.convertAndSend(ROUTE_KEY, entity);
		LOGGER.info(CommonUtils.log("消息队列: 批量新增门控机, 发送消息到消息队列完成 . 数据: " + entity.toString()));

		
		BaseDto returnDto = new BaseDto();
		returnDto.setMessageMap(new MessageMap(null, "门控机新增成功!"));
		return returnDto;
	}
	
	private String createGatingAccount(String accountName,String companyId) {
		//组装成platform的Account对象,发出请求,注册门控机对象
		Account account = new Account();
		account.setAccountName(accountName);
		account.setAccountCode(CommonUtils.getUUID());
		account.setPassword(Base64.encodeBase64String(RC4.encry_RC4_string("123456", "jZ5$x!6yeAo1Qe^r").getBytes()));
		account.setType(3);
		account.setCompanyId(companyId);
		
		LOGGER.info(CommonUtils.log("传入参数: " + account.toString()));
		//注册门控机
		RemoteModelResult<Account> rslt = this.accountApi.register(account, null, null);
		if(rslt.isSuccess()){
			LOGGER.info(CommonUtils.log("门控机帐号向账户系统注册成功: 数据 -> " + account.toString()));
		}else{
			LOGGER.warn(CommonUtils.log("门控机帐号向账户系统注册失败: 数据  -> " + account.toString()));
		}
		return account.getAccountCode();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getGatingByKey(String companyId, String string,String companyIdStr) {

		JSONObject obj = new JSONObject();
		int ready = Integer.parseInt(countGatingState("使用中"));
		int using = Integer.parseInt(countGatingState("未使用"));
		DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
		String readyout = df.format((float) ready / (ready + using));
		String usingout = df.format((float) using / (ready + using));
		obj.put("ready", readyout);
		obj.put("using", usingout);
		BaseDto returnDto = new BaseDto(new MessageMap(null,MessageMap.INFOR_SUCCESS));
		returnDto.setObj(obj);
		return returnDto;
	}
	
	private String countGatingState(String key) {
		Gating gating = this.gatingMapper.getGatingByKey(key);
		return (null == gating) ? Constants.STR_ZERO:gating.getStatenum();
	}

	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto updateGateing(String companyId, Gating gating) {
		try {
			int row = gatingMapper.updateGating(gating);
			if(row > 0) {
				//发送到消息队列
				MqEntity entity = new MqEntity(MqConstants.OPR_UPDATE, gating);
				this.amqpTemplate.convertAndSend(ROUTE_KEY, entity);
				LOGGER.info(CommonUtils.log("消息队列: 修改门控机, 发送消息到消息队列完成 . 数据: " + entity.toString()));

				return new BaseDto(new MessageMap(null, "更新成功."));
			}else{
				return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"更新失败."));
			}
		} catch (Exception e) {
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "更新操作异常."));
		}
	}

	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto deleteGating(String companyId, String id) {
		try {
			Gating gating = this.gatingMapper.backgroundGetGatingByid(id);
			int row = gatingMapper.deleteGating(id);
			//删除门控机绑定的房屋数据 TODO 
			int gateCount = this.buildingGateMapper.deleteBuildingGateByGateCode(id);  //gate_id == gate_code
			if(row > 0) {
				//删除该门控机的内部账号信息 //TODO 等待接口
				RemoteModelResult<Account> rslt = this.accountApi.queryByAccountCode(gating.getGatingCode());
				LOGGER.info(CommonUtils.log("删除平台上门控机数据: code -> " + gating.getGatingCode() + ", 返回结果: " + rslt.toString()));

				if(rslt.isSuccess()){
					Account account = rslt.getModel();
					RemoteModelResult<Account> rslt1 = this.accountApi.cancelAccount(account.getAccountName(), 3, account.getPassword());
					if(rslt1.isSuccess()){
						LOGGER.info(CommonUtils.log("删除平台上门控机数据完成: code -> " + gating.getGatingCode() + ", 返回结果: " + rslt1.toString()));
						
						
						//发送到消息队列
						MqEntity entity = new MqEntity(MqConstants.OPR_DEL, gating);
						this.amqpTemplate.convertAndSend(ROUTE_KEY, entity);
						LOGGER.info(CommonUtils.log("消息队列: 删除门控机, 发送消息到消息队列完成 . 数据: " + entity.toString()));
						
						if(gateCount > 0){
							//推送到房屋门控机绑定
							MqEntity bgEntity = new MqEntity(MqConstants.OPR_DEL, gating);
							this.amqpTemplate.convertAndSend(BG_ROUTE_KEY,bgEntity);
							LOGGER.info(CommonUtils.log("消息队列: 绑定门控机与房屋关联关系, 发送消息到消息队列完成 . 数据: " + entity.toString()));
							
						}
					}else{
						LOGGER.warn(CommonUtils.log("删除平台上门控机数据出现异常: code -> " + gating.getGatingCode() + ", 返回结果: " + rslt1.toString()));
						throw new ECPBusinessException(ReturnCode.MK_GATING_DELETE_FAILED);
					}
					
					return new BaseDto(new MessageMap(null, "删除成功."));
				}else{
					LOGGER.warn(CommonUtils.log("获取平台上门控机数据出现异常: code -> " + gating.getGatingCode() + ", 返回结果: " + rslt.toString()));
					throw new ECPBusinessException(ReturnCode.MK_GATING_DELETE_FAILED);
				}
				
			}else{
				return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "删除失败."));
			}
		} catch (Exception e) {
			throw new ECPBusinessException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listBuildingGateByGateId(String companyId,String gateId) {
		return new BaseDto(this.buildingGateMapper.listBuildingGateByGateId(gateId), null);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto batchInsertBuildingGate(String companyId, List<BuildingGate> list) {
		MessageMap msgMap = new MessageMap();
		if(CommonUtils.isNotEmpty(list)){
			//1. 插入数据库
			this.buildingGateMapper.batchInsertBuildingGate(list);
			
			//2. 同步到platform数据库
			MqEntity entity = new MqEntity(MqConstants.OPR_BATCH_ADD, list);
			this.amqpTemplate.convertAndSend(BG_ROUTE_KEY, entity);
			LOGGER.info(CommonUtils.log("消息队列: 批量删除门控机, 发送消息到消息队列完成 . 数据: " + entity.toString()));

			msgMap.setMessage("插入成功");
		}else{
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("数据为空,插入失败");
		}
		return new BaseDto(msgMap);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getGatingState(String companyId, Gating gating) {
		gating.setFacilityState("未使用");
		int ready = gatingMapper.selectGatingState(gating);
		gating.setFacilityState("运行中");
		int using = gatingMapper.selectGatingState(gating);
		JSONObject jsonObj = new JSONObject();
		int total = ready+using;
		if(0 >= total){
			jsonObj.put("ready", "0.00");
			jsonObj.put("using", "0.00");
		}else{
			DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
			jsonObj.put("using", df.format((float) using / (ready + using)));
			jsonObj.put("ready", df.format((float) ready / (ready + using)));
		}
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(jsonObj);
		return returnDto;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getAccountDetail(String companyId, String gatingCode) {
		BaseDto returnDto = new BaseDto();
		RemoteModelResult<Account> rslt = this.accountApi.queryByAccountCodeWithoutException(gatingCode);
		if(rslt.isSuccess()){
			if(null != rslt.getModel()){
				Account account = rslt.getModel();
				account.setPassword(RC4.decry_RC4(new String(Base64.decodeBase64(account.getPassword())), Constants.ENCRY_KEY));
				returnDto.setObj(rslt.getModel());
			}else{
				returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_WARNING,"未找到相应的门控机账户, 无法获取密码. "));
			}
		}else{
			returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_ERROR, rslt.getMsg()));
		}
		return returnDto;
	}

	@Override
	public LinphoneResult queryLogin(String companyId, Account account) {
		if(account==null){
			return new LinphoneResult(ResponseCode.LOGIN_FAILURE);
		}
		Integer state=account.getState();
		if(state==0){
			LOGGER.debug("登录失败，账户已被注销!");
			return new LinphoneResult(ResponseCode.USER_LOG_OFF);
		}
		String accountId=account.getAccountId();
		String token= TokenGenUtils.generateToken(accountId);
		springRedisTools.addData(linphoneTokenKeyPreffix+token, JSON.toJSONString(account),60*24, TimeUnit.MINUTES);
		Map<String,String> map=gatingMapper.selectLoginGating(account.getAccountName());
		if(map!=null) {
			map.put("token", token);
		}
		return new LinphoneResult(map);
	}

	@Override
	public LinphoneResult queryIndoorLogin(String companyId, Account account) {
		if(account==null){
			return new LinphoneResult(ResponseCode.LOGIN_FAILURE);
		}
		Integer state=account.getState();
		if(state==0){
			return new LinphoneResult(ResponseCode.USER_LOG_OFF);
		}
		String accountId=account.getAccountId();
		String token=TokenGenUtils.generateToken(accountId);
		springRedisTools.addData(linphoneTokenKeyPreffix+token, JSON.toJSONString(account),Integer.valueOf(linphoneTokenExpireTime), TimeUnit.MINUTES);
		Map<String,String> map=gatingMapper.selectBuildingGating(account.getAccountCode());
		if(map!=null) {
			map.put("gatingCode","");
			map.put("gatingName","");
			map.put("token", token);
		}
		return new LinphoneResult(map);
	}

	@Override
	public BaseDto updateStatus(String companyId, String gatingCode, String onlineState,String videosState,String version) {
		Gating gating=new Gating();
		gating.setGatingCode(gatingCode);
		if(StringUtils.isNotEmpty(onlineState)) {
			gating.setOnlineState(Integer.parseInt(onlineState));
		}
		if(StringUtils.isNotEmpty(videosState)) {
			gating.setVideosState(Integer.parseInt(videosState));
		}
		gating.setVersion(version);
		BaseDto baseDto=new BaseDto();
		LOGGER.debug("开始更新物业门控机状态！");
		int count=gatingMapper.updateStatus(gating);
		if(count==0){
			LOGGER.debug("更新物业门控机状态失败!");
			baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_ERROR,"更新门控机信息失败!"));
			return baseDto;
		}
		LOGGER.debug("更新物业门控机状态成功!");
		baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"更新门控机信息成功"));
		return baseDto;
	}

    @Override
    public List<ProjectGatingDTO> queryProjectGating(String companyId) {
        return gatingMapper.queryProjectGating();
    }

    @Override
    public List<GatingDTO> queryByProjectId(String companyId ,String projectId) {
        return gatingMapper.queryByProjectId(projectId);
    }

    @Override
    public List<GatingLogStatisticsDTO> queryLogStatistics(String companyId, String mkAccountName) {
        return gatingMapper.queryLogStatistics(mkAccountName);
    }

	@Override
	public BaseDto listPageByCondition(String companyId, GatingUserDto gatingUserDto) {
		BaseDto baseDto=new BaseDto();
		Integer totalResult=gatingMapper.countSelectByCondition(gatingUserDto);
		if(totalResult!=0){
			gatingUserDto.getPage().setTotalResult(totalResult);
			gatingUserDto.setStart((gatingUserDto.getPage().getCurrentPage()-1)*gatingUserDto.getPage().getShowCount());
			List<Gating> gatings=gatingMapper.selectByCondition(gatingUserDto);
			baseDto.setLstDto(gatings);
		}
		Page page=gatingUserDto.getPage();
		page.setTotalPage(totalResult/page.getShowCount());
		page.setTotalResult(totalResult);
		baseDto.setPage(page);
		return baseDto;
	}

	@Override
	public BaseDto batchInsertWhiteList(String dataSourceCompanyId, List<WhiteList> whiteLists) {
		whiteListMapper.batchInsert(whiteLists);
		BaseDto returnDto = new BaseDto();
		returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS, "门控机新增成功!"));
		return returnDto;
	}

	@Override
	public List<Map<String, String>> getWhiteListByCPG(String companyId, String projectId, String gatingId) {
		return whiteListMapper.selectByCPG(companyId,projectId,gatingId);
	}

	@Override
	public RemoteModelResult getGatingStruct(String companyId, String projectId) {
		List<Map<String,String>> buildings=gatingMapper.getGatingStruct(companyId,projectId);
		LOGGER.debug("查询成功！");
		RemoteModelResult RemoteModelResult = new RemoteModelResult();
		RemoteModelResult.setCode(ReturnCode.API_RESOLVE_SUCCESS.getCode());
		RemoteModelResult.setModel(buildings);
		return  RemoteModelResult;
	}

	@Override
	public RemoteModelResult getBuildingsByApartmentId(String companyId, String projectId, String apartmentId) {
		List<Map<String,String>> buildings = gatingMapper.getBuildingsByApartmentId(companyId,projectId,apartmentId);
		RemoteModelResult RemoteModelResult = new RemoteModelResult();
		RemoteModelResult.setCode(ReturnCode.API_RESOLVE_SUCCESS.getCode());
		RemoteModelResult.setModel(buildings);
		return  RemoteModelResult;
	}

	@Override
	public RemoteModelResult getGatingDataByBuildingId(String companyid, String buildingId) {
		List<Map<String,String>> gatings = gatingMapper.getGatingDataByBuildingId(buildingId);
		RemoteModelResult RemoteModelResult = new RemoteModelResult();
		RemoteModelResult.setCode(ReturnCode.API_RESOLVE_SUCCESS.getCode());
		RemoteModelResult.setModel(gatings);
		return  RemoteModelResult;
	}

	@Override
	public RemoteModelResult<BaseDto> deleteWhiteList(String companyId,String userId) {
		gatingMapper.deleteWhiteList(userId);
		BaseDto returnDto = new BaseDto();
		returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS, "门控机新增成功!"));
		return new RemoteModelResult<>(returnDto);
	}

}
