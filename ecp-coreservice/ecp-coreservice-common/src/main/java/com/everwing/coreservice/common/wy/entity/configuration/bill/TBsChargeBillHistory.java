package com.everwing.coreservice.common.wy.entity.configuration.bill;import com.everwing.coreservice.common.Page;import org.springframework.format.annotation.DateTimeFormat;import java.util.Date;/******************************************************************************* * javaBeans * t_bs_charge_bill_history --> TBsChargeBillHistory  * <table explanation> * @author 2017-07-05 15:45:23 *  */	public class TBsChargeBillHistory implements java.io.Serializable {	/**	 * 	 */	private static final long serialVersionUID = 1L;	//field	/** 主键 **/	private String id;	/** 本期总记录id **/	private String chargeTotalId;	/** 项目id **/	private String projectId;	/** 建筑code **/	private String buildingCode;	/** 建筑全名 **/	private String fullName;	/** 上期账单金额 **/	private Double lastBillFee;	/** 上期已付 **/	private Double lastPayed;	/** 本期产生金额 **/	private Double currentFee;	/** 违约金 **/	private Double lateFee;	/** 本清应付(本期账单) **/	private Double currentBillFee;	/** 账户余额 **/	private Double accountBalance;	/** 计费时间 **/	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	private Date billingTime;	/** 上期账单id **/	private String lastBillId;	/** 是否有效 **/	private Integer isUsed;	/** 创建人 **/	private String createId;	/** 创建时间 **/	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	private Date createTime;	/** 修改人 **/	private String modifyId;	/** 修改时间 **/	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	private Date modifyTime;	/** 分摊费用 **/	private Double shareFee;	/** 通用账户 抵扣金额   **/	private Double commonDesummoney;	/** 非通用账户抵扣金额    **/	private Double noCommonDesummoney;	/** 本期的扣取额  **/	private Double currentKqAmount;	/** 是否已经打包完成 **/	private Integer isZipComplete;		/** 税金  **/	private Double tax;	/**记录各个费用项计算值拼凑成json字符串**/	private String feeItemDetail;		/** 记录截止至上个周期的欠费记录json串, 以便于后期重新计费**/	private String lastOwedInfo;		/** 分页对象  **/	private Page page;		/** 模糊搜索条件  **/	private String searchCode;		private String searchTime;		/** 结果包含辅助字段 **/	private Integer type;		/**分单审核状态**/	private Integer aduitStatus;		private String temporaryBill;	//method		public String getId() {		return id;	}	public String getTemporaryBill() {		return temporaryBill;	}	public void setTemporaryBill(String temporaryBill) {		this.temporaryBill = temporaryBill;	}	public Integer getAduitStatus() {		return aduitStatus;	}	public void setAduitStatus(Integer aduitStatus) {		this.aduitStatus = aduitStatus;	}	public String getFeeItemDetail() {		return feeItemDetail;	}	public void setFeeItemDetail(String feeItemDetail) {		this.feeItemDetail = feeItemDetail;	}	public void setId(String id) {		this.id = id;	}	public String getChargeTotalId() {		return chargeTotalId;	}	public void setChargeTotalId(String chargeTotalId) {		this.chargeTotalId = chargeTotalId;	}	public String getProjectId() {		return projectId;	}	public void setProjectId(String projectId) {		this.projectId = projectId;	}	public String getBuildingCode() {		return buildingCode;	}	public void setBuildingCode(String buildingCode) {		this.buildingCode = buildingCode;	}	public String getFullName() {		return fullName;	}	public void setFullName(String fullName) {		this.fullName = fullName;	}	public Double getLastBillFee() {		return lastBillFee;	}	public void setLastBillFee(Double lastBillFee) {		this.lastBillFee = lastBillFee;	}	public Double getLastPayed() {		return lastPayed;	}	public void setLastPayed(Double lastPayed) {		this.lastPayed = lastPayed;	}	public Double getCurrentFee() {		return currentFee;	}	public void setCurrentFee(Double currentFee) {		this.currentFee = currentFee;	}	public Double getLateFee() {		return lateFee;	}	public void setLateFee(Double lateFee) {		this.lateFee = lateFee;	}	public Double getCurrentBillFee() {		return currentBillFee;	}	public void setCurrentBillFee(Double currentBillFee) {		this.currentBillFee = currentBillFee;	}	public Double getAccountBalance() {		return accountBalance;	}	public void setAccountBalance(Double accountBalance) {		this.accountBalance = accountBalance;	}	public Date getBillingTime() {		return billingTime;	}	public void setBillingTime(Date billingTime) {		this.billingTime = billingTime;	}	public String getLastBillId() {		return lastBillId;	}	public void setLastBillId(String lastBillId) {		this.lastBillId = lastBillId;	}	public Object getIsUsed() {		return isUsed;	}	public void setIsUsed(Integer isUsed) {		this.isUsed = isUsed;	}	public String getCreateId() {		return createId;	}	public void setCreateId(String createId) {		this.createId = createId;	}	public Date getCreateTime() {		return createTime;	}	public void setCreateTime(Date createTime) {		this.createTime = createTime;	}	public String getModifyId() {		return modifyId;	}	public void setModifyId(String modifyId) {		this.modifyId = modifyId;	}	public Date getModifyTime() {		return modifyTime;	}	public void setModifyTime(Date modifyTime) {		this.modifyTime = modifyTime;	}	public Page getPage() {		return page;	}	public void setPage(Page page) {		this.page = page;	}	public String getSearchCode() {		return searchCode;	}	public void setSearchCode(String searchCode) {		this.searchCode = searchCode;	}	public Double getShareFee() {		return shareFee;	}	public void setShareFee(Double shareFee) {		this.shareFee = shareFee;	}	public Double getCommonDesummoney() {		return commonDesummoney;	}	public void setCommonDesummoney(Double commonDesummoney) {		this.commonDesummoney = commonDesummoney;	}	public Double getNoCommonDesummoney() {		return noCommonDesummoney;	}	public void setNoCommonDesummoney(Double noCommonDesummoney) {		this.noCommonDesummoney = noCommonDesummoney;	}	public String getLastOwedInfo() {		return lastOwedInfo;	}	public void setLastOwedInfo(String lastOwedInfo) {		this.lastOwedInfo = lastOwedInfo;	}	public Double getCurrentKqAmount() {		return currentKqAmount;	}	public void setCurrentKqAmount(Double currentKqAmount) {		this.currentKqAmount = currentKqAmount;	}	public Integer getType() {		return type;	}	public void setType(Integer type) {		this.type = type;	}	public Integer getIsZipComplete() {		return isZipComplete;	}	public void setIsZipComplete(Integer isZipComplete) {		this.isZipComplete = isZipComplete;	}	public Double getTax() {		return tax;	}	public void setTax(Double tax) {		this.tax = tax;	}	public String getSearchTime() {		return searchTime;	}	public void setSearchTime(String searchTime) {		this.searchTime = searchTime;	}	@Override	public String toString() {		return "TBsChargeBillHistory [id=" + id + ", chargeTotalId="				+ chargeTotalId + ", projectId=" + projectId				+ ", buildingCode=" + buildingCode + ", fullName=" + fullName				+ ", lastBillFee=" + lastBillFee + ", lastPayed=" + lastPayed				+ ", currentFee=" + currentFee + ", lateFee=" + lateFee				+ ", currentBillFee=" + currentBillFee + ", accountBalance="				+ accountBalance + ", billingTime=" + billingTime				+ ", lastBillId=" + lastBillId + ", isUsed=" + isUsed				+ ", createId=" + createId + ", createTime=" + createTime				+ ", modifyId=" + modifyId + ", modifyTime=" + modifyTime				+ ", shareFee=" + shareFee + ", commonDesummoney="				+ commonDesummoney + ", noCommonDesummoney="				+ noCommonDesummoney + ", currentKqAmount=" + currentKqAmount				+ ", isZipComplete=" + isZipComplete + ", tax=" + tax				+ ", feeItemDetail=" + feeItemDetail + ", lastOwedInfo="				+ lastOwedInfo + ", page=" + page + ", searchCode="				+ searchCode + ", type=" + type + "]";	}	//return String[] filed; 	public String[] getField() {		return new String[]{"id","chargeTotalId","projectId","buildingCode","fullName","lastBillFee","lastPayed","currentFee","lateFee","currentBillFee","accountBalance","billingTime","lastBillId","isUsed","shareFee","commonDesummoney","noCommonDesummoney","lastOwedInfo","createId","createTime","modifyId","modifyTime"};	}}