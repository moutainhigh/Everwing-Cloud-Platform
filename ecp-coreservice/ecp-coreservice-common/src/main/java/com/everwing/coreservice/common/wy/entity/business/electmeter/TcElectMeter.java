package com.everwing.coreservice.common.wy.entity.business.electmeter;

import com.everwing.coreservice.common.wy.entity.annex.Annex;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TcElectMeter implements Serializable{

	private static final long serialVersionUID = 5537041208774772962L;
	/** 主键id **/
	private String id;
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
	private String  position; //电表位置编码
	private String positionName; //电表位置名称
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
	/** 上传资料实体	**/
	private List<Annex> annexs;
	private String[] dirtyinfo;
	/** 创建人姓名 **/
	private  String  createrName;
	/** 修改人姓名 **/
	private  String  modifyName;
	private String location;

	/** 是否校验表 **/
	private  String checkTable;

	/** 描述 **/
	private   String describeNum;

	/** 组表编码 **/
	private    String  groupTableCoding ;

	/** 组表名称 **/
	private  String groupTableName ;

	/** 是否分摊  0 是, 1 不是 **/
	private  Integer  isShare;

	/** 新编码 **/
	private String  newCoding;

	/** 外部编码 **/
	private String externalCoding;
	/** **/
	private String electCode;

	private String  electState;

	public String getElectCode() {
		return electCode;
	}

	public void setElectCode(String electCode) {
		this.electCode = electCode;
	}

	public String getElectState() {
		return electState;
	}

	public void setElectState(String electState) {
		electState = electState;
	}

	public String getCheckTable() {
		return checkTable;
	}

	public void setCheckTable(String checkTable) {
		this.checkTable = checkTable;
	}

	public String getDescribeNum() {
		return describeNum;
	}

	public void setDescribeNum(String describeNum) {
		this.describeNum = describeNum;
	}

	public String getGroupTableCoding() {
		return groupTableCoding;
	}

	public void setGroupTableCoding(String groupTableCoding) {
		this.groupTableCoding = groupTableCoding;
	}

	public String getGroupTableName() {
		return groupTableName;
	}

	public void setGroupTableName(String groupTableName) {
		this.groupTableName = groupTableName;
	}

	public Integer getIsShare() {
		return isShare;
	}

	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}

	public String getNewCoding() {
		return newCoding;
	}

	public void setNewCoding(String newCoding) {
		this.newCoding = newCoding;
	}

	public String getExternalCoding() {
		return externalCoding;
	}

	public void setExternalCoding(String externalCoding) {
		this.externalCoding = externalCoding;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Annex> getAnnexs() {
		return annexs;
	}

	public void setAnnexs(List<Annex> annexs) {
		this.annexs = annexs;
	}

	public String[] getDirtyinfo() {
		return dirtyinfo;
	}

	public void setDirtyinfo(String[] dirtyinfo) {
		this.dirtyinfo = dirtyinfo;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
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

	public String getAssemblepersonId() {
		return assemblepersonId;
	}

	public void setAssemblepersonId(String assemblepersonId) {
		this.assemblepersonId = assemblepersonId;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
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

	public String getRelationbuildingName() {
		return relationbuildingName;
	}

	public void setRelationbuildingName(String relationbuildingName) {
		this.relationbuildingName = relationbuildingName;
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

	public float getMaxPeakValue() {
		return maxPeakValue;
	}

	public void setMaxPeakValue(float maxPeakValue) {
		this.maxPeakValue = maxPeakValue;
	}

	public float getInitvalleyvalue() {
		return initvalleyvalue;
	}

	public void setInitvalleyvalue(float initvalleyvalue) {
		this.initvalleyvalue = initvalleyvalue;
	}

	public float getMaxValleyValue() {
		return maxValleyValue;
	}

	public void setMaxValleyValue(float maxValleyValue) {
		this.maxValleyValue = maxValleyValue;
	}

	public float getInitaveragevalue() {
		return initaveragevalue;
	}

	public void setInitaveragevalue(float initaveragevalue) {
		this.initaveragevalue = initaveragevalue;
	}

	public float getMaxAverageValue() {
		return maxAverageValue;
	}

	public void setMaxAverageValue(float maxAverageValue) {
		this.maxAverageValue = maxAverageValue;
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

	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

	public Integer getMeterLevel() {
		return meterLevel;
	}

	public void setMeterLevel(Integer meterLevel) {
		this.meterLevel = meterLevel;
	}

	@Override
	public String toString() {
		return "TcElectMeter{" +
				"id='" + id + '\'' +
				", electmeterId='" + electmeterId + '\'' +
				", code='" + code + '\'' +
				", minamount=" + minamount +
				", ispublic=" + ispublic +
				", assembleperson='" + assembleperson + '\'' +
				", assemblepersonId='" + assemblepersonId + '\'' +
				", electricitymetername='" + electricitymetername + '\'' +
				", maxamount=" + maxamount +
				", assetno='" + assetno + '\'' +
				", assembletime=" + assembletime +
				", position='" + position + '\'' +
				", positionName='" + positionName + '\'' +
				", iscycle=" + iscycle +
				", brand='" + brand + '\'' +
				", createid='" + createid + '\'' +
				", rate=" + rate +
				", relationbuilding='" + relationbuilding + '\'' +
				", relationbuildingName='" + relationbuildingName + '\'' +
				", specs='" + specs + '\'' +
				", createtime=" + createtime +
				", electricitymeterType=" + electricitymeterType +
				", state=" + state +
				", provider='" + provider + '\'' +
				", modifyid='" + modifyid + '\'' +
				", initamount=" + initamount +
				", type=" + type +
				", providerphone='" + providerphone + '\'' +
				", modifytime=" + modifytime +
				", initpeakvalue=" + initpeakvalue +
				", maxPeakValue=" + maxPeakValue +
				", initvalleyvalue=" + initvalleyvalue +
				", maxValleyValue=" + maxValleyValue +
				", initaveragevalue=" + initaveragevalue +
				", maxAverageValue=" + maxAverageValue +
				", parentcode='" + parentcode + '\'' +
				", mastercode='" + mastercode + '\'' +
				", usertype=" + usertype +
				", isbilling=" + isbilling +
				", companycode='" + companycode + '\'' +
				", changereason='" + changereason + '\'' +
				", codes=" + codes +
				", enclosure='" + enclosure + '\'' +
				", enclosureurl='" + enclosureurl + '\'' +
				", projectId='" + projectId + '\'' +
				", projectName='" + projectName + '\'' +
				", queryParam='" + queryParam + '\'' +
				", meterLevel=" + meterLevel +
				", annexs=" + annexs +
				", dirtyinfo=" + Arrays.toString(dirtyinfo) +
				", createrName='" + createrName + '\'' +
				", modifyName='" + modifyName + '\'' +
				", location='" + location + '\'' +
				", checkTable='" + checkTable + '\'' +
				", describeNum='" + describeNum + '\'' +
				", groupTableCoding='" + groupTableCoding + '\'' +
				", groupTableName='" + groupTableName + '\'' +
				", isShare=" + isShare +
				", newCoding='" + newCoding + '\'' +
				", externalCoding='" + externalCoding + '\'' +
				", electCode='" + electCode + '\'' +
				", electState='" + electState + '\'' +
				'}';
	}
}
