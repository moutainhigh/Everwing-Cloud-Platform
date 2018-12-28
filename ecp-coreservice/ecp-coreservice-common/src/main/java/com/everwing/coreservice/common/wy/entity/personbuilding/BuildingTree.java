package com.everwing.coreservice.common.wy.entity.personbuilding;

import com.everwing.coreservice.common.BaseEntity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="buildingTree")
public class BuildingTree extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8660931280977770512L;
	private String id;
	private String name;
	private String pId;
	private String checked;
	private String noChecked;
	private String buildingStructureId;
	public String getBuildingStructureId() {
		return buildingStructureId;
	}
	public void setBuildingStructureId(String buildingStructureId) {
		this.buildingStructureId = buildingStructureId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getNoChecked() {
		return noChecked;
	}
	public void setNoChecked(String noChecked) {
		this.noChecked = noChecked;
	}
	public BuildingTree() {
		super();
	}
	public BuildingTree(String id, String name, String pId, String buildingStructureId,String checked,
			String noChecked) {
		super();
		this.id = id;
		this.buildingStructureId = buildingStructureId;
		this.name = name;
		this.pId = pId;
		this.checked = checked;
		this.noChecked = noChecked;
	}
	
	
	
	
	
}
