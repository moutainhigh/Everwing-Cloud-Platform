package com.everwing.coreservice.common.platform.entity.extra;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class AccountAndHouseSipData implements Serializable {
	public AccountAndHouseSipData(String accountCode, String houseCode) {
		super();
		this.accountCode = accountCode;
		this.houseCode = houseCode;
	}

	// format:{"binduser":[{"username":"房间内部账号","bindusername":"内部账号"}]}
	@JSONField(name = "bindusername")
	private String accountCode;
	
	@JSONField(name = "username")
	private String houseCode;

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getHouseCode() {
		return houseCode;
	}

	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}

	@Override
	public String toString() {
		return "AccountAndHouseSipData [accountCode=" + accountCode + ", houseCode=" + houseCode
				+ "]";
	}
}
