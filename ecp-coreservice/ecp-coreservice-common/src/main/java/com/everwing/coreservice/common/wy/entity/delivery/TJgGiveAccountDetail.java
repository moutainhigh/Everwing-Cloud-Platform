package com.everwing.coreservice.common.wy.entity.delivery;

import com.everwing.coreservice.common.Page;

import java.io.Serializable;
import java.util.Date;




/***
 * @describe 银账交割交账明细表javaBean
 * @author qhc
 * @ date 2017-08-31 
 */
public class TJgGiveAccountDetail implements Serializable{

	
	private static final long serialVersionUID = -3374442560868883333L;
	
	/**主键**/
	private String id;
	
	/**批次号**/
	private String oprNum;
	
	/**交账金额**/
	private Double oprAmount;
	
	/**交账来源  1 系统 2 其他**/
	private Integer type;
	
	/**状态 1 待确认 2 退回 3 已确认 **/
	private Integer status;
	
	/**操作人id **/
	private String oprId;
	
	/**操作人姓名**/
	private String oprName;

	/**接收人id**/
	private String receiveId;
	
	/**接收人姓名**/
	private String receiveName;
	
	/**总结算表id**/
	private String totalId;
	
	/**项目id**/
	private String projectId;
	
	/**项目名**/
	private String projectName;
	
	/**下期总结算id**/
	private String nextTotalId;

	/**交账开始时间**/
	private Date oprStartTime;

	/**交账结束时间**/
	private Date oprEndTime;

	/**交账方式 1 现金 2 其他**/
	private Integer oprType;

	/**备注**/
	private String remark;

	/**分页用**/
	private Page page;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOprNum() {
		return oprNum;
	}

	public void setOprNum(String oprNum) {
		this.oprNum = oprNum;
	}

	public Double getOprAmount() {
		return oprAmount;
	}

	public void setOprAmount(Double oprAmount) {
		this.oprAmount = oprAmount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getTotalId() {
		return totalId;
	}

	public void setTotalId(String totalId) {
		this.totalId = totalId;
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

	public String getNextTotalId() {
		return nextTotalId;
	}

	public void setNextTotalId(String nextTotalId) {
		this.nextTotalId = nextTotalId;
	}

	public Date getOprStartTime() {
		return oprStartTime;
	}

	public void setOprStartTime(Date oprStartTime) {
		this.oprStartTime = oprStartTime;
	}

	public Date getOprEndTime() {
		return oprEndTime;
	}

	public void setOprEndTime(Date oprEndTime) {
		this.oprEndTime = oprEndTime;
	}

	public Integer getOprType() {
		return oprType;
	}

	public void setOprType(Integer oprType) {
		this.oprType = oprType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	
	
	
}
