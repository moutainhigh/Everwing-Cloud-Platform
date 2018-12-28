package com.everwing.coreservice.common.wy.entity.business.electmeter;

import com.everwing.coreservice.common.BaseEntity;

public class ElectMeterImport extends BaseEntity{

	private static final long serialVersionUID = 8199733809923764310L;
	
	/** 电表编号**/
	private String code;
	/**最小读数**/
	private Double minamount;
	/**　位置**/
	private String location;
	/** 是否公用**/
	private Integer ispublic; 
	/**电表名称**/
	private String electricitymetername;
	/**最大读数**/
	private Double maxamount;
	/**资产编号**/
	private String assetno;
	/**循环使用 0 是, 1 不是**/
	private Integer iscycle;
	/**品牌**/
	private String brand; 
	/**倍率**/
	private float rate;
	/**规格型号**/
	private String specs;
	/**电表类型**/
	private Integer electricitymeterType;
	/**电表状态 0启用  1停用**/
	private Integer state; 
	/**供应商**/
	private String provider;
	/**初始读数**/
	private Double initamount;
	/**抄表方式 0 室内 , 1 室外 , 2 远程**/
	private Integer type;
	/**供应商电话**/
	private String providerphone;
	/**初始峰值**/
	private float initpeakvalue;
	/**初始谷值**/
	private float initvalleyvalue;
	/**初始平值**/
	private float initaveragevalue;
	/**父表编号**/
	private String parentcode;
	/**主表编号**/
	private String mastercode;
	/**使用性质 0 商用, 1 民用 , 2 管理处**/
	private Integer usertype;
	/**是否计费  0 是, 1 不是**/
	private Integer isbilling;
	/** 收费对象编码**/
	private String relationbuilding;
	/**收费对象名称**/
	private String relationbuildingName;
	/**电表位置名称**/
	private String locationName;
	/**安装人**/
	private String assembleperson;  //这里现在定传入工号
	/**安装人编号**/
	private String assemblepersonId;
	/** 是否成功，必须要加该字段 **/
    private Boolean successFlag;
    /**电表等级**/
    private Integer meterLevel;
    /** 项目编号**/
    private String projectId; 
    /** 项目名称**/
	private String projectName;

    /**错误原因，必须要加该字段**/
    private String errorMessage;
    /**峰值最大值**/
    private float maxPeakValue;
	
    /**谷值最大值**/
    private float maxValleyValue;
    
    /** 平值最大值**/
    private float maxAverageValue;

    private String companyId;

    private String projectCode;
    
    
	public float getMaxPeakValue() {
		return maxPeakValue;
	}
	public void setMaxPeakValue(float maxPeakValue) {
		this.maxPeakValue = maxPeakValue;
	}
	public float getMaxValleyValue() {
		return maxValleyValue;
	}
	public void setMaxValleyValue(float maxValleyValue) {
		this.maxValleyValue = maxValleyValue;
	}
	public float getMaxAverageValue() {
		return maxAverageValue;
	}
	public void setMaxAverageValue(float maxAverageValue) {
		this.maxAverageValue = maxAverageValue;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Integer getMeterLevel() {
		return meterLevel;
	}
	public void setMeterLevel(Integer meterLevel) {
		this.meterLevel = meterLevel;
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
	public String getRelationbuilding() {
		return relationbuilding;
	}
	public void setRelationbuilding(String relationbuilding) {
		this.relationbuilding = relationbuilding;
	}
	public String getRelationbuildingName() {
		return relationbuildingName;
	}
	public void setRelationbuildingName(String relationbuildingName) {
		this.relationbuildingName = relationbuildingName;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getAssembleperson() {
		return assembleperson;
	}
	public void setAssembleperson(String assembleperson) {
		this.assembleperson = assembleperson;
	}
	public String getAssemblepersonId() {
		return assemblepersonId;
	}
	public void setAssemblepersonId(String assemblepersonId) {
		this.assemblepersonId = assemblepersonId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Double getMinamount() {
		return minamount;
	}
	public void setMinamount(Double minamount) {
		this.minamount = minamount;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getIspublic() {
		return ispublic;
	}
	public void setIspublic(Integer ispublic) {
		this.ispublic = ispublic;
	}
	public String getElectricitymetername() {
		return electricitymetername;
	}
	public void setElectricitymetername(String electricitymetername) {
		this.electricitymetername = electricitymetername;
	}
	public Double getMaxamount() {
		return maxamount;
	}
	public void setMaxamount(Double maxamount) {
		this.maxamount = maxamount;
	}
	public String getAssetno() {
		return assetno;
	}
	public void setAssetno(String assetno) {
		this.assetno = assetno;
	}
	public Integer getIscycle() {
		return iscycle;
	}
	public void setIscycle(Integer iscycle) {
		this.iscycle = iscycle;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public Integer getElectricitymeterType() {
		return electricitymeterType;
	}
	public void setElectricitymeterType(Integer electricitymeterType) {
		this.electricitymeterType = electricitymeterType;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public Double getInitamount() {
		return initamount;
	}
	public void setInitamount(Double initamount) {
		this.initamount = initamount;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getProviderphone() {
		return providerphone;
	}
	public void setProviderphone(String providerphone) {
		this.providerphone = providerphone;
	}
	public float getInitpeakvalue() {
		return initpeakvalue;
	}
	public void setInitpeakvalue(float initpeakvalue) {
		this.initpeakvalue = initpeakvalue;
	}
	public float getInitvalleyvalue() {
		return initvalleyvalue;
	}
	public void setInitvalleyvalue(float initvalleyvalue) {
		this.initvalleyvalue = initvalleyvalue;
	}
	public float getInitaveragevalue() {
		return initaveragevalue;
	}
	public void setInitaveragevalue(float initaveragevalue) {
		this.initaveragevalue = initaveragevalue;
	}
	public String getParentcode() {
		return parentcode;
	}
	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}
	public String getMastercode() {
		return mastercode;
	}
	public void setMastercode(String mastercode) {
		this.mastercode = mastercode;
	}
	public Integer getUsertype() {
		return usertype;
	}
	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}
	public Integer getIsbilling() {
		return isbilling;
	}
	public void setIsbilling(Integer isbilling) {
		this.isbilling = isbilling;
	}


	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getProjectCode() {
		return this.projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
}
