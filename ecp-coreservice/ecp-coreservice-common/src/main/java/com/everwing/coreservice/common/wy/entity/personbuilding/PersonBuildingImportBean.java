package com.everwing.coreservice.common.wy.entity.personbuilding;

import java.io.Serializable;

public class PersonBuildingImportBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3701973598520782855L;
	
	private String fullName;
	private String nodeName;
	private String houseCode;
	private String custName;
	private String cardnum;
	private String isEc;
	
	// 是否成功，必须要加该字段
	private Boolean successFlag;
	
	// 错误原因，必须要加该字段
	private String errorMessage;
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	public String getIsEc() {
		return isEc;
	}
	public void setIsEc(String isEc) {
		this.isEc = isEc;
	}
	public Boolean getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(Boolean successFlag) {
		this.successFlag = successFlag;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getHouseCode() {
		return houseCode;
	}
	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}
	@Override
	public String toString() {
		return "PersonBuildingImportBean [fullName=" + fullName + ", nodeName="
				+ nodeName + ", custName=" + custName + ", cardnum=" + cardnum
				+ ", isEc=" + isEc + "]";
	}
	
	

}
