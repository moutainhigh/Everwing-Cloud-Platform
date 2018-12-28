package com.everwing.coreservice.platform.api;

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.AccountDto;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.exception.ExceptionProxy;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.constant.SourceTypeEnum;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.AccountExample;
import com.everwing.coreservice.platform.api.util.ServiceResources;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.everwing.coreservice.common.utils.CommonUtils.throwECPException;

@Service
public class AccountApi extends ServiceResources{

	@Autowired
	private SpringRedisTools springRedisTools;

	@Value("${linphoneVerifySMSKeyPrefix}")
	private String linphoneSMSCodePrefix;

	@Value("${linphoneSMSKeyCountPrefix}")
	private String linphoneSMSCountPrefix;

	@Value("${linphoneVerifySMSMaxCount}")
	private Integer linphoneSMSMaxCount;

	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(AccountApi.class);

	public RemoteModelResult<Account> queryMaxAccountName(int type) {
		return accountService.queryMaxAccountName(type);
	}

	@ExceptionProxy
	public RemoteModelResult<Account> queryByAccountName(String accountName, int type) {
		return accountService.queryByAccountName(accountName, type);
	}

	public RemoteModelResult<Account> queryByAccountNameAndPsw(String accountName, int type,
			String password) {
		return accountService.queryByAccountNameAndPsw(accountName, type, password);
	}

	/*-参数		参数名称	类型	必填	说明	示例
	accountName	账号	String	是		
	password	密码	String	否	如有，需要RC4 + Base64加密；如无，不需要传此字段或值为空字符串	
	type		来源类型	Integer	是	账号类型说明	
	smsCode		验证码	String	否	以下业务类型注册需要验证码：0，2	
	phoneNum	接收验证码的手机号码	String	否	以下业务类型注册需要手机号码：0，2	
	exData		额外信息	String	否	账号的额外信息，一般是JSON字符串格式，长度不超过1000	*/
	public RemoteModelResult<Account> register(Account account, String smsCode, String phoneNum) {
		return accountService.register(account, smsCode, phoneNum);
	}
	
	 public RemoteModelResult<Account> queryByAccountCodeWithoutException(String accountCode) {
		 try {
			 return accountService.queryByAccountCode(accountCode);
		 } catch (Exception e) {
			 return new RemoteModelResult<Account>(null);
		 }
	 }

	public RemoteModelResult<Account> queryByAccountCode(String accountCode) {
		return accountService.queryByAccountCode(accountCode);
	}

	public RemoteModelResult<Account> cancelAccountByAccountCode(String accountCode) {
		RemoteModelResult<Account> model = queryByAccountCode(accountCode);
		Account account = model.getModel();
		if(account == null){
			return new RemoteModelResult<Account>();
		}
		return cancelAccount(account.getAccountName(),account.getType());
	}
	
	public RemoteModelResult<Account> cancelAccount(String accountName, int type,String password) {
		RemoteModelResult<Account> result = new RemoteModelResult<Account>();
		try {
			return accountService.cancelAccount(accountName, type,password);
		} catch (ECPBusinessException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (Exception e) {
			result.setCode(ReturnCode.PF_ACCOUNT_CANCELED.getCode());
			result.setMsg(ReturnCode.PF_ACCOUNT_CANCELED.getDescription());
			return result;
		}
		return result;
	}
	
	public RemoteModelResult<Account> cancelAccount(String accountName, int type) {
		RemoteModelResult<Account> result = new RemoteModelResult<Account>();
		try {
			return accountService.cancelAccount(accountName, type);
		} catch (ECPBusinessException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (Exception e) {
			result.setCode(ReturnCode.PF_ACCOUNT_CANCELED.getCode());
			result.setMsg(ReturnCode.PF_ACCOUNT_CANCELED.getDescription());
			return result;
		}
		return result;
	}

	public RemoteModelResult<Account> exDataUpdate(String accountName, int type, String newExData) {
		return accountService.exDataUpdate(accountName, type,  newExData);
	}

	public RemoteModelResult<Account> queryBySourceId(String sourceId, int type) {
		return accountService.queryBySourceId(sourceId, type);
	}

	/**
	 * 获取验证码
	 * @param mobile 手机号码
	 * @return 通用返回结果
	 */
	@NoExceptionProxy
	public LinphoneResult getVerificationCode(String mobile){
		Object value=springRedisTools.getByKey(linphoneSMSCountPrefix+mobile);
		int count=0;
		if(value!=null){
			count= (int) value;
		}
		if(count>linphoneSMSMaxCount){
			return new LinphoneResult(ReturnCode.API_VALIDATE_CODE_MAX_TIME);
		}
		return accountService.queryVerificationCode(mobile);
	}

	/**
	 * 登录
	 * @param mobile 用户手机号码
	 * @param password   密码
	 * @param sourceType  登录设备类型
	 * @param loginType 登录类型(铃音或其他)
	 * @return 通用返回结果
	 */
	@NoExceptionProxy
    public LinphoneResult login(String mobile, String password, String sourceType, int loginType) {
		if(!SourceTypeEnum.isDefined(sourceType)){
			new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
			logger.error("登录设备类型不在系统内,拒绝处理!");
		}
		return accountService.queryAccountLogin(mobile,password,sourceType,loginType);
    }

	/**
	 * 邻音重置密码
	 * @param password 重置后的新密码
	 * @param mobile   用户手机号码
	 * @param verificationCode 找回密码的手机验证码
	 * @return 邻音通用返回结果
	 */
	@NoExceptionProxy
    public LinphoneResult resetPassword(String password, String mobile, String verificationCode) {
		return accountService.resetPassword(password,mobile,verificationCode);
    }

	/**
	 * 修改密码
	 * @param account 当前登录账号
	 * @param password 新密码
	 * @return
	 */
	@NoExceptionProxy
	public LinphoneResult modifyPassword(Account account, String password) {
		return accountService.modifyPassword(account,password);
	}

	/**
	 * 登出系统
	 * @param accountId 账号ID
	 * @return 邻音通用返回结果
	 */
	@NoExceptionProxy
	public LinphoneResult loginOut(String accountId) {
		return accountService.loginOut(accountId);
	}

	/**
	 * 邻音注册
	 * @param account 注册信息
	 * @param verificationCode 验证码
	 * @param mobile 手机号码
	 * @return 邻音统一返回结果
	 */
	@NoExceptionProxy
	public LinphoneResult registerLinPhone(Account account, String verificationCode, String mobile) {
		//验证验证码
		Object val=springRedisTools.getByKey(linphoneSMSCodePrefix + mobile);
		if(val!=null) {
			if (Integer.parseInt(verificationCode) != (int) val) {
				return new LinphoneResult(ReturnCode.API_MOBILE_VALIDATE_CODE_ERROR);
			}
		}else{
			return new LinphoneResult(ReturnCode.API_MOBILE_VALIDATE_CODE_ERROR);
		}
		return accountService.registerLinphone(account,verificationCode,mobile);
	}

	/**
	 * 注册api
	 * @param account 注册用户信息
	 * @param verificationCode 注册验证码
	 * @param mobile  注册手机号
	 * @param cachePrefix redis中key前缀
	 * @return 通用返回结果
	 */
	public RemoteModelResult<HashMap> registerApi(Account account, String verificationCode, String mobile, String cachePrefix) {
		//验证验证码
		logger.info("开始验证验证码.");
		Object val=springRedisTools.getByKey(cachePrefix + mobile);
		if(val!=null) {
			if (Integer.parseInt(verificationCode) != (int) val) {
				logger.info("验证码验证失败!");
				throwECPException(ReturnCode.API_MOBILE_VALIDATE_CODE_ERROR);
			}
		}
		logger.info("验证码验证通过.");
		return accountService.registerApi(account,mobile);
	}

	/**
	 * 查询账号房间绑定关系
	 * @param mobile 手机号
	 * @return 统一返回结果
	 */
	public RemoteModelResult<List<String>> queryAccountBuildingR(String mobile) {
		return accountService.queryAccountBuildingR(mobile);
	}

	/**
	 * 查询系统已绑定的资产
	 * @param mobile 手机号
	 * @return 统一返回结果
	 */
	public RemoteModelResult<List<String>> queryAccountHouse(String mobile) {
		return accountService.queryAccountHouse(mobile);
	}

	public RemoteModelResult addAccountHouse(List<Map<String, String>> value){
		return accountService.addAccountHouse(value);
	}

	public RemoteModelResult delAccoutHouse(String mobile){
		return accountService.delAccountHouse(mobile);
	}

	/**
	 * 通用发送验证码api
	 * @param mobile 手机号
	 * @param codePrefix redis中code key前缀
	 * @param countPrefix redis中count key 前缀
	 * @param sMSMaxCount 每天最大发送次数
	 * @return 通用返回对象
	 */
	public RemoteModelResult<HashMap<String,String>> getVerifyCodeApi(String mobile, String codePrefix, String countPrefix, Integer sMSMaxCount) {
		Object value=springRedisTools.getByKey(countPrefix+mobile);
		int count=0;
		if(value!=null){
			count= (int) value;
		}
		if(count>sMSMaxCount){
			throwECPException(ReturnCode.API_VALIDATE_CODE_MAX_TIME);
		}
		return accountService.queryVerifyCodeApi(mobile,codePrefix,countPrefix,sMSMaxCount);
	}

	public RemoteModelResult<AccountDto> loginApi(String mobile, String password,int accountType, String sourceType, String tokenKeyPrefix,int expireTime) {
		if(!SourceTypeEnum.isDefined(sourceType)){
			throwECPException(ReturnCode.API_RESOLVE_FAIL);
			logger.error("登录设备类型不在系统内,拒绝处理!");
		}
		return accountService.queryLoginApi(mobile,password,accountType,tokenKeyPrefix,expireTime);
	}

	public RemoteModelResult resetPasswordApi(String accountName, String password, String mobile,int accountType, String verificationCode, String sMSCodePrefix) {
		Object storedCode=springRedisTools.getByKey(sMSCodePrefix+mobile);
		if(storedCode==null){
			throwECPException(ReturnCode.API_FIND_PASSWORD_VALIDATE_CODE_ERROR);
		}
		if(Integer.parseInt(verificationCode)!=((int)storedCode)){
			throwECPException(ReturnCode.API_FIND_PASSWORD_VALIDATE_CODE_ERROR);
		}
		return accountService.resetPasswordApi(accountName,accountType,password,mobile);
	}

	public RemoteModelResult modifyPasswordApi(Account account, String password) {
		return accountService.modifyPasswordApi(account,password);
	}

	public RemoteModelResult checkCanRegisterWyCompany(String accountId,String mobile){
		return accountService.selectRegisterWyCompany(accountId,mobile);
	}

	public RemoteModelResult updateCompanyIdByAccountNameAndType(String accountName,int type,String companyId){
		return accountService.updateCompanyIdByAccountNameAndType(accountName,type,companyId);
	}

	/**
	 * 物邻
	 * @param accountId
	 * @param mobile
	 * @param OldMobile
	 * @param verificationCode
	 * @param sMSCodePrefix
	 * @return
	 */
	public RemoteModelResult resetMobile(String accountId, String mobile,String OldMobile,String verificationCode, String sMSCodePrefix) {
		Object object=springRedisTools.getByKey(sMSCodePrefix+mobile);
		if(object==null){
			logger.info("未查询到系统内保存的验证码!");
			throwECPException(ReturnCode.API_LINPHONE_RESET_MOBILE_VALIDATE_CODE_ERROR);
		}else {
			String restoredCode=object.toString();
			if(!restoredCode.equals(verificationCode)){
				logger.info("验证码验证失败!");
				throwECPException(ReturnCode.API_LINPHONE_RESET_MOBILE_VALIDATE_CODE_ERROR);
			}
		}
		return accountService.updateMobile(accountId,mobile,OldMobile);
	}


	/**
	 * 重载 邻音
	 * @param accountId
	 * @param mobile
	 * @param oldMobile
	 * @param verificationCode
	 * @return
	 */
	public RemoteModelResult resetMobile(String accountId, String mobile, String oldMobile, String verificationCode) {
		Object object=springRedisTools.getByKey(linphoneSMSCodePrefix+mobile);
		if(object==null){
			logger.info("未查询到系统内保存的验证码!");
			throwECPException(ReturnCode.API_LINPHONE_RESET_MOBILE_VALIDATE_CODE_ERROR);
		}else {
			String restoredCode=object.toString();
			if(!restoredCode.equals(verificationCode)){
				logger.info("验证码验证失败!");
				throwECPException(ReturnCode.API_LINPHONE_RESET_MOBILE_VALIDATE_CODE_ERROR);
			}
		}
		return accountService.updateMobile(accountId,mobile,oldMobile);
	}

	/**
	 * 邻音用户获取公司ID
	 * @return
	 */
	public RemoteModelResult getCompanyId(String mobile) {
		return accountService.getCompanyId(mobile);
	}


	public RemoteModelResult<List<Account>> findByCondition(AccountExample condition){
		return accountService.findByCondition(condition);
	}
}