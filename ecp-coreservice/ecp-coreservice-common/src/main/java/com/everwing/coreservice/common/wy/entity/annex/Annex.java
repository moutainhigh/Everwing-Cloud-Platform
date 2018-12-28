package com.everwing.coreservice.common.wy.entity.annex;

import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * 附件表
 * @author shaozheng
 *	2015-7-20
 */
@XmlRootElement(name="Annex")
public class Annex implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3620771018607798805L;
	private String annexId;//附件Id
	private String relationId;//关联Id
	private Integer annexType;//附件类型	0.图片  1.文档  2.录音
	private String annexAddress;//附件地址
	private String annexName;//附件名称
	
	private String annexTime;//附件时间
	private String pactId;//合同id
	private String isMain;//是否是主辅图，0主图1辅图，关联产品表
	private String companyId;//公司id
	
	private String fileType; //附件格式
	
	private String projectId;//项目ID
	
	private String isFinish;//建筑初始化，数据迁移使用字段  0新数据/1迁移数据  王洲  2016.03.24
	
	private String operatorId;//操作人id
	
	private Integer isUsed; //是否有效,主要用于账单的重新生成
	
	private String md5;		//文件的md5码,用来做重复导入校验
	
	private String uploadFileId;  //对接fastDFS用于从fastDFS取文件的file_id，具有唯一性
	
	//分页
	private Page page;
	
	private List<String> annexIds; //附件编码列表
	
	
	public Integer getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}
	public List<String> getAnnexIds() {
		return annexIds;
	}
	public void setAnnexIds(List<String> annexIds) {
		this.annexIds = annexIds;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getPactId() {
		return pactId;
	}
	public void setPactId(String pactId) {
		this.pactId = pactId;
	}
	public String getAnnexId() {
		return annexId;
	}
	public void setAnnexId(String annexId) {
		this.annexId = annexId;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public Integer getAnnexType() {
		return annexType;
	}
	public void setAnnexType(Integer annexType) {
		this.annexType = annexType;
	}
	public String getAnnexAddress() {
		return annexAddress;
	}
	public void setAnnexAddress(String annexAddress) {
		this.annexAddress = annexAddress;
	}
	public String getAnnexName() {
		return annexName;
	}
	public void setAnnexName(String annexName) {
		this.annexName = annexName;
	}
	
	
	public String getAnnexTime() {
		return annexTime;
	}
	public void setAnnexTime(String annexTime) {
		this.annexTime = annexTime;
	}
	public String getIsMain() {
		return isMain;
	}
	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	
	public String getUploadFileId() {
		return uploadFileId;
	}
	public void setUploadFileId(String uploadFileId) {
		this.uploadFileId = uploadFileId;
	}
	@Override
	public String toString() {
		return "Annex [annexId=" + annexId + ", relationId=" + relationId
				+ ", annexType=" + annexType + ", annexAddress=" + annexAddress
				+ ", annexName=" + annexName + ", annexTime=" + annexTime
				+ ", pactId=" + pactId + ", isMain=" + isMain + ", companyId="
				+ companyId + ", fileType=" + fileType + ", projectId="
				+ projectId + ", isFinish=" + isFinish + ", operatorId="
				+ operatorId + ", isUsed=" + isUsed + ", md5=" + md5
				+ ", page=" + page + ", annexIds=" + annexIds + "]";
	}
	
	public Annex(){}
	
	public Annex(UploadFile file , Integer fileType , String relationId, String projectId){
		this.annexId = file.getUploadFileId();
		this.annexName = file.getFileName();
		this.annexType = fileType;
		this.annexTime = CommonUtils.getDateStr();
		this.fileType = file.getSuffix().replaceAll(".", "");
		this.isUsed = 0;
		this.md5 = file.getMd5();
		this.relationId = relationId;
		this.projectId = projectId;
		this.annexAddress = file.getPath();
	}
}
