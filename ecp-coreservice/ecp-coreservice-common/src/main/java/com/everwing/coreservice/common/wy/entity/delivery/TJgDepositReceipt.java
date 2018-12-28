package com.everwing.coreservice.common.wy.entity.delivery;

import com.everwing.coreservice.common.Page;

import java.io.Serializable;
import java.util.Date;



/***
 * @describe 存单表javaBean
 * @author qhc
 * @ date 2017-08-31 
 */
public class TJgDepositReceipt implements Serializable{


	private static final long serialVersionUID = 2016441652822750164L;

	/**主键**/
	private String id;
	
	/**总结算id**/
	private String totalId;
	
	/**存单号**/
	private String depositNum;
	
	/**存款金额**/
	private Double amount;
	
	/**创建人**/
	private String oprId;
	
	/**操作人姓名**/
	private String oprName;
	
	/**创建时间**/
	private Date createTime;
	

	/**项目id**/
	private String projectId;
	
	/**项目名**/
	private String projectName;
	
	
	/**分页查询用**/
	private Page page;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTotalId() {
		return totalId;
	}

	public void setTotalId(String totalId) {
		this.totalId = totalId;
	}

	public String getDepositNum() {
		return depositNum;
	}

	public void setDepositNum(String depositNum) {
		this.depositNum = depositNum;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getOprId() {
		return oprId;
	}

	public void setOprId(String oprId) {
		this.oprId = oprId;
	}

	public String getOprName() {
		return oprName;
	}

	public void setOprName(String oprName) {
		this.oprName = oprName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
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
	
	
}
