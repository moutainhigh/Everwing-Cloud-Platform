package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.dto.AccountDto;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.AccountExample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AccountService {
	
	public RemoteModelResult<Account> register(Account account,String smsCode,String phoneNum);
	
	public RemoteModelResult<Account> cancelAccount(String accountName, int type,String password);
	
	public RemoteModelResult<Account> cancelAccount(String accountName, int type);

	public RemoteModelResult<Account> exDataUpdate(String accountName, int type, String newExData);

	public RemoteModelResult<Account> queryByAccountCode(String accountCode);

	public RemoteModelResult<Account> queryBySourceId(String sourceId, int type);

	public RemoteModelResult<Account> queryByAccountName(String accountName, int type);
	
	public RemoteModelResult<Account> queryMaxAccountName(int type);

	public RemoteModelResult<Account> queryByAccountNameAndPsw(String accountName, int type,
			String password);

    LinphoneResult queryVerificationCode(String mobile);

    LinphoneResult queryAccountLogin(String accountName, String password, String sourceType, int loginType);

    LinphoneResult resetPassword(String password, String mobile, String verificationCode);

    LinphoneResult modifyPassword(Account account, String password);

	LinphoneResult loginOut(String accountId);

	/**
	 * 查询用户房间绑定关系
	 * @param mobile 用户账号
	 * @return 房屋buildingCode集合
	 */
	RemoteModelResult<List<String>> queryAccountBuildingR(String mobile);

	LinphoneResult registerLinphone(Account account, String verificationCode, String mobile);

	RemoteModelResult<List<String>> queryAccountHouse(String mobile);

	RemoteModelResult addAccountHouse(List<Map<String,String>> value);

	RemoteModelResult delAccountHouse(String mobile);

	/**
	 * 通用注册api
	 * @param account 账号信息
	 * @param mobile  手机号码
	 * @return 通用返回结果
	 */
	RemoteModelResult<HashMap> registerApi(Account account, String mobile);

	/**
	 * 通用发送验证码api
	 * @param mobile 手机号码
	 * @param codePrefix redis中的code前缀
	 * @param countPrefix redis中的count前缀
	 * @param sMSMaxCount 每天一个账号的最大发送次数
	 * @return 通用返回结果
	 */
	RemoteModelResult<HashMap<String,String>> queryVerifyCodeApi(String mobile, String codePrefix, String countPrefix, Integer sMSMaxCount);

	/**
	 * 通用登录Api
	 * @param mobile 手机号
	 * @param password 密码
	 * @param accountType 账号类型
	 * @param tokenKeyPrefix redis中存储token的key前缀
	 * @return 通用返回结果
	 */
	RemoteModelResult<AccountDto> queryLoginApi(String mobile, String password,int accountType, String tokenKeyPrefix,int expireTime);

	/**
	 * 通用重置密码api
	 * @param accountName 用户名
	 * @param accountType 用户类型
	 * @param password 密码
	 * @param mobile 手机号码
	 * @return 通用返回结果
	 */
	RemoteModelResult resetPasswordApi(String accountName,int accountType, String password, String mobile);

	/**
	 * 通用修改密码
	 * @param account 账号信息
	 * @param password 新密码
	 * @return 通用返回结果
	 */
	RemoteModelResult modifyPasswordApi(Account account, String password);

	/**
	 * 查询是否可以注册物业公司
	 * @param accountId 账号Id
	 * @param mobile 手机号
	 * @return 通用返回结果
	 */
	RemoteModelResult selectRegisterWyCompany(String accountId,String mobile);

	/**
	 * 根据accountName和type更新companyId
	 * @param accountName 账号名称
	 * @param type 账号类型
	 * @param companyId 公司id
	 * @return 通用返回结果
	 */
	RemoteModelResult updateCompanyIdByAccountNameAndType(String accountName, int type, String companyId);

	/**
	 * 修改老用户的accountName和mobile变成手机号码
	 * @param accountId 账号id
	 * @param mobile 手机号
	 * @return 通用返回结果
	 */
	RemoteModelResult updateMobile(String accountId, String mobile,String OldMobile);

	RemoteModelResult getCompanyId(String mobile);



	RemoteModelResult<List<Account>> findByCondition(AccountExample condition);
}
