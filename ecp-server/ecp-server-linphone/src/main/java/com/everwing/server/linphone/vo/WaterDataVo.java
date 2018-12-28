package com.everwing.server.linphone.vo;

import java.io.Serializable;

public class WaterDataVo  implements Serializable {
	 /**
	 * 根据房屋编号查出水表信息
	 */
	private static final long serialVersionUID = 1L;
	private String waterMeterName;
	 private String waterMeterCode;
	 private String companyId;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getWaterMeterName() {
		return waterMeterName;
	}
	public void setWaterMeterName(String waterMeterName) {
		this.waterMeterName = waterMeterName;
	}
	public String getWaterMeterCode() {
		return waterMeterCode;
	}
	public void setWaterMeterCode(String waterMeterCode) {
		this.waterMeterCode = waterMeterCode;
	}
	@Override
	public String toString() {
		return "WaterDataVo [waterMeterName=" + waterMeterName
				+ ", waterMeterCode=" + waterMeterCode + ", companyId="
				+ companyId + "]";
	}
	
	
}
