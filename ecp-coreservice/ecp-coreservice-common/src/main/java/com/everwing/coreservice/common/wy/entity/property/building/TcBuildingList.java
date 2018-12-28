package com.everwing.coreservice.common.wy.entity.property.building;/**
 * Created by wust on 2017/4/17.
 */

/**
 *
 * Function:列表对象
 * Reason:
 * Date:2017-4-17 16:49:15
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TcBuildingList extends TcBuilding{
    private static final long serialVersionUID = -6850322395107450612L;
    private String buildingTypeName;
    private String projectName;
    private String marketStateName;
	private String assetAttributesName;

    private String jzjgCode;
	private String jzjgName;
	private String qiCode;
	private String qiName;
	private String quCode;
	private String quName;
	private String dongzuoCode;
	private String dongzuoName;
	private String danyuanrukouCode;
	private String danyuanrukouName;
	private String cengCode;
	private String cengName;

    private Double totalArrears;//总欠费
    private Double arrears;  //总欠费


	public String getBuildingTypeName() {
		return buildingTypeName;
	}

	public void setBuildingTypeName(String buildingTypeName) {
		this.buildingTypeName = buildingTypeName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getMarketStateName() {
		return marketStateName;
	}

	public void setMarketStateName(String marketStateName) {
		this.marketStateName = marketStateName;
	}

	public String getAssetAttributesName() {
		return assetAttributesName;
	}

	public void setAssetAttributesName(String assetAttributesName) {
		this.assetAttributesName = assetAttributesName;
	}

	public String getJzjgCode() {
		return jzjgCode;
	}

	public void setJzjgCode(String jzjgCode) {
		this.jzjgCode = jzjgCode;
	}

	public String getJzjgName() {
		return jzjgName;
	}

	public void setJzjgName(String jzjgName) {
		this.jzjgName = jzjgName;
	}

	public String getQiCode() {
		return qiCode;
	}

	public void setQiCode(String qiCode) {
		this.qiCode = qiCode;
	}

	public String getQiName() {
		return qiName;
	}

	public void setQiName(String qiName) {
		this.qiName = qiName;
	}

	public String getQuCode() {
		return quCode;
	}

	public void setQuCode(String quCode) {
		this.quCode = quCode;
	}

	public String getQuName() {
		return quName;
	}

	public void setQuName(String quName) {
		this.quName = quName;
	}

	public String getDongzuoCode() {
		return dongzuoCode;
	}

	public void setDongzuoCode(String dongzuoCode) {
		this.dongzuoCode = dongzuoCode;
	}

	public String getDongzuoName() {
		return dongzuoName;
	}

	public void setDongzuoName(String dongzuoName) {
		this.dongzuoName = dongzuoName;
	}

	public String getDanyuanrukouCode() {
		return danyuanrukouCode;
	}

	public void setDanyuanrukouCode(String danyuanrukouCode) {
		this.danyuanrukouCode = danyuanrukouCode;
	}

	public String getDanyuanrukouName() {
		return danyuanrukouName;
	}

	public void setDanyuanrukouName(String danyuanrukouName) {
		this.danyuanrukouName = danyuanrukouName;
	}

	public String getCengCode() {
		return cengCode;
	}

	public void setCengCode(String cengCode) {
		this.cengCode = cengCode;
	}

	public String getCengName() {
		return cengName;
	}

	public void setCengName(String cengName) {
		this.cengName = cengName;
	}

	public Double getTotalArrears() {
		return totalArrears;
	}

	public void setTotalArrears(Double totalArrears) {
		this.totalArrears = totalArrears;
	}

	public Double getArrears() {
		return arrears;
	}

	public void setArrears(Double arrears) {
		this.arrears = arrears;
	}

	@Override
	public String toString() {
		return super.toString() + "TcBuildingList{" +
				"buildingTypeName='" + buildingTypeName + '\'' +
				", projectName='" + projectName + '\'' +
				", marketStateName='" + marketStateName + '\'' +
				", assetAttributesName='" + assetAttributesName + '\'' +
				", jzjgCode='" + jzjgCode + '\'' +
				", jzjgName='" + jzjgName + '\'' +
				", qiCode='" + qiCode + '\'' +
				", qiName='" + qiName + '\'' +
				", quCode='" + quCode + '\'' +
				", quName='" + quName + '\'' +
				", dongzuoCode='" + dongzuoCode + '\'' +
				", dongzuoName='" + dongzuoName + '\'' +
				", danyuanrukouCode='" + danyuanrukouCode + '\'' +
				", danyuanrukouName='" + danyuanrukouName + '\'' +
				", cengCode='" + cengCode + '\'' +
				", cengName='" + cengName + '\'' +
				", totalArrears=" + totalArrears +
				", arrears=" + arrears +
				'}';
	}
}
