package com.everwing.server.linphone.vo;

import java.io.Serializable;

public class ElcDataVo implements Serializable {
	 /**
	 * 根据房屋编号查出电表信息
	 */
	private static final long serialVersionUID = 1L;
	private String elcMeterName;
	 private String elcMeterCode;
	 private String companyId;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getElcMeterName() {
		return elcMeterName;
	}
	public void setElcMeterName(String elcMeterName) {
		this.elcMeterName = elcMeterName;
	}
	public String getElcMeterCode() {
		return elcMeterCode;
	}
	public void setElcMeterCode(String elcMeterCode) {
		this.elcMeterCode = elcMeterCode;
	}
	@Override
	public String toString() {
		return "ElcDataVo [elcMeterName=" + elcMeterName + ", elcMeterCode="
				+ elcMeterCode + ", companyId=" + companyId + "]";
	}

	
	
	
}
