package com.everwing.coreservice.common.dto;

import java.io.Serializable;

public class LoginRslt implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -676255894973083742L;

	private String accountID;
	
	private String companyID;
	
	private String lastLoginTime;

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Override
	public String toString() {
		return "LoginRslt [accountID=" + accountID + ", companyID=" + companyID
				+ ", lastLoginTime=" + lastLoginTime + "]";
	}
}
