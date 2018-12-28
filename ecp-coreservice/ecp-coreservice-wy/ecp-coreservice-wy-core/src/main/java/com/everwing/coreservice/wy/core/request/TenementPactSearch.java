package com.everwing.coreservice.wy.core.request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TenementPactSearch") 
public class TenementPactSearch {
	private String tenementPactName;//物业合同名称
	
	private String staTime;//开始时间
	
	private String endTime;//结束时间
	
	private Boolean loseEfficacy;//已失效
	
	private Boolean aboutOfLoseEfficacy;//即将失效
	
	private Boolean crp;//执行中
	
	private String project;//合同项目

	public Boolean getCrp() {
		return crp;
	}

	public void setCrp(Boolean crp) {
		this.crp = crp;
	}

	public String getTenementPactName() {
		return tenementPactName;
	}

	public void setTenementPactName(String tenementPactName) {
		this.tenementPactName = tenementPactName;
	}

	public String getStaTime() {
		return staTime;
	}

	public void setStaTime(String staTime) {
		this.staTime = staTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Boolean getLoseEfficacy() {
		return loseEfficacy;
	}

	public void setLoseEfficacy(Boolean loseEfficacy) {
		this.loseEfficacy = loseEfficacy;
	}

	public Boolean getAboutOfLoseEfficacy() {
		return aboutOfLoseEfficacy;
	}

	public void setAboutOfLoseEfficacy(Boolean aboutOfLoseEfficacy) {
		this.aboutOfLoseEfficacy = aboutOfLoseEfficacy;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
	
	
}
