package com.everwing.coreservice.common.wy.entity.cust.enterprise;
/**
 * 创建 by 肖聪   
 * 2015/6/25
 * 搜索公司和人员信息
 */

import com.everwing.coreservice.common.BaseEntity;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "SearchPersonAndEnterprise")
public class SearchPersonAndEnterprise extends BaseEntity {

	private static final long serialVersionUID = 1169936496040743146L;

	private String representative;//公司法人名称
	
	private String representativeId;//公司法人id
	
    private String custCode;//客户编号
    
    private String name;//客户姓名
    
    private String cardNum;//客户身份证
    
    private String enterpriseName;//公司名称 
    
//    private StallNew stallNew;
    
//    private HouseNew houseNew;
     
	private List<PersonCustNew> personCustNew;
    
//    private RegionNew regionNew;
    
	private List<EnterpriseCustNew> enterpriseCustNew;
    
    public List<EnterpriseCustNew> getEnterpriseCustNew() {
		return enterpriseCustNew;
	}
	public void setEnterpriseCustNew(List<EnterpriseCustNew> enterpriseCustNew) {
		this.enterpriseCustNew = enterpriseCustNew;
	}

    
    
	public List<PersonCustNew> getPersonCustNew() {
		return personCustNew;
	}
	public void setPersonCustNew(List<PersonCustNew> personCustNew) {
		this.personCustNew = personCustNew;
	}
    
    /*public StallNew getStallNew() {
		return stallNew;
	}
	public void setStallNew(StallNew stallNew) {
		this.stallNew = stallNew;
	}
	public HouseNew getHouseNew() {
		return houseNew;
	}
	public void setHouseNew(HouseNew houseNew) {
		this.houseNew = houseNew;
	}

	public RegionNew getRegionNew() {
		return regionNew;
	}
	public void setRegionNew(RegionNew regionNew) {
		this.regionNew = regionNew;
	}*/
	

	
    
	public String getRepresentative() {
		return representative;
	}
	public void setRepresentative(String representative) {
		this.representative = representative;
	}
	public String getRepresentativeId() {
		return representativeId;
	}
	public void setRepresentativeId(String representativeId) {
		this.representativeId = representativeId;
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
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
   
}
