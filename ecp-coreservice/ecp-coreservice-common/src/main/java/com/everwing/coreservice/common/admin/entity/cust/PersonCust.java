package com.everwing.coreservice.common.admin.entity.cust;

import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelation;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @TODO 物业平台同步过来的个人客户数据
 * @author wsw
 *
 */
public class PersonCust implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 个人客户 **/
	private String custId;

	/** 客户编号 **/
	private String custCode;

	/** 姓名 **/
	private String name;

	/** 性别 **/
	private String sex;

	/** 籍贯 **/
	private String nativePlace;

	/** 户口所在地 **/
	private String census;


	/** 出生日期 **/
	private @DateTimeFormat(pattern="yyyy-MM-dd")
	Date birthday;

	/** 证件类型 **/
	private String cardType;

	/** 证件号码 **/
	private String cardNum;

	/** 家家客户端id **/
	private String jiajiaNum;

	/** 微信帐号 **/
	private String weixinNum;

	/** 工作单位 **/
	private String workUnits;

	/** 婚否 **/
	private Byte marrieState;

	/** 是否有企业联系 **/
	private Byte isEnterprise;

	/** 紧急联系人 **/
	private String urgentContactPerson;

	/** 紧急联系电话 **/
	private String urgentContactPhone;

	/** 上传图片 **/
	private String uploadImage;

	/** 电话号码 **/
	private String phoneNum;

	/** 电子邮件  **/
	private String email;

	/** 民族  **/
	private String nation;


	/** 备注  **/
	private String remark;


	/** 注册电话 **/
	private String registerPhone;


	/** 国籍 **/
	private  String national;

	/** 公司id **/
	private String companyId;

	/** 创建人 **/
	private  String  createId;

	/** 创建人姓名 **/
	private  String  createName;

	/** 修改人ID **/
	private  String  modifyId;

	/** 修改人姓名 **/
	private  String  modifyName;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	/** 创建时间 **/
	private Date createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	/** 修改时间 **/
	private  Date modifyTime;
	/** 上传资料实体	**/
	private List<Annex> annexs; //上传资料实体

	/** 关系人列表 **/
	private PersonCustRelation personCustRelation;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getCensus() {
		return census;
	}

	public void setCensus(String census) {
		this.census = census;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getJiajiaNum() {
		return jiajiaNum;
	}

	public void setJiajiaNum(String jiajiaNum) {
		this.jiajiaNum = jiajiaNum;
	}

	public String getWeixinNum() {
		return weixinNum;
	}

	public void setWeixinNum(String weixinNum) {
		this.weixinNum = weixinNum;
	}

	public String getWorkUnits() {
		return workUnits;
	}

	public void setWorkUnits(String workUnits) {
		this.workUnits = workUnits;
	}

	public Byte getMarrieState() {
		return marrieState;
	}

	public void setMarrieState(Byte marrieState) {
		this.marrieState = marrieState;
	}

	public Byte getIsEnterprise() {
		return isEnterprise;
	}

	public void setIsEnterprise(Byte isEnterprise) {
		this.isEnterprise = isEnterprise;
	}

	public String getUrgentContactPerson() {
		return urgentContactPerson;
	}

	public void setUrgentContactPerson(String urgentContactPerson) {
		this.urgentContactPerson = urgentContactPerson;
	}

	public String getUrgentContactPhone() {
		return urgentContactPhone;
	}

	public void setUrgentContactPhone(String urgentContactPhone) {
		this.urgentContactPhone = urgentContactPhone;
	}

	public String getUploadImage() {
		return uploadImage;
	}

	public void setUploadImage(String uploadImage) {
		this.uploadImage = uploadImage;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNational() {
		return national;
	}

	public void setNational(String national) {
		this.national = national;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRegisterPhone() {
		return registerPhone;
	}

	public void setRegisterPhone(String registerPhone) {
		this.registerPhone = registerPhone;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public PersonCustRelation getPersonCustRelation() {
		return personCustRelation;
	}

	public void setPersonCustRelation(PersonCustRelation personCustRelation) {
		this.personCustRelation = personCustRelation;
	}

	public List<Annex> getAnnexs() {
		return annexs;
	}

	public void setAnnexs(List<Annex> annexs) {
		this.annexs = annexs;
	}

	@Override
	public String toString() {
		return "PersonCustNew{" +
				"custId='" + custId + '\'' +
				", custCode='" + custCode + '\'' +
				", name='" + name + '\'' +
				", sex='" + sex + '\'' +
				", nativePlace='" + nativePlace + '\'' +
				", census='" + census + '\'' +
				", birthday=" + birthday +
				", cardType='" + cardType + '\'' +
				", cardNum='" + cardNum + '\'' +
				", jiajiaNum='" + jiajiaNum + '\'' +
				", weixinNum='" + weixinNum + '\'' +
				", workUnits='" + workUnits + '\'' +
				", marrieState=" + marrieState +
				", isEnterprise=" + isEnterprise +
				", urgentContactPerson='" + urgentContactPerson + '\'' +
				", urgentContactPhone='" + urgentContactPhone + '\'' +
				", uploadImage='" + uploadImage + '\'' +
				", phoneNum='" + phoneNum + '\'' +
				", email='" + email + '\'' +
				", nation='" + nation + '\'' +
				", remark='" + remark + '\'' +
				", registerPhone='" + registerPhone + '\'' +
				", national='" + national + '\'' +
				", companyId='" + companyId + '\'' +
				", createId='" + createId + '\'' +
				", createName='" + createName + '\'' +
				", modifyId='" + modifyId + '\'' +
				", modifyName='" + modifyName + '\'' +
				", createTime=" + createTime +
				", modifyTime=" + modifyTime +
				", annexs=" + annexs +
				", personCustRelation=" + personCustRelation +
				'}';
	}
}
