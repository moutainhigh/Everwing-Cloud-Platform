/**
 * @Title: Tree.java
 * @Package com.flf.entity
 * @Description: TODO
 * Copyright: Copyright (c) 2011 
 * Company:武汉闻风多奇软件开发有限公司
 * 
 * @author wangtao
 * @date 2015-5-11 下午7:13:09
 * @version V1.0
 */

package com.everwing.coreservice.common.wy.entity.personbuilding;

import com.everwing.coreservice.common.BaseEntity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @ClassName: Tree
 * @Description: TODO
 * @author wangyang
 * @date 2015-5-11 下午7:13:09
 *
 */
@XmlRootElement(name = "Tree") 
public class Tree extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8958058164790927407L;
	private String id;
	private String name;
	private List<Tree> children;
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
	public List<Tree> getChildren() {
		return children;
	}
	public void setChildren(List<Tree> children) {
		this.children = children;
	}
}
