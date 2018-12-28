package com.everwing.coreservice.common.wy.entity.delivery;

import com.everwing.coreservice.common.Page;

import java.io.Serializable;
import java.util.Date;



/***
 * @describe 收账明细表javaBean
 * @author qhc
 * @ date 2017-08-31 
 */
public class TJgAccountReceivable implements Serializable{

	
	private static final long serialVersionUID = 7209170766143787260L;

	/**主键**/
	private String id;

	/**交易编号**/
	private String tradNo;
	
	/**交账人姓名**/
	private String payerName;
	
	/**1 现金 2 微信 3 银联 4 混合支付 5托收 6银行收款**/
	private String payedType;
	
	/**收账时间 **/
	private Date payedTime;
	
	/**微信收账金额 **/
	private String payWx;
	
	/**微信收账金额 **/
	private String alipay;
	
	/**现金收账金额**/
	private String payCash;

	/**银联收账金额**/
	private String payUnion;
	
	private String bankReceipts;
	
	/**收账状态**/
	private Integer status;
	
	/**接收人**/
	private String oprId;
	
	/**接收人姓名**/
	private String oprName;
	
	/**项目id**/
	private String projectId;
	
	/**项目名**/
	private String projectName;
	
	private String totalId;
	
	private String relationId;
	
	private Integer businessType;
	
	
	
	
	
	/**分页用**/
	private Page page;
	
	//增加两个字段，后面搜索查询使用
//	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private String startTime;
	public String getTotalId() {
		return totalId;
	}

	public void setTotalId(String totalId) {
		this.totalId = totalId;
	}
	
//	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private String endTime;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayedType() {
		return payedType;
	}

	public void setPayedType(String payedType) {
		this.payedType = payedType;
	}

	public Date getPayedTime() {
		return payedTime;
	}

	public void setPayedTime(Date payedTime) {
		this.payedTime = payedTime;
	}

	public String getPayWx() {
		return payWx;
	}

	public void setPayWx(String payWx) {
		this.payWx = payWx;
	}

	public String getPayCash() {
		return payCash;
	}

	public void setPayCash(String payCash) {
		this.payCash = payCash;
	}

	public String getPayUnion() {
		return payUnion;
	}

	public void setPayUnion(String payUnion) {
		this.payUnion = payUnion;
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

	public String getTradNo() {
		return tradNo;
	}

	public void setTradNo(String tradNo) {
		this.tradNo = tradNo;
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

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public String getBankReceipts() {
		return bankReceipts;
	}

	public void setBankReceipts(String bankReceipts) {
		this.bankReceipts = bankReceipts;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}


	
	
}
