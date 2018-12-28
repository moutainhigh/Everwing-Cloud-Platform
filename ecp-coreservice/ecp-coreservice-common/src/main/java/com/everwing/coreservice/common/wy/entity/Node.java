package com.everwing.coreservice.common.wy.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Node")
/** 
 * 节点类 
 */
public class Node {
	/**
	 * 节点编号
	 */
	private String id;
	/**
	 * 房屋全名
	 */
	private String fullName;
	/**
	 * 节点名称
	 */

	private String nodeName;
	/**
	 * 父节点编号
	 */
	private String parentId;
	/**
	 * 业主名称拼接字符串
	 */
	private String names;
	
	private String custType;	//户主类型
	
	private String buildingCode; //建筑Code

	/**
	 * id卡类型拼接字符串
	 */
	private String cardTypes;// 拼接卡类型
	/**
	 * id卡号拼接字符串
	 */
	private String cardNums;// 拼接卡号

	private String uuid;

	private String buildingType;// 建筑结构类型

	private String projectId;// 项目id

	private String buildingCertificate;// 房产证号

	private String buildingArea;// 建筑面积
	
	private String isParent;//是否为父节点
	
	private boolean hasChildren;//是否有子节点
	
	private String nodeLevel;//节点层级
	
	
	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getBuildingCertificate() {
		return buildingCertificate;
	}

	public void setBuildingCertificate(String buildingCertificate) {
		this.buildingCertificate = buildingCertificate;
	}

	public String getBuildingArea() {
		return buildingArea;
	}

	public void setBuildingArea(String buildingArea) {
		this.buildingArea = buildingArea;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	private List<Node> children = new ArrayList<Node>();

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getCardTypes() {
		return cardTypes;
	}

	public void setCardTypes(String cardTypes) {
		this.cardTypes = cardTypes;
	}

	public String getCardNums() {
		return cardNums;
	}

	public void setCardNums(String cardNums) {
		this.cardNums = cardNums;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public String getNodeLevel() {
		return nodeLevel;
	}

	public void setNodeLevel(String nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}
}