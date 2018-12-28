package com.everwing.coreservice.common.platform.entity.extra;

import java.io.Serializable;

public class AccountAndHouseData implements Serializable{

	private String objAccount;
	private Integer type;
	private String houseAccount;

	public AccountAndHouseData(String objAccount, Integer type, String houseAccount) {
		super();
		this.objAccount = objAccount;
		this.type = type;
		this.houseAccount = houseAccount;
	}

	public String getObjAccount() {
		return objAccount;
	}

	public void setObjAccount(String objAccount) {
		this.objAccount = objAccount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getHouseAccount() {
		return houseAccount;
	}

	public void setHouseAccount(String houseAccount) {
		this.houseAccount = houseAccount;
	}

	@Override
	public String toString() {
		return "AccountAndHouseData [objAccount=" + objAccount + ", type=" + type
				+ ", houseAccount=" + houseAccount + "]";
	}
}
