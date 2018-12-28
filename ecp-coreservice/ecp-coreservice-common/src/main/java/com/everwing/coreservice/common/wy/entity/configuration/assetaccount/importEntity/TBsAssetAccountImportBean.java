package com.everwing.coreservice.common.wy.entity.configuration.assetaccount.importEntity;

import java.io.Serializable;

/**
 * 
 * 账户老数据导入辅助工具类
 *
 */
public class TBsAssetAccountImportBean implements Serializable{

	private static final long serialVersionUID = -5513101561593026058L;
	
	private String id;
	
	private String fullName;
	
	private Double wyAmount;
	
	private Double wyLateFee;
	
	private Double btAmount;
	
	private Double waterAmount;
	
	private Double electAmount;
	
	// 是否成功，必须要加该字段
	private Boolean successFlag;
	
	// 错误原因，必须要加该字段
	private String errorMessage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Double getWyAmount() {
		return wyAmount;
	}

	public void setWyAmount(Double wyAmount) {
		this.wyAmount = wyAmount;
	}

	public Double getWyLateFee() {
		return wyLateFee;
	}

	public void setWyLateFee(Double wyLateFee) {
		this.wyLateFee = wyLateFee;
	}

	public Double getBtAmount() {
		return btAmount;
	}

	public void setBtAmount(Double btAmount) {
		this.btAmount = btAmount;
	}

	public Double getWaterAmount() {
		return waterAmount;
	}

	public void setWaterAmount(Double waterAmount) {
		this.waterAmount = waterAmount;
	}

	public Double getElectAmount() {
		return electAmount;
	}

	public void setElectAmount(Double electAmount) {
		this.electAmount = electAmount;
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

	@Override
	public String toString() {
		return "AssetAccountImportBean [id=" + id + ", fullName=" + fullName
				+ ", wyAmount=" + wyAmount + ", wyLateFee=" + wyLateFee
				+ ", btAmount=" + btAmount + ", waterAmount=" + waterAmount
				+ ", electAmount=" + electAmount + "]";
	}
	
}
