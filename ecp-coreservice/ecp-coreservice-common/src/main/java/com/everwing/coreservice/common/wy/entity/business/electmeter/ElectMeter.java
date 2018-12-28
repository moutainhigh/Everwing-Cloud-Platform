package com.everwing.coreservice.common.wy.entity.business.electmeter;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ElectMeter implements Serializable{

	private static final long serialVersionUID = 5537041208774772962L;

	private String electmeterId; //ID
	private String code;//电表编号
	private Double minamount; //最小读数
	private Integer ispublic; //是否公用  0 是, 1 不是
	private String assembleperson; //安装人
	private String assemblepersonId;//安装人编号
	private String electricitymetername;//电表名称
	private Double maxamount; //最大读数
	private String assetno; //资产编号
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date assembletime; //安装日期
	private String location; //电表位置编码
	private String locationName; //电表位置名称
	private Integer iscycle; //循环使用 0 是, 1 不是
	private String brand; //品牌
	private String createid; //创建人
	private float rate; //倍率
	private String relationbuilding; //收费对象编码
	private String relationbuildingName;//收费对象名称
	private String specs; //规格型号
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createtime; //创建时间
	private Integer electricitymeterType; //电表类型
	private Integer state; //电表状态 0启用  1停用
	private String provider; //供应商
	private String modifyid; //修改人
	private Double initamount; //初始读数
	private Integer type; //抄表方式 0 室内 , 1 室外 , 2 远程
	private String providerphone; //供应商电话
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date modifytime; //修改时间
	private float initpeakvalue;//初始峰值
	/*最大峰值*/
	private float maxPeakValue;
	private float initvalleyvalue; //初始谷值
	/*最大谷值*/
	private float maxValleyValue;
	private float initaveragevalue;//初始平值
	/*最大平值*/
	private float maxAverageValue;
	private String parentcode; //父表编号
	private String mastercode; //主表编号
	private Integer usertype; //使用性质 0 商用, 1 民用 , 2 管理处
	private Integer isbilling; //是否计费  0 是, 1 不是
	private String companycode; //公司编码

	private String changereason;//变更原因

	private List<String> codes; //电表编号组

	private String enclosure; //附近名称

	private String enclosureurl; //附件地址

	private String projectId; //项目编号

	private String projectName; //项目名称

	/** 冗余一个查询参数，因为这里查询需要多匹配，不指定字段  **/
	private String queryParam;

	/**电表等级**/
	private Integer meterLevel;



	public Integer getMeterLevel() {
		return meterLevel;
	}

	public void setMeterLevel(Integer meterLevel) {
		this.meterLevel = meterLevel;
	}

	public float getMaxPeakValue() {
		return maxPeakValue;
	}

	public void setMaxPeakValue(float maxPeakValue) {
		this.maxPeakValue = maxPeakValue;
	}

	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
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

	public String getAssemblepersonId() {
		return assemblepersonId;
	}

	public void setAssemblepersonId(String assemblepersonId) {
		this.assemblepersonId = assemblepersonId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getRelationbuildingName() {
		return relationbuildingName;
	}

	public void setRelationbuildingName(String relationbuildingName) {
		this.relationbuildingName = relationbuildingName;
	}

	public ElectMeter() {
		super();
	}

	public ElectMeter(String code, String location, Integer state) {
		super();
		this.code = code;
		this.location = location;
		this.state = state;
	}

	public String getEnclosure() {
		return enclosure;
	}
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}
	public String getEnclosureurl() {
		return enclosureurl;
	}
	public void setEnclosureurl(String enclosureurl) {
		this.enclosureurl = enclosureurl;
	}
	public String getChangereason() {
		return changereason;
	}
	public void setChangereason(String changereason) {
		this.changereason = changereason;
	}
	public List<String> getCodes() {
		return codes;
	}
	public void setCodes(List<String> codes) {
		this.codes = codes;
	}
	public String getElectmeterId() {
		return electmeterId;
	}
	public void setElectmeterId(String electmeterId) {
		this.electmeterId = electmeterId;
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
	public Integer getIspublic() {
		return ispublic;
	}
	public void setIspublic(Integer ispublic) {
		this.ispublic = ispublic;
	}
	public String getAssembleperson() {
		return assembleperson;
	}
	public void setAssembleperson(String assembleperson) {
		this.assembleperson = assembleperson;
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
	public Date getAssembletime() {
		return assembletime;
	}
	public void setAssembletime(Date assembletime) {
		this.assembletime = assembletime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
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
	public String getCreateid() {
		return createid;
	}
	public void setCreateid(String createid) {
		this.createid = createid;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public String getRelationbuilding() {
		return relationbuilding;
	}
	public void setRelationbuilding(String relationbuilding) {
		this.relationbuilding = relationbuilding;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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
	public String getModifyid() {
		return modifyid;
	}
	public void setModifyid(String modifyid) {
		this.modifyid = modifyid;
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
	public Date getModifytime() {
		return modifytime;
	}
	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
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
	public String getCompanycode() {
		return companycode;
	}
	public void setCompanycode(String companycode) {
		this.companycode = companycode;
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



}
