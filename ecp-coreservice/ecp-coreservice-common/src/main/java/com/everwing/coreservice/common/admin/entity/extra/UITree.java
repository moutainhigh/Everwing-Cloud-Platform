package com.everwing.coreservice.common.admin.entity.extra;

import java.util.List;

/**
 * @author MonKong
 * @date 2017年7月28日
 */
public class UITree {
	private String id;// ID
	private String text;// 树的值
	private String state;// open或者closed,当open时为展开状态或者叶子节点,为closed时有子节点且从服务器加载
	private String iconCls;
	private Boolean checked;
	private List<UITree> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public List<UITree> getChildren() {
		return children;
	}

	public void setChildren(List<UITree> children) {
		this.children = children;
	}
}
