package com.everwing.coreservice.common.wy.entity.property.property;/**
 * Created by wust on 2017/8/14.
 */

import com.everwing.coreservice.common.Page;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/14
 * @author wusongti@lii.com.cn
 */
public class CustomerSearch implements java.io.Serializable{
    private static final long serialVersionUID = 2846771087598203468L;
    private Page page;
    private String name;		// 客户名称
    private String projectCode;	// 项目编码
	private String customerType;// 客户类型
    /*入口查询 START*/
    private String personName;//个人客户姓名
    private String enterpriseName;//企业客户姓名
    private String registerPhone;//个人或企业客户注册电话
    private String cardNum;//个人客户身份证号或企业客户注册号码
    private String buildingAddress;//
    private String houseNum;//房号
    private List<String> custIds;//
    /*入口查询 END*/


	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getRegisterPhone() {
		return registerPhone;
	}

	public void setRegisterPhone(String registerPhone) {
		this.registerPhone = registerPhone;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getBuildingAddress() {
		return buildingAddress;
	}

	public void setBuildingAddress(String buildingAddress) {
		this.buildingAddress = buildingAddress;
	}

	public List<String> getCustIds() {
		return custIds;
	}

	public void setCustIds(List<String> custIds) {
		this.custIds = custIds;
	}

	public String getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
	}
}
