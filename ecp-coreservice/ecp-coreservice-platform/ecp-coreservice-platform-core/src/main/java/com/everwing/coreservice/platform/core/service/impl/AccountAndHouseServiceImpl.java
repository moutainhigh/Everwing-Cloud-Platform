package com.everwing.coreservice.platform.core.service.impl;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseData;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseSipData;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.AccountAndHouse;
import com.everwing.coreservice.common.platform.entity.generated.AccountAndHouseExample;
import com.everwing.coreservice.common.platform.entity.generated.AccountExample;
import com.everwing.coreservice.common.platform.service.AccountAndHouseService;
import com.everwing.coreservice.platform.core.util.Resources;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.everwing.coreservice.common.utils.CommonUtils.*;

@Service
public class AccountAndHouseServiceImpl extends Resources implements AccountAndHouseService {

	private Account selelctHouse(String houseAccount) {
		AccountExample example = new AccountExample();
		example.createCriteria().andAccountNameEqualTo(houseAccount)//
				.andTypeEqualTo(Dict.ACCOUNT_TYPE_HOUSE.getIntValue())//
				.andStateEqualTo(Dict.ACCOUNT_STATE_NORMAL.getIntValue());

		List<Account> houseList = accountMapper.selectByExample(example);
		if (houseList.isEmpty()) {
			throwECPException(ReturnCode.PF_ACCOUNT_NOT_EXISTS);
		}
		return houseList.get(0);
	}

	

	@Override
	public RemoteModelResult<Void> batchDelete(List<AccountAndHouseData> dataList) {
		isAnyNull(dataList);
		ArrayList<AccountAndHouse> aahList = new ArrayList<AccountAndHouse>();
		ArrayList<AccountAndHouseSipData> aashdList = new ArrayList<AccountAndHouseSipData>();// 用于推送给
		for (AccountAndHouseData data : dataList) {
			// 查出账号
			Account account = accountService.selectAccount(data.getObjAccount(), data.getType());
			
			// 查出房屋账号
			Account house = selelctHouse(data.getHouseAccount());

			if (Dict.ACCOUNT_TYPE_LY_USER.equals(account.getType())) {// 推送给sip的数据
				aashdList.add(new AccountAndHouseSipData(account.getAccountCode(),
						house.getAccountCode()));
			}

			AccountAndHouse accountAndHouse = new AccountAndHouse();
			accountAndHouse.setAccountId(account.getAccountId());
			accountAndHouse.setHouseId(house.getAccountId());
			aahList.add(accountAndHouse);
		}
		accountAndHouseExtraMapper.batchDelete(aahList);

		// 调用SIP接口
		sipApiService.unbind(aashdList);
		return returnSuccess();
	}

	@Override
	public RemoteModelResult<Void> batchAdd(List<AccountAndHouseData> dataList) {
		// 判断参数是否可用
		isAnyNull(dataList);
		ArrayList<AccountAndHouse> insertList = new ArrayList<AccountAndHouse>();
		ArrayList<AccountAndHouseSipData> sipPushList = new ArrayList<AccountAndHouseSipData>();
		for (AccountAndHouseData data : dataList) {
			// 查出账号
			Account account = accountService.selectAccount(data.getObjAccount(), data.getType());

			// 查出房屋账号
			Account house = selelctHouse(data.getHouseAccount());

			String accountId = account.getAccountId();
			String houseId = house.getAccountId();

			// 检查是否已绑定
			AccountAndHouseExample example = new AccountAndHouseExample();
			example.createCriteria()//
					.andAccountIdEqualTo(accountId)//
					.andHouseIdEqualTo(houseId);
			List<AccountAndHouse> accountAndHouseList = accountAndHouseMapper
					.selectByExample(example);

			// 如果不存在绑定关系
			if (accountAndHouseList.isEmpty()) {
				AccountAndHouse accountAndHouse = new AccountAndHouse();
				accountAndHouse.setCreateTime(new Date());
				accountAndHouse.setAccountId(accountId);
				accountAndHouse.setHouseId(houseId);
				insertList.add(accountAndHouse);// 插入数据

				if (Dict.ACCOUNT_TYPE_LY_USER.equals(account.getType())) {// 推送数据
					sipPushList.add(new AccountAndHouseSipData(account.getAccountCode(),
							house.getAccountCode()));
				}
			}
		}
		accountAndHouseExtraMapper.batchInsert(insertList);

		// 调用sip接口
		sipApiService.bind(sipPushList);

		return returnSuccess();
	}
	
	
	/*-@Override
	public RemoteModelResult<Void> add(String objAccount, Integer type, String houseAccount) {
		// 判断参数是否可用
		isAnyNull(objAccount, houseAccount, type);

		// 查出账号
		Account account = accountService.selectAccount(objAccount, type);

		// 查出房屋账号
		Account house = selelctHouse(houseAccount);

		Long accountId = account.getAccountId();
		Long houseId = house.getAccountId();

		// 检查是否已绑定
		AccountAndHouseExample example = new AccountAndHouseExample();
		example.createCriteria().andAccountIdEqualTo(accountId)//
				.andHouseIdEqualTo(houseId);
		List<AccountAndHouse> accountAndHouseList = accountAndHouseMapper.selectByExample(example);

		// 如果不存在关系，插入绑定数据
		if (accountAndHouseList.isEmpty()) {
			AccountAndHouse accountAndHouse = new AccountAndHouse();
			accountAndHouse.setAccountId(accountId);
			accountAndHouse.setHouseId(houseId);
			accountAndHouseMapper.insert(accountAndHouse);
		}

		// 无论如何，都调用sip接口
		if (Dict.ACCOUNT_TYPE_LY_USER.equals(account.getType())) {
			// sipApiService.bind(account.getAccountCode(),
			// house.getAccountCode());
		}

		if (!accountAndHouseList.isEmpty()) {// 即使存在也推送到SIP
			throw new ECPBusinessException(ReturnCode.PF_BIND_EXITED);
		} else {
			return returnSuccess();
		}
	}

	@Override
	public RemoteModelResult<Void> delete(String objAccount, Integer type, String houseAccount) {
		// 判断参数是否可用
		isAnyNull(objAccount, houseAccount, type);

		// 查出账号
		Account account = accountService.selectAccount(objAccount, type);

		// 查出房屋账号
		Account house = selelctHouse(houseAccount);

		AccountAndHouse accountAndHouse = new AccountAndHouse();
		accountAndHouse.setAccountId(account.getAccountId());
		accountAndHouse.setHouseId(house.getAccountId());
		accountAndHouseExtraMapper.delete(accountAndHouse);

		// 调用SIP接口
		if (Dict.ACCOUNT_TYPE_LY_USER.equals(account.getType())) {
			// sipApiService.unbind(account.getAccountCode(),
			// house.getAccountCode());
		}
		return returnSuccess();
	}*/
}