package com.everwing.coreservice.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.AccountDto;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.AccountExample;
import com.everwing.coreservice.common.platform.entity.generated.CompanyApproval;
import com.everwing.coreservice.common.platform.entity.generated.CompanyApprovalExample;
import com.everwing.coreservice.common.platform.service.AccountService;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.platform.core.util.Resources;
import com.everwing.utils.TokenGenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.everwing.coreservice.common.utils.CommonUtils.*;

@Service
public class AccountServiceImpl extends Resources implements AccountService {

	private static final Logger logger= LoggerFactory.getLogger(AccountServiceImpl.class);

	private ConcurrentHashMap<String,String> mobileCache=new ConcurrentHashMap<>();

	@Value("${linphoneVerifySMSKeyPrefix}")
	private String linphoneSMSCodePrefix;

	@Value("${linphoneSMSKeyCountPrefix}")
	private String linphoneSMSCountPrefix;

	@Value("${wyVerifySMSKeyPrefix}")
	private String wySMSCodePrefix;

	@Value("${wySMSKeyCountPrefix}")
	private String wySMSKeyCountPrefix;

	@Value("${linphoneVerifySMSMaxCount}")
	private Integer linphoneSMSMaxCount;

	@Value("${linphoneTokenKeyPrefix}")
	private String linphoneTokePrefix;

	@Value("${linphoneTokenExpireTime}")
	private int linphoneTokenExpireTime;

	@Override
	public RemoteModelResult<Account> queryByAccountName(String accountName, int type) {
		logger.info("queryByAccountName {}  {}", accountName,  type);
		isAnyNull(accountName, type);
		try {
			return returnAccount(selectAccount(accountName, type));
		} catch (ECPBusinessException e) {
			return new RemoteModelResult<Account>(null);
		}
	}

	@Override
	public RemoteModelResult<Account> queryByAccountNameAndPsw(String accountName, int type,
			String password) {
		logger.info("queryByAccountNameAndPsw: {}  {}  {}" , accountName, type, password);
		password = handlePsw(password);//允许无密码登录
		isAnyNull(accountName, type);
		// 查询对象
		AccountExample example = new AccountExample();
		example.createCriteria().andAccountNameEqualTo(accountName).andTypeEqualTo(type)
				.andPasswordEqualTo(password);

		List<Account> accountList = accountMapper.selectByExample(example);
		return returnAccountByList(accountList);
	}

	@Override
	public LinphoneResult queryVerificationCode(String mobile) {
		Object object= springRedisTools.getByKey(linphoneSMSCodePrefix + mobile);
		int verifyCode=getRandomSixNum();
		int count=0;
		String content="【翔恒科技】您的验证码为：" + verifyCode + "，请在5分钟内使用，超时请重新获取。";
		if(object==null){//不存在
			smsService.sendText(content,mobile);
			springRedisTools.addData(linphoneSMSCodePrefix+mobile,verifyCode,5, TimeUnit.MINUTES);
			springRedisTools.addData(linphoneSMSCountPrefix+mobile,1,24,TimeUnit.HOURS);
		}else {
			smsService.sendText(content,mobile);
			springRedisTools.updateDataByKey(linphoneSMSCodePrefix+mobile,verifyCode);
			springRedisTools.updateDataByKey(linphoneSMSCountPrefix+mobile,count+1);
		}
		logger.debug("手机号:{},验证码为:{}",mobile,verifyCode);
		HashMap<String,String> hashMap=new HashMap(3);
		hashMap.put("verificationCode",String.valueOf(verifyCode));
		hashMap.put("mobile",mobile);
		hashMap.put("count",count+1+"");
		return new LinphoneResult(hashMap);
	}

	@Override
	public LinphoneResult queryAccountLogin(String mobile, String password, String sourceType, int loginType) {
		AccountExample accountExample=new AccountExample();
		AccountExample.Criteria criteria=accountExample.createCriteria();
		criteria.andMobileEqualTo(mobile);
		criteria.andPasswordEqualTo(password);
		criteria.andTypeEqualTo(loginType);
		logger.debug("开始查找此账号密码账户!账号:{},密码:{}",mobile,password);
		List<Account> accounts=accountMapper.selectByExample(accountExample);
		if(accounts.size()==0){
			logger.debug("未找到此账号密码账户!账号:{},密码:{}",mobile,password);
			return new LinphoneResult(ReturnCode.API_LOGIN_FAILURE);
		}else if(accounts.size()==1){
			Account account=accounts.get(0);
			//生成token令牌
			String token= TokenGenUtils.generateToken(account.getAccountId());
			if(!StringUtils.isNotEmpty(token)){
				logger.error("账号token生成失败!拒绝处理");
				new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
			}
			springRedisTools.addData(linphoneTokePrefix+token,JSON.toJSONString(account),linphoneTokenExpireTime,TimeUnit.MINUTES);
			AccountDto accountDto=new AccountDto(account);
			accountDto.setToken(token);
			accountDto.setMobile(mobile);
			logger.debug("找到账号，开始回写数据:{}",accountDto);
			return new LinphoneResult(accountDto);
		}else {//查询到多个账号数据库数据错误!
			logger.error("数据库数据错误,通过账号密码查询到多条数据!账号:{},密码:{}",mobile,password);
			return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
		}
	}

	@Override
	public LinphoneResult resetPassword(String password, String mobile, String verificationCode){
		Object storedCode=springRedisTools.getByKey(linphoneSMSCodePrefix+mobile);
		if(storedCode==null){
			return new LinphoneResult(ResponseCode.FIND_PASSWORD_VALIDATE_CODE_ERROR);
		}
		if(Integer.parseInt(verificationCode)!=((int)storedCode)){
			return new LinphoneResult(ResponseCode.FIND_PASSWORD_VALIDATE_CODE_ERROR);
		}
		AccountExample accountExample=new AccountExample();
		accountExample.createCriteria().andMobileEqualTo(mobile).andTypeEqualTo(Dict.ACCOUNT_TYPE_LY_USER.getIntValue());
		List<Account> accounts=accountMapper.selectByExample(accountExample);
		if(accounts.size()==1){
			Account account=accounts.get(0);
			account.setPassword(password);
			account.setUpdateTime(new Date());
			int count=accountMapper.updateByPrimaryKey(account);
			if(count==0){
				logger.error("重置密码失败!");
				return new LinphoneResult(ResponseCode.RESOLVE_FAIL);
			}else {
				return new LinphoneResult(account);
			}
		}
		return new LinphoneResult(ResponseCode.FIND_PASSWORD_COUNT_ERROR);
	}

	@Override
	public LinphoneResult modifyPassword(Account account, String password){
		account.setPassword(password);
		int count=accountExtraMapper.updateAccountById(account.getAccountId(),password);
		if(count==0){
			logger.error("修改密码失败!");
			return new LinphoneResult(ResponseCode.RESOLVE_FAIL);
		}else {
			return new LinphoneResult(ResponseCode.RESOLVE_SUCCESS);
		}
	}

	@Override
	public LinphoneResult loginOut(String accountId) {
		return new LinphoneResult(ResponseCode.RESOLVE_SUCCESS);
	}

	@Override
	public RemoteModelResult<List<String>> queryAccountBuildingR(String mobile) {
		List<String> buildingCodeList=accountExtraMapper.selectAccountBuildingR(mobile);
		//保存到数据库
		if(!buildingCodeList.isEmpty()){
			List<Map<String,String>> paramList=new ArrayList<>(buildingCodeList.size());
			for (String code:buildingCodeList){
				Map<String,String> paramMap=new HashMap<>(2);
				paramMap.put("mobile",mobile);
				paramMap.put("buildingCode",code);
				paramList.add(paramMap);
			}
			if(!paramList.isEmpty()){
				accountExtraMapper.batchInsertAccountHouse(paramList);
			}
		}
		return new RemoteModelResult<>(buildingCodeList);
	}

	@Override
	public RemoteModelResult<Account> queryBySourceId(String sourceId, int type) {
		isAnyNull(sourceId, type);
		// 查询对象
		AccountExample example = new AccountExample();
		example.createCriteria().andSourceIdEqualTo(sourceId).andTypeEqualTo(type);
		List<Account> accountList = accountMapper.selectByExample(example);
		return returnAccountByList(accountList);
	}

	@Override
	public RemoteModelResult<Account> queryByAccountCode(String accountCode) {
		isAnyNull(accountCode);
		// 查询对象
		AccountExample example = new AccountExample();
		example.createCriteria().andAccountCodeEqualTo(accountCode);

		List<Account> accountList = accountMapper.selectByExample(example);
		return returnAccountByList(accountList);
	}

	/**
	 * @description 需要短信验证码的账号类型
	 */
	private boolean needSmsCode(int accountType) {
		return Dict.ACCOUNT_TYPE_LY_USER.equals(accountType);
	}

	@Override
	public RemoteModelResult<Account> register(Account account, String smsCode, String phoneNum) {

		account.setPassword(handlePsw(account.getPassword()));// 允许无密码注册

		if (needSmsCode(account.getType())) {
			if (StringUtils.isBlank(phoneNum) || StringUtils.isBlank(smsCode)) {
				throwECPException(ReturnCode.PF_PHONE_NUMBER_OR_SMSCODE_ERROR);
			}
			if (!smsService.isVerifyCodeCorrect(phoneNum, smsCode, false).getModel()) {
				throwECPException(ReturnCode.PF_SMS_CODE_ERROR);
			}
		}

		// 验证参数
		if (StringUtils.isBlank(account.getAccountName())) {
			throwECPException(ReturnCode.PF_ACCOUNT_NAME_FORMAT_INCORRECT);
		} else if (account.getType() == null) {
			throwECPException(ReturnCode.PF_PARAMS_MISSING);
		}
		
		
		// 验证是否存在重复账号名
		AccountExample example = new AccountExample();
		example.createCriteria().andAccountNameEqualTo(account.getAccountName())//
				.andTypeEqualTo(account.getType());
		List<Account> list = accountMapper.selectByExample(example);
		checkExisted(list);
		// 验证是否存在重复来源ID
		if (StringUtils.isNotBlank(account.getSourceId())) {
			AccountExample example2 = new AccountExample();
			example2.createCriteria().andSourceIdEqualTo(account.getSourceId())//
					.andTypeEqualTo(account.getType());
			List<Account> list2 = accountMapper.selectByExample(example2);
			checkExisted(list2);
		}

		// 设置账号初始值
		setinitialAccountValue(account, null);

		accountMapper.insert(account);

		if (needPush(account.getType())) {
			// 推送到SIP和Openfire接口
			ArrayList<Account> accountList = new ArrayList<Account>();
			accountList.add(account);
			sipApiService.register(accountList);
			openfireApiService.register(accountList);
		}
		/*ArrayList<Account> accountList = new ArrayList<Account>();
		if(Dict.ACCOUNT_TYPE_LY_USER.getIntValue() == account.getType()){//铃音都推
			accountList.add(account);
			sipApiService.register(accountList);
			openfireApiService.register(accountList);
		}else if(Dict.ACCOUNT_TYPE_HOUSE.getIntValue() == account.getType()){//房屋只推sip
			accountList.add(account);
			sipApiService.register(accountList);
		}*/

		if (needSmsCode(account.getType())) {
			smsService.deletePhoneNum(phoneNum);
		}
		return returnAccount(account);
	}

	@Override
	public LinphoneResult registerLinphone(Account account, String verificationCode, String mobile) {
		/**
		 * 单实例并发控制
		 */
		if(isCached(mobile)){
			throwECPException(ReturnCode.API_MOBILE_EXISTED);
		}
		boolean sipFlag= false;
		boolean opFlag= false;
		try {
			// 验证是否存在重复账号名
			AccountExample example = new AccountExample();
			example.createCriteria().andAccountNameEqualTo(account.getAccountName())//
                    .andTypeEqualTo(account.getType());
			List<Account> list = accountMapper.selectByExample(example);
			if(list.size()>0){
                return new LinphoneResult(ReturnCode.API_USER_EXISTED);
            }
			AccountExample example1=new AccountExample();
			example1.createCriteria().andMobileEqualTo(mobile);
			long count=accountMapper.countByExample(example1);
			if(count>0){
                return new LinphoneResult(ReturnCode.API_MOBILE_EXISTED);
            }
			account.setAccountId(UUID.randomUUID().toString());
			account.setAccountCode(UUID.randomUUID().toString());
			account.setState(Dict.ACCOUNT_STATE_NORMAL.getIntValue());
			account.setCreateTime(new Date());
			account.setUpdateTime(new Date());
			account.setAccountName(TokenGenUtils.generateToken(mobile));
			account.setMobile(mobile);
			ArrayList<Account> accountList = new ArrayList<Account>();
			accountList.add(account);
			sipFlag = sipApiService.register(accountList);
			opFlag = openfireApiService.register(accountList);
		} catch (Exception e) {

		} finally {
			removeMobileCache(mobile);
		}
		if(sipFlag&&opFlag) {
			accountMapper.insert(account);
			logger.info("{}注册成功！",mobile);
			HashMap hashMap=new HashMap(4);
			hashMap.put("accountId",account.getAccountId());
			hashMap.put("accountName",account.getAccountName());
			hashMap.put("accountCode",account.getAccountCode());
			hashMap.put("registerTime",account.getCreateTime());
			return new LinphoneResult(hashMap);
		}else {
			logger.info("{}注册失败,同步sip或openfire错误",mobile);
			return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
		}
	}

	@Override
	public RemoteModelResult<List<String>> queryAccountHouse(String mobile) {
		logger.debug("根据mobile查询资产绑定关系:{}",mobile);
		return new RemoteModelResult<>(accountExtraMapper.selectCodesByMobile(mobile));
	}

	@Override
	public RemoteModelResult addAccountHouse(List<Map<String, String>> value) {
		logger.debug("批量增加资产绑定关系,绑定关系列表:{}",JSON.toJSONString(value));
		accountExtraMapper.batchInsertAccountHouse(value);
		return new RemoteModelResult(null);
	}

	@Override
	public RemoteModelResult delAccountHouse(String mobile) {
		logger.debug("根据手机号批量删除资产绑定关系,手机号:{}",mobile);
		accountExtraMapper.delUserHouse(mobile);
		return new RemoteModelResult(null);
	}

	@Override
	public RemoteModelResult<HashMap> registerApi(Account account, String mobile) {
		/**
		 * 单实例并发控制
		 */
		if(isCached(mobile)){
			throwECPException(ReturnCode.API_MOBILE_EXISTED);
		}
		try {
			// 验证是否存在重复账号名
			AccountExample example = new AccountExample();
			example.createCriteria().andAccountNameEqualTo(account.getAccountName())//
                    .andTypeEqualTo(account.getType());
			List<Account> list = accountMapper.selectByExample(example);
			if(list.size()>0){
                logger.info("手机号:{},注册失败。用户已存在！",mobile);
                throwECPException(ReturnCode.API_USER_EXISTED);
            }
			AccountExample example1=new AccountExample();
			example1.createCriteria().andMobileEqualTo(mobile).andTypeEqualTo(account.getType());
			long count=accountMapper.countByExample(example1);
			if(count>0){
                logger.info("手机号:{},注册失败。手机号已存在！",mobile);
                throwECPException(ReturnCode.API_MOBILE_EXISTED);
            }
			account.setAccountId(UUID.randomUUID().toString());
			account.setAccountCode(StringUtils.isBlank(account.getAccountCode())?generateAccountCode():account.getAccountCode());
			account.setState(Dict.ACCOUNT_STATE_NORMAL.getIntValue());
			account.setCreateTime(new Date());
			account.setUpdateTime(new Date());
			account.setAccountName(mobile);//accountName 设置为mobile
			account.setMobile(mobile);
			accountMapper.insert(account);
		} catch (ECPBusinessException e) {

		} finally {
			removeMobileCache(mobile);
		}
		ArrayList<Account> accountList = new ArrayList<Account>();
		accountList.add(account);
		if(account.getType().equals(Dict.ACCOUNT_TYPE_LY_USER.getIntValue())) {
			sipApiService.register(accountList);
			openfireApiService.register(accountList);
		}
		logger.info("手机号:{},注册成功！",mobile);
		HashMap hashMap=new HashMap(4);
		hashMap.put("accountId",account.getAccountId());
		hashMap.put("accountName",account.getAccountName());
		hashMap.put("accountCode",account.getAccountCode());
		hashMap.put("registerTime",account.getCreateTime());
		return new RemoteModelResult<>(hashMap);
	}

	private boolean isCached(String mobile){
		if(mobileCache.containsKey(mobile)){
			return true;
		}else {
			mobileCache.put(mobile,mobile);
			return false;
		}
	}

	private void removeMobileCache(String mobile){
		mobileCache.remove(mobile);
	}

	@Override
	public RemoteModelResult<HashMap<String,String>> queryVerifyCodeApi(String mobile, String codePrefix, String countPrefix, Integer sMSMaxCount) {
		Object object= springRedisTools.getByKey(codePrefix + mobile);
		int verifyCode=getRandomSixNum();
		int count=0;
		Object storeCount=springRedisTools.getByKey(countPrefix+mobile);
		if(storeCount!=null){
			count= (int) storeCount;
		}
		String content="【翔恒科技】您的验证码为：" + verifyCode + "，请在5分钟内使用，超时请重新获取。";
		if(object==null){//不存在
			smsService.sendText(content,mobile);
			springRedisTools.addData(codePrefix+mobile,verifyCode,5, TimeUnit.MINUTES);
			if(storeCount==null) {
				springRedisTools.addData(countPrefix + mobile, 1, 24, TimeUnit.HOURS);
			}else {
				springRedisTools.updateDataByKey(countPrefix+mobile,count+1);
			}
		}else {
			smsService.sendText(content,mobile);
			springRedisTools.updateDataByKey(codePrefix+mobile,verifyCode);
			springRedisTools.updateDataByKey(countPrefix+mobile,count+1);
		}
		HashMap<String,String> hashMap=new HashMap(3);
		hashMap.put("verificationCode",String.valueOf(verifyCode));
		hashMap.put("mobile",mobile);
		hashMap.put("count",count+1+"");
		return new RemoteModelResult<>(hashMap);
	}

	@Override
	public RemoteModelResult<AccountDto> queryLoginApi(String mobile, String password,int accountType, String tokenKeyPrefix,int expireTime) {
		AccountExample accountExample=new AccountExample();
		AccountExample.Criteria criteria=accountExample.createCriteria();
		criteria.andMobileEqualTo(mobile);
		criteria.andPasswordEqualTo(password).andTypeEqualTo(accountType);
		List<Account> accounts=accountMapper.selectByExample(accountExample);
		if(accounts.size()==0){
			logger.debug("根据登录信息,未查询到账户.手机号:{}",mobile);
			throwECPException(ReturnCode.API_LOGIN_FAILURE);
		}else if(accounts.size()==1){
			Account account=accounts.get(0);
			//生成token令牌
			String token= TokenGenUtils.generateToken(account.getAccountId());
			if(!StringUtils.isNotEmpty(token)){
				logger.error("账号token生成失败!拒绝处理");
				throwECPException(ReturnCode.API_RESOLVE_FAIL);
			}
			springRedisTools.addData(tokenKeyPrefix+token,JSON.toJSONString(account),expireTime,TimeUnit.MINUTES);
			AccountDto accountDto=new AccountDto(account);
			accountDto.setToken(token);
			accountDto.setMobile(mobile);
			logger.debug("登录成功,回写数据:{}",accountDto);
			return new RemoteModelResult<>(accountDto);
		}else {//查询到多个账号数据库数据错误!
			logger.error("数据库数据错误,通过账号密码查询到多条数据!");
			throwECPException(ReturnCode.API_RESOLVE_FAIL);
		}
		return new RemoteModelResult<>(null);
	}

	@Override
	public RemoteModelResult resetPasswordApi(String accountName,int accountType,String password, String mobile) {
		AccountExample accountExample=new AccountExample();
		accountExample.createCriteria().andMobileEqualTo(mobile).andTypeEqualTo(accountType);
		List<Account> accounts=accountMapper.selectByExample(accountExample);
		if(accounts.size()==1){
			Account account=accounts.get(0);
			account.setPassword(password);
			int count=accountMapper.updateByPrimaryKey(account);
			if(count==0){
				logger.error("重置密码失败!账户:{}",account);
				throwECPException(ReturnCode.API_RESOLVE_FAIL);
			}else {
				if(accountType==Dict.ACCOUNT_TYPE_LY_USER.getIntValue()) {
					asyncToOpenFireAndSIP(account);
				}
				return new RemoteModelResult(null);
			}
		}
		logger.debug("未查询到账户,手机号:{}",mobile);
		throwECPException(ReturnCode.API_FIND_PASSWORD_COUNT_ERROR);
		return new RemoteModelResult(null);
	}

	@Override
	public RemoteModelResult modifyPasswordApi(Account account, String password) {
		account.setPassword(password);
		int count=accountExtraMapper.updateAccountById(account.getAccountId(),password);
		if(count==0){
			logger.debug("修改密码失败!账户:{}",account);
			throwECPException(ReturnCode.API_RESOLVE_FAIL);
		}else {
			if (account.getType().equals(Dict.ACCOUNT_TYPE_LY_USER.getIntValue())) {
				asyncToOpenFireAndSIP(account);
			}
			logger.debug("修改密码成功。账户:{}",account);
			return new RemoteModelResult(null);
		}
		return new RemoteModelResult(ReturnCode.API_RESOLVE_FAIL);
	}

	@Override
	public RemoteModelResult selectRegisterWyCompany(String accountId,String mobile) {
		if(StringUtils.isNotEmpty(mobile)) {
			//查询是否身份认证
			int count = identityExtraMapper.checkMobile(mobile);
			if (count == 0) {
				logger.debug("身份认证信息不存在");
				throwECPException(ReturnCode.API_REGISTER_COMPANY_IDENTITY_NOT_EXISTS);
			} else {
				logger.debug("查询到身份认证信息");
				//查询注册公司审核情况
				CompanyApprovalExample companyApprovalExample=new CompanyApprovalExample();
				companyApprovalExample.createCriteria().andAccountIdEqualTo(accountId);
				List<CompanyApproval> companyApprovalList=companyApprovalMapper.selectByExample(companyApprovalExample);
				if(companyApprovalList.isEmpty()) {//未注册公司
					return new RemoteModelResult();
				}else {
					boolean flag=true;
					for(CompanyApproval companyApproval:companyApprovalList){
						Integer status=companyApproval.getStatus();
						if(status!=0) {//不通过
							flag=false;
							break;
						}
					}
					if(!flag){
						throwECPException(ReturnCode.API_REGISTER_COMPANY_PERMISSION_DENY);
					}else {
						return new RemoteModelResult();
					}
				}
			}
		}else {
			logger.error("用户手机号为空拒绝处理!");
			throwECPException(ReturnCode.API_RESOLVE_FAIL);
		}
		return new RemoteModelResult();
	}

	@Override
	public RemoteModelResult updateCompanyIdByAccountNameAndType(String accountName, int type, String companyId) {
		int count=accountExtraMapper.updateCompanyIdByANT(accountName,type,companyId);
		if(count>0){
			return new RemoteModelResult(ReturnCode.API_RESOLVE_SUCCESS);
		}else {
			return new RemoteModelResult(ReturnCode.API_RESOLVE_FAIL);
		}
	}

	@Override
	public RemoteModelResult<Account> cancelAccount(String accountName, int type) {
//		password = StringUtils.defaultIfBlank(password, Dict.ACCOUNT_DEFAULT_PSW.getStringValue());// 允许无密码登录

		// 查询对象
		Account accountObj = queryByAccountName(accountName, type).getModel();


		// 删除账号绑定关系
		accountAndHouseExtraMapper.deleteByAccountId(accountObj.getAccountId());
		// 删除账号
		accountExtraMapper.deleteById(accountObj.getAccountId());

		// 调用SIP / openfire接口
		if (needPush(type)) {
			ArrayList<Account> list = new ArrayList<Account>();
			list.add(accountObj);
			sipApiService.cancel(list);
			openfireApiService.cancel(list);
		}
		return new RemoteModelResult<Account>();
	}

	@Override
	public RemoteModelResult<Account> exDataUpdate(String accountName, int type, String newExData) {
		CommonUtils.isAnyNull(accountName,type,newExData);

		// 查询账号
		Account account = selectAccount(accountName, type);
		// Account account = selectAccountWithPsw(accountName, type, password);

		JSONObject newExDataObj = JSON.parseObject(newExData);
		JSONObject originalExDataObj = JSON.parseObject(account.getExData());

		// 更新额外信息
		if (originalExDataObj == null) {
			originalExDataObj = newExDataObj;
		} else {
			originalExDataObj.putAll(newExDataObj);
		}

		// 设置当前推送类型
		if (newExDataObj.containsKey(Dict.EXDATA_ATTR_KEY_IOS_PRO_APP_TOKEN.getStringValue())) {
			originalExDataObj.put((String) Dict.EXDATA_ATTR_KEY_CURR_IOS_TYPE.getStringValue(),
					Dict.EXDATA_ATTR_CURR_PUSH_TYPE_PRO.getStringValue());
		} else if (newExDataObj.containsKey(Dict.EXDATA_ATTR_KEY_IOS_ENT_APP_TOKEN.getStringValue())) {
			originalExDataObj.put((String) Dict.EXDATA_ATTR_KEY_CURR_IOS_TYPE.getStringValue(),
					Dict.EXDATA_ATTR_CURR_PUSH_TYPE_ENT.getStringValue());
		}

		Account updateAccount = new Account();
		updateAccount.setAccountId(account.getAccountId());
		updateAccount.setExData(originalExDataObj.toJSONString());
		accountMapper.updateByPrimaryKeySelective(updateAccount);
		return returnSuccess();
	}

	/**
	 * @description 某些账号类型需要推送给sip和openfire
	 */
	private boolean needPush(int accountType) {
		return (accountType == Dict.ACCOUNT_TYPE_LY_USER.getIntValue() ||
				accountType == Dict.ACCOUNT_TYPE_ANDROID_MKJ.getIntValue() ||
				accountType == Dict.ACCOUNT_TYPE_IOS_MKJ.getIntValue() ||
				accountType == Dict.ACCOUNT_TYPE_HOUSE.getIntValue());
	}

	/**
	 * @description 是否重复账号
	 */
	private void checkExisted(List<Account> list) {
		if (!list.isEmpty()) {// 已存在
			Account temp = list.get(0);
			if (Dict.ACCOUNT_STATE_CANCEL.equals(temp.getState())) {
				throwECPException(ReturnCode.PF_ACCOUNT_CANCELED);
			} else {
				throwECPException(ReturnCode.PF_ACCOUNT_EXISTED);
			}
		}

	}

	private String handlePsw(String psw) {
		return StringUtils.isBlank(psw) ? Dict.ACCOUNT_DEFAULT_PSW.getStringValue() : psw;
	}

	private Account setinitialAccountValue(Account account, Integer type) {
		if (!CommonUtils.isEmpty(type)) {
			account.setType(type);
		}
		account.setAccountId(UUID.randomUUID().toString());
		account.setPassword(handlePsw(account.getPassword()));
		account.setAccountCode(StringUtils.isBlank(account.getAccountCode())?generateAccountCode():account.getAccountCode());
		account.setState(Dict.ACCOUNT_STATE_NORMAL.getIntValue());
		account.setCreateTime(new Date());
		account.setUpdateTime(new Date());

		return account;
	}

	/**
	 * @description 生成内部账号算法
	 */
	private String generateAccountCode() {
		return UUID.randomUUID().toString();
	}
	
	public Account selectAccountWithPsw(String accountName, int type,String psw) {
		// 查询对象
		AccountExample example = new AccountExample();
		example.createCriteria().andAccountNameEqualTo(accountName).andTypeEqualTo(type).andPasswordEqualTo(handlePsw(psw));
		
		List<Account> accountList = accountMapper.selectByExample(example);
		return checkAccountException(accountList.size() > 0 ? accountList.get(0) : null);
	}

	public Account selectAccount(String accountName, int type) {
		// 查询对象
		AccountExample example = new AccountExample();
		example.createCriteria().andAccountNameEqualTo(accountName).andTypeEqualTo(type);

		List<Account> accountList = accountMapper.selectByExample(example);
		return checkAccountException(accountList.size() > 0 ? accountList.get(0) : null);
	}

	/**
	 * @description 检查账号异常情况
	 */
	private Account checkAccountException(Account account) {
		if (account == null) {
			throwECPException(ReturnCode.PF_ACCOUNT_OR_PSW_INCORRECT);
		} else {
			if (Dict.ACCOUNT_STATE_CANCEL.equals(account.getState())) {//账号已被注销
				throwECPException(ReturnCode.PF_ACCOUNT_CANCELED);
			}
		}
		return account;
	}

	private RemoteModelResult<Account> returnAccountByList(List<Account> accountList) {
		return returnAccount(accountList.isEmpty() ? null : accountList.get(0));
	}

	private RemoteModelResult<Account> returnAccount(Account account) {
		checkAccountException(account);
		return returnRslt(account);
	}

	private <T> RemoteModelResult<T> returnRslt(T model) {
		RemoteModelResult<T> result = new RemoteModelResult<T>();
		result.setModel(model);
		return result;
	}

	@Override
	public RemoteModelResult<Account> queryMaxAccountName(int type) {
		Account account = accountExtraMapper.queryMaxAccountName(type);
		return returnAccount(account);
	}

	private Integer getRandomSixNum() {
		return Double.valueOf(((Math.random() + 1) * 100000)).intValue();
	}

	private void asyncToOpenFireAndSIP(Account account){
		List<Account> accountList=new ArrayList<>(1);
		accountList.add(account);
		logger.debug("开始同步账户到sip服务器,账户:{}",account);
		sipUtils.modifyPwd(accountList);
		logger.debug("开始同步账户到openfire服务器。账户:{}",account);
		openFireUtils.modifyPwd(accountList);
		logger.debug("sip、openFire账号同步完成!");
	}

	@Override
	public RemoteModelResult<Account> cancelAccount(String accountName, int type, String password) {
		password = StringUtils.defaultIfBlank(password, Dict.ACCOUNT_DEFAULT_PSW.getStringValue());// 允许无密码登录

		// 查询对象
		Account accountObj = queryByAccountNameAndPsw(accountName, type, password).getModel();

		// 删除账号绑定关系
		accountAndHouseExtraMapper.deleteByAccountId(accountObj.getAccountId());
		// 删除账号
		accountExtraMapper.deleteById(accountObj.getAccountId());

		// 调用SIP / openfire接口
		ArrayList<Account> list = new ArrayList<Account>();
	
		if( Dict.ACCOUNT_TYPE_LY_USER.getIntValue() == type){//房屋只推sip
			list.add(accountObj);
			sipApiService.cancel(list);
			openfireApiService.cancel(list);
		}else if( Dict.ACCOUNT_TYPE_HOUSE.getIntValue() == type){//铃音都推
			list.add(accountObj);
			sipApiService.cancel(list);
		}
		
		return new RemoteModelResult<Account>();
	}

	@Override
	public RemoteModelResult updateMobile(String accountId, String mobile,String OldMobile) {
		int mobileCount=accountExtraMapper.checkMobileExists(mobile);
		if(mobileCount>0){
			throwECPException(ReturnCode.API_LINPHONE_RESET_MOBILE_MOBILE_EXISTS_ERROR);
		}else{
			int count=accountExtraMapper.updateMobile(accountId,mobile);
			if(count==0){
				throwECPException(ReturnCode.API_RESOLVE_FAIL);
			}

			mobileCount = accountExtraMapper.checkMobilePerson(OldMobile);

			if(mobileCount == 0){
				logger.debug("客户关系表中没有此手机号客户!");
			}else{
				count=accountExtraMapper.updatepersonMobile(mobile,OldMobile);
				if(count==0){
					throwECPException(ReturnCode.API_RESOLVE_FAIL);
				}
			}

		}
		return new RemoteModelResult();
	}

	@Override
	public RemoteModelResult getCompanyId(String mobile) {
		return new  RemoteModelResult<>(personCustMapper.getCompanyId(mobile));
	}


	@Override
	public RemoteModelResult<List<Account>> findByCondition(AccountExample condition) {
		RemoteModelResult remoteModelResult = new RemoteModelResult();
		List<Account> accountList = accountMapper.selectByExample(condition);
		remoteModelResult.setModel(accountList);
		return remoteModelResult;
	}
}