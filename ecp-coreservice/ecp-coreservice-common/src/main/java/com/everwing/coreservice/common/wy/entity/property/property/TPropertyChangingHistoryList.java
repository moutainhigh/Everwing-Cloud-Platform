package com.everwing.coreservice.common.wy.entity.property.property;/**
 * Created by wust on 2017/8/9.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/9
 * @author wusongti@lii.com.cn
 */
public class TPropertyChangingHistoryList extends TPropertyChangingHistory{

    private static final long serialVersionUID = 6414188098023367566L;

    private String buildingFullName;

    private String buildingTypeName;

    private String propertyCode;

    private String projectId;
    
    /**
     * 附件上传ID
     */
    private String uploadFileId;
    
    /**
     * 附件名
     */
    private String annexName;
    
    
    

    public String getUploadFileId() {
		return uploadFileId;
	}

	public void setUploadFileId(String uploadFileId) {
		this.uploadFileId = uploadFileId;
	}

	public String getAnnexName() {
		return annexName;
	}

	public void setAnnexName(String annexName) {
		this.annexName = annexName;
	}

	public String getBuildingFullName() {
        return this.buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

    public String getBuildingTypeName() {
        return this.buildingTypeName;
    }

    public void setBuildingTypeName(String buildingTypeName) {
        this.buildingTypeName = buildingTypeName;
    }

    public String getPropertyCode() {
        return this.propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
