package com.everwing.coreservice.wy.core.request;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * 组织结构树
 * @author shaozheng
 *	2015-7-29
 */
@XmlRootElement(name="OrgTree")
public class OrgTree {
	private String id;
	private String pId;
	private String name;
	private String leaderId;
	private String leaderName;
	private List<OrgTree> tList = new ArrayList<OrgTree>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	public String getLeaderName() {
		return leaderName;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	public List<OrgTree> gettList() {
		return tList;
	}
	public void settList(List<OrgTree> tList) {
		this.tList = tList;
	}
	
}
