package com.everwing.coreservice.common.wy.entity.personbuilding;

import com.everwing.coreservice.common.BaseEntity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="SchedulingBuildingRecord")
public class SchedulingBuildingRecord extends BaseEntity {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5253448095875536512L;

	private String schedulingJobId;

    private String buildingType;

    private String id;
    
    private List<SchedulingBuildingRecord> sbrList;

    public List<SchedulingBuildingRecord> getSbrList() {
		return sbrList;
	}

	public void setSbrList(List<SchedulingBuildingRecord> sbrList) {
		this.sbrList = sbrList;
	}

	public String getSchedulingJobId() {
		return schedulingJobId;
	}

	public void setSchedulingJobId(String schedulingJobId) {
		this.schedulingJobId = schedulingJobId;
	}

	public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}