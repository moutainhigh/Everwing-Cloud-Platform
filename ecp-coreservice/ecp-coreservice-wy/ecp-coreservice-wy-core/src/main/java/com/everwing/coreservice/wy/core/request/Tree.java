/**
 * @Title: Tree.java
 * @Package com.flf.request
 * @Description: TODO
 * Copyright: Copyright (c) 2011 
 * Company:武汉闻风多奇软件开发有限公司
 * 
 * @author wangtao
 * @date 2015-5-27 下午8:05:56
 * @version V1.0
 */

package com.everwing.coreservice.wy.core.request;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
/**
 * @ClassName: Tree
 * @Description: TODO
 * @author wangyang
 * @date 2015-5-27 下午8:05:56
 *
 */
@XmlRootElement(name = "Tree") 
public class Tree {
	private String id;
	private String pId;
	private String name;
	private Boolean open;
	private Boolean checked;
	private String types;
	private List<Tree> children;
	public List<Tree> getChildren() {
		return children;
	}
	public void setChildren(List<Tree> children) {
		this.children = children;
	}
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
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	
}
