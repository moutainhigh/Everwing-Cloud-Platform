package com.everwing.coreservice.common.wy.entity.delivery;

import com.everwing.coreservice.common.Page;

import java.io.Serializable;



/***
 * @describe 总结算表javaBean
 * @author qhc
 * @ date 2017-08-31 
 */
public class TJgTotalCalculation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2539964830541164154L;

	/**主键**/
	private String id;
	
	/**操作人**/
	private String oprId;
	
	/**操作人姓名**/
	private String oprName;
	
	/**状态 1 待确认 2 已确认 3已交账 **/
	private Integer status;
	
	/**现金总金额**/
	private Double cashTotal;
	
	/**现金已交金额**/
	private Double cashGaven;
	
	/**现金未交金额**/
	private Double cashNotGive;
	
	/**微信总金额 **/
	private Double wxTotal;
	
	/**微信已交金额 **/
	private Double wxGaven;
	
	/**微信未交金额**/
	private Double wxNotGive;
	
	/**支付宝总金额 **/
	private Double alipayTotal;
	
	/**支付宝已交金额 **/  
	private Double alipayGaven ;
	
	/**支付宝未交金额**/
	private Double alipayNotGive ;

	/**银联总金额**/
	private Double unionTotal;

	/**银联已交金额**/
	private Double unionGaven;
	
	/**银联未交金额**/
	private Double unionNotGive;
	
	/**银行收款**/
	private Double bankReceiptsTotal;
	
	/**银行收款已交金额**/
	private Double bankReceiptsGaven;
	
	/**银行收款未交金额**/
	private Double bankReceiptsNotGive;
	
	/**接收人 **/
	private String receiveId;
	
	/**接收人姓名 **/
	private String receiveName;
	
	/**创建时间**/
	private String createTime;

	/**项目id**/
	private String projectId;
	
	/**项目名**/
	private String projectName;
	
	/**总单id**/
	private String totalId;
	
	/**结束时间**/
	private String endTime;
	
	/**总单的交账 单号**/
	private String totalNum;

	/**分页用**/
	private Page page;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getCashTotal() {
		return cashTotal;
	}

	public void setCashTotal(Double cashTotal) {
		this.cashTotal = cashTotal;
	}

	public Double getCashGaven() {
		return cashGaven;
	}

	public void setCashGaven(Double cashGaven) {
		this.cashGaven = cashGaven;
	}

	public Double getCashNotGive() {
		return cashNotGive;
	}

	public void setCashNotGive(Double cashNotGive) {
		this.cashNotGive = cashNotGive;
	}

	public Double getWxTotal() {
		return wxTotal;
	}

	public void setWxTotal(Double wxTotal) {
		this.wxTotal = wxTotal;
	}

	public Double getWxGaven() {
		return wxGaven;
	}

	public void setWxGaven(Double wxGaven) {
		this.wxGaven = wxGaven;
	}

	public Double getWxNotGive() {
		return wxNotGive;
	}

	public void setWxNotGive(Double wxNotGive) {
		this.wxNotGive = wxNotGive;
	}

	public Double getUnionTotal() {
		return unionTotal;
	}

	public void setUnionTotal(Double unionTotal) {
		this.unionTotal = unionTotal;
	}

	public Double getUnionGaven() {
		return unionGaven;
	}

	public void setUnionGaven(Double unionGaven) {
		this.unionGaven = unionGaven;
	}

	public Double getUnionNotGive() {
		return unionNotGive;
	}

	public void setUnionNotGive(Double unionNotGive) {
		this.unionNotGive = unionNotGive;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getTotalId() {
		return totalId;
	}

	public void setTotalId(String totalId) {
		this.totalId = totalId;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public Double getBankReceiptsTotal() {
		return bankReceiptsTotal;
	}

	public void setBankReceiptsTotal(Double bankReceiptsTotal) {
		this.bankReceiptsTotal = bankReceiptsTotal;
	}

	public Double getBankReceiptsGaven() {
		return bankReceiptsGaven;
	}

	public void setBankReceiptsGaven(Double bankReceiptsGaven) {
		this.bankReceiptsGaven = bankReceiptsGaven;
	}

	public Double getBankReceiptsNotGive() {
		return bankReceiptsNotGive;
	}

	public void setBankReceiptsNotGive(Double bankReceiptsNotGive) {
		this.bankReceiptsNotGive = bankReceiptsNotGive;
	}

	public Double getAlipayTotal() {
		return alipayTotal;
	}

	public void setAlipayTotal(Double alipayTotal) {
		this.alipayTotal = alipayTotal;
	}

	public Double getAlipayGaven() {
		return alipayGaven;
	}

	public void setAlipayGaven(Double alipayGaven) {
		this.alipayGaven = alipayGaven;
	}

	public Double getAlipayNotGive() {
		return alipayNotGive;
	}

	public void setAlipayNotGive(Double alipayNotGive) {
		this.alipayNotGive = alipayNotGive;
	}

	
	
	
}
