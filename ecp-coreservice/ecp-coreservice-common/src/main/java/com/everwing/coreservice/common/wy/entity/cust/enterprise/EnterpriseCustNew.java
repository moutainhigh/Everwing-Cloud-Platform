package com.everwing.coreservice.common.wy.entity.cust.enterprise;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelation;
import org.springframework.format.annotation.DateTimeFormat;

import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
@XmlRootElement(name = "EnterpriseCustNew") 
public class EnterpriseCustNew implements Serializable{

	private static final long serialVersionUID = -5888908978892207164L;

	/** UUID **/
	private String enterpriseId;

    /** 企业名称 **/
    private String enterpriseName;

    /** 地址 **/
    private String address;

    /** 法人代表 **/
    private String representative;

    /** 办公电话 **/
    private String officePhone;

    /** 传真 **/
    private String faxNumber;

    /** 企业网址 **/
    private String enterpriseUrl;

    /** 税务资质 **/
    private String taxCertificate;

    /** 单位属性 **/
    private String enterpriseProperty;

    /** 营业证书有效期 **/
    private @DateTimeFormat(pattern="yyyy-MM-dd")Date tradingDate;

    /** 营业执照号 **/
    private String tradingNumber; //用于存放营业执照文件在ts_annex中文件地址

    /** 税务登记证号 **/
    private String taxNumber;

    /** 税务证书有效期 **/
    private @DateTimeFormat(pattern="yyyy-MM-dd")Date taxDate;

    /** 经营类型 **/
    private String manageType;

    /** 企业编号 **/
    private String unitNumber;

    /** 电子邮件 **/
    private String email;

    /** 公司ID **/
	private String companyId;


    /** 创建时间 **/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

    /** 修改时间 **/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date modifyTime;

    /** 创建人ID **/
	private String createId;

    /** 修改人ID **/
	private String modifyId;

	/** 办公地址 **/
	private String businessAddress;

	/** 单位委托人 **/
    private String principal;

    /** 单位紧急联系人 **/
    private String emergencyContact;

    /** 单位紧急联系人号码 **/
    private String emergencyContactPhone;

    /** 组织机构代码 **/
    private String organizationCode; //用于存放组织机构代码文件在ts_annex中文件地址

    /** 注册时间 **/
    private @DateTimeFormat(pattern="yyyy-MM-dd")Date registerDate;//注册时间
    
    private PersonCustNew personCustNew; //企业法人对象

	private Page page;

    private List<Annex> annexs; //用于文件上传

    /** 关系人列表 **/
    private PersonCustRelation personCustRelation;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getEnterpriseUrl() {
        return enterpriseUrl;
    }

    public void setEnterpriseUrl(String enterpriseUrl) {
        this.enterpriseUrl = enterpriseUrl;
    }

    public String getTaxCertificate() {
        return taxCertificate;
    }

    public void setTaxCertificate(String taxCertificate) {
        this.taxCertificate = taxCertificate;
    }

    public String getEnterpriseProperty() {
        return enterpriseProperty;
    }

    public void setEnterpriseProperty(String enterpriseProperty) {
        this.enterpriseProperty = enterpriseProperty;
    }

    public Date getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(Date tradingDate) {
        this.tradingDate = tradingDate;
    }

    public String getTradingNumber() {
        return tradingNumber;
    }

    public void setTradingNumber(String tradingNumber) {
        this.tradingNumber = tradingNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public Date getTaxDate() {
        return taxDate;
    }

    public void setTaxDate(Date taxDate) {
        this.taxDate = taxDate;
    }

    public String getManageType() {
        return manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public PersonCustNew getPersonCustNew() {
        return personCustNew;
    }

    public void setPersonCustNew(PersonCustNew personCustNew) {
        this.personCustNew = personCustNew;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<Annex> getAnnexs() {
        return annexs;
    }

    public void setAnnexs(List<Annex> annexs) {
        this.annexs = annexs;
    }

    public PersonCustRelation getPersonCustRelation() {
        return personCustRelation;
    }

    public void setPersonCustRelation(PersonCustRelation personCustRelation) {
        this.personCustRelation = personCustRelation;
    }

    @Override
    public String toString() {
        return "EnterpriseCustNew{" +
                "enterpriseId='" + enterpriseId + '\'' +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", address='" + address + '\'' +
                ", representative='" + representative + '\'' +
                ", officePhone='" + officePhone + '\'' +
                ", faxNumber='" + faxNumber + '\'' +
                ", enterpriseUrl='" + enterpriseUrl + '\'' +
                ", taxCertificate='" + taxCertificate + '\'' +
                ", enterpriseProperty='" + enterpriseProperty + '\'' +
                ", tradingDate=" + tradingDate +
                ", tradingNumber='" + tradingNumber + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                ", taxDate=" + taxDate +
                ", manageType='" + manageType + '\'' +
                ", unitNumber='" + unitNumber + '\'' +
                ", email='" + email + '\'' +
                ", companyId='" + companyId + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", createId='" + createId + '\'' +
                ", modifyId='" + modifyId + '\'' +
                ", businessAddress='" + businessAddress + '\'' +
                ", principal='" + principal + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", emergencyContactPhone='" + emergencyContactPhone + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", registerDate=" + registerDate +
                ", personCustNew=" + personCustNew +
                ", page=" + page +
                ", annexs=" + annexs +
                ", personCustRelation=" + personCustRelation +
                '}';
    }
}