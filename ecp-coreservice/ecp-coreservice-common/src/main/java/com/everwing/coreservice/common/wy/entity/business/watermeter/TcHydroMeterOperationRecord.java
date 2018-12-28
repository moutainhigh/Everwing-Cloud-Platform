package com.everwing.coreservice.common.wy.entity.business.watermeter;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @describe 水电表操作信息系记录表
 *           记录包括管理员对水电表的启用，禁用，以及更换的操作记录
 * @author QHC
 * @date 2017-05-04
 *
 */
public class TcHydroMeterOperationRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 主键id **/
	private String id ;
	
	/** 操作前表编号 **/
	private String replaceBeforeCode;
	
	/** 操作后表编号 **/
	private String replaceAfterCode;
	
	/** 表类型 0:水表,1:点表 **/
	private Integer meterType;
	
	/** 操作前读数 **/
	private Double readingBefore;
	
	/** 更换水电表时，旧表已经使用的水电量 **/
	private Double usedAmount;
	
	/** 操作后读数 **/
	private Double readingAfter;
	
	/** 操作类型  0:更换,1:启用,2:停用  **/
	private Integer operationType;
	
	/** 操作原因 **/
	private String operationReason;
	
	/** 操作人 **/
	private String operationUser;
	
	/**变更前平值读数**/
	private Double fairValueBefore;
	
	/**变更前峰值读数**/
	private Double peakValueBefore;
	
	/** 变更前谷值读数**/
	private Double valleyValueBefore;
	
	/** 变更后平值读数**/
	private Double fairValueAfter;
	
	/**变更后峰值读数**/
	private Double peakValueAfter;
	
	/**变更后谷值读数**/
	private Double valleyValueAfter;
	
	/**换表前的峰值用量**/
	private Double usedPeakAmount;
	
	/**换表前的谷值用量**/
	private Double usedValleyAmount;
	
	/**换表前的平值用量**/
	private Double usedFairAmount;
	
	
	public Double getUsedPeakAmount() {
		return usedPeakAmount;
	}

	public void setUsedPeakAmount(Double usedPeakAmount) {
		this.usedPeakAmount = usedPeakAmount;
	}

	public Double getUsedValleyAmount() {
		return usedValleyAmount;
	}

	public void setUsedValleyAmount(Double usedValleyAmount) {
		this.usedValleyAmount = usedValleyAmount;
	}

	public Double getUsedFairAmount() {
		return usedFairAmount;
	}

	public void setUsedFairAmount(Double usedFairAmount) {
		this.usedFairAmount = usedFairAmount;
	}

	public Double getFairValueBefore() {
		return fairValueBefore;
	}

	public void setFairValueBefore(Double fairValueBefore) {
		this.fairValueBefore = fairValueBefore;
	}

	public Double getPeakValueBefore() {
		return peakValueBefore;
	}

	public void setPeakValueBefore(Double peakValueBefore) {
		this.peakValueBefore = peakValueBefore;
	}

	public Double getValleyValueBefore() {
		return valleyValueBefore;
	}

	public void setValleyValueBefore(Double valleyValueBefore) {
		this.valleyValueBefore = valleyValueBefore;
	}

	public Double getFairValueAfter() {
		return fairValueAfter;
	}

	public void setFairValueAfter(Double fairValueAfter) {
		this.fairValueAfter = fairValueAfter;
	}

	public Double getPeakValueAfter() {
		return peakValueAfter;
	}

	public void setPeakValueAfter(Double peakValueAfter) {
		this.peakValueAfter = peakValueAfter;
	}

	public Double getValleyValueAfter() {
		return valleyValueAfter;
	}

	public void setValleyValueAfter(Double valleyValueAfter) {
		this.valleyValueAfter = valleyValueAfter;
	}
	/** 操作时间 **/
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date operationTime;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReplaceBeforeCode() {
		return replaceBeforeCode;
	}

	public void setReplaceBeforeCode(String replaceBeforeCode) {
		this.replaceBeforeCode = replaceBeforeCode;
	}

	public String getReplaceAfterCode() {
		return replaceAfterCode;
	}

	public void setReplaceAfterCode(String replaceAfterCode) {
		this.replaceAfterCode = replaceAfterCode;
	}

	public Object getMeterType() {
		return meterType;
	}

	public void setMeterType(Integer meterType) {
		this.meterType = meterType;
	}

	public Double getReadingBefore() {
		return readingBefore;
	}

	public void setReadingBefore(Double readingBefore) {
		this.readingBefore = readingBefore;
	}

	public Double getReadingAfter() {
		return readingAfter;
	}

	public void setReadingAfter(Double readingAfter) {
		this.readingAfter = readingAfter;
	}

	public Object getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public String getOperationReason() {
		return operationReason;
	}

	public void setOperationReason(String operationReason) {
		this.operationReason = operationReason;
	}

	public String getOperationUser() {
		return operationUser;
	}

	public void setOperationUser(String operationUser) {
		this.operationUser = operationUser;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}
	
	public Double getUsedAmount() {
		return usedAmount;
	}

	public void setUsedAmount(Double usedAmount) {
		this.usedAmount = usedAmount;
	}

	public String[] getField() {
		return new String[]{"id","replaceBeforeCode","replaceAfterCode","usedAmount","meterType","readingBefore","readingAfter",
				"operationType","operationReason","operationUser","operationTime"};
	}

	@Override
	public String toString() {
		return "TcHydroMeterOperationRecord [id=" + id + ", replaceBeforeCode="
				+ replaceBeforeCode + ", replaceAfterCode=" + replaceAfterCode
				+ ", meterType=" + meterType + ", readingBefore="
				+ readingBefore + ", usedAmount=" + usedAmount
				+ ", readingAfter=" + readingAfter + ", operationType="
				+ operationType + ", operationReason=" + operationReason
				+ ", operationUser=" + operationUser + ", fairValueBefore="
				+ fairValueBefore + ", peakValueBefore=" + peakValueBefore
				+ ", valleyValueBefore=" + valleyValueBefore
				+ ", fairValueAfter=" + fairValueAfter + ", peakValueAfter="
				+ peakValueAfter + ", valleyValueAfter=" + valleyValueAfter
				+ ", operationTime=" + operationTime + "]";
	}


	
}
