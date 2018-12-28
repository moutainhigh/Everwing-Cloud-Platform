package com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream;

import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;

import java.io.Serializable;
import java.util.Date;

public class TBsAssetAccountStream implements Serializable{

	private static final long serialVersionUID = 5414191259417849246L;
	
	/**主键**/
	private String id;
	
	/**父ID**/
	private String parentId;
	
	/**变化金额**/
	private Double changMoney;
	
	/**业务发生时间**/
	private Date occurrenceTime;
	
	/**创建人编码**/
	private String createId;
	
	/**创建人名称**/
	private String createName;
	
	//辅助查询字段
	private String buildingCode;
	
	private String type;
	
	private Date startTime;
	
	private Date endTime;
	
	private String purpose;
	
	//分页字段
	private Page page;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Double getChangMoney() {
		return changMoney;
	}

	public void setChangMoney(Double changMoney) {
		this.changMoney = changMoney;
	}

	public Date getOccurrenceTime() {
		return occurrenceTime;
	}

	public void setOccurrenceTime(Date occurrenceTime) {
		this.occurrenceTime = occurrenceTime;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}


	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public TBsAssetAccountStream() {
		super();
	}

	public TBsAssetAccountStream(String id, String parentId, Double changMoney,
			Date occurrenceTime, String createId, String createName,String purpose) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.changMoney = changMoney;
		this.occurrenceTime = occurrenceTime;
		this.createId = createId;
		this.createName = createName;
		this.purpose = purpose;
	}
	
	public TBsAssetAccountStream(String accountId, double kfAmount,String purpose) {
		this.id = CommonUtils.getUUID();
		this.parentId = accountId;
		this.changMoney = kfAmount;
		this.occurrenceTime = new Date();
		this.createId = Constants.STR_AUTO_GENER;
		this.createName = Constants.STR_AUTO_GENER;
		this.purpose = purpose;
	}
	
}
