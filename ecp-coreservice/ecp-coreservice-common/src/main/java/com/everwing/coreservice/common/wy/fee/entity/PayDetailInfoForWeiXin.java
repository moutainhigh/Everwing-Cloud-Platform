package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 微信小程序调用新账户进行缴费操作实体类
 * @author QHC
 * @date 2018-08-18
 *
 */
public class PayDetailInfoForWeiXin implements Serializable {


	private static final long serialVersionUID = 7528559547268186114L;

	//公司id
	private String companyId;
	//项目id（code）
	private String projectId;
	//项目名称
	private String projectName;
	//新房号
	private String houseCodeNew;
	//物业管理费金额
	private BigDecimal wyAmount;
	//物业管理费违约金金额
	private BigDecimal wyLateFeeAmount;
	//本体金额
	private BigDecimal btAmount;
	//本体违约金金额
	private BigDecimal btLateFeeAmount;
	//水费金额
	private BigDecimal waterAmount;
	//水费违约金金额
	private BigDecimal waterLateFeeAmount;
	//电费金额
	private BigDecimal electAmount;
	//电费违约金金额
	private BigDecimal electLateFeeAmount;
	//操作明细id(老账户和新账户来自不同的表)
	private String operaId;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
	public String getHouseCodeNew() {
		return houseCodeNew;
	}
	public void setHouseCodeNew(String houseCodeNew) {
		this.houseCodeNew = houseCodeNew;
	}
	public BigDecimal getWyAmount() {
		return wyAmount;
	}
	public void setWyAmount(BigDecimal wyAmount) {
		this.wyAmount = wyAmount;
	}
	public BigDecimal getWyLateFeeAmount() {
		return wyLateFeeAmount;
	}
	public void setWyLateFeeAmount(BigDecimal wyLateFeeAmount) {
		this.wyLateFeeAmount = wyLateFeeAmount;
	}
	public BigDecimal getBtAmount() {
		return btAmount;
	}
	public void setBtAmount(BigDecimal btAmount) {
		this.btAmount = btAmount;
	}
	public BigDecimal getBtLateFeeAmount() {
		return btLateFeeAmount;
	}
	public void setBtLateFeeAmount(BigDecimal btLateFeeAmount) {
		this.btLateFeeAmount = btLateFeeAmount;
	}
	public BigDecimal getWaterAmount() {
		return waterAmount;
	}
	public void setWaterAmount(BigDecimal waterAmount) {
		this.waterAmount = waterAmount;
	}
	public BigDecimal getWaterLateFeeAmount() {
		return waterLateFeeAmount;
	}
	public void setWaterLateFeeAmount(BigDecimal waterLateFeeAmount) {
		this.waterLateFeeAmount = waterLateFeeAmount;
	}
	public BigDecimal getElectAmount() {
		return electAmount;
	}
	public void setElectAmount(BigDecimal electAmount) {
		this.electAmount = electAmount;
	}
	public BigDecimal getElectLateFeeAmount() {
		return electLateFeeAmount;
	}
	public void setElectLateFeeAmount(BigDecimal electLateFeeAmount) {
		this.electLateFeeAmount = electLateFeeAmount;
	}
	public String getOperaId() {
		return operaId;
	}
	public void setOperaId(String operaId) {
		this.operaId = operaId;
	}

	@Override
	public String toString() {
		return "PayDetailInfoForWeiXin{" +
				"companyId='" + companyId + '\'' +
				", projectId='" + projectId + '\'' +
				", projectName='" + projectName + '\'' +
				", houseCodeNew='" + houseCodeNew + '\'' +
				", wyAmount=" + wyAmount +
				", wyLateFeeAmount=" + wyLateFeeAmount +
				", btAmount=" + btAmount +
				", btLateFeeAmount=" + btLateFeeAmount +
				", waterAmount=" + waterAmount +
				", waterLateFeeAmount=" + waterLateFeeAmount +
				", electAmount=" + electAmount +
				", electLateFeeAmount=" + electLateFeeAmount +
				", operaId='" + operaId + '\'' +
				'}';
	}
}
