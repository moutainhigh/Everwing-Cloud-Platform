package com.everwing.coreservice.common.admin.util;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.CaseFormat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageBean implements Serializable{
	private static final long serialVersionUID = -5688441117254642667L;
	
	@JSONField(serialize=false)
	private int rows = 15;// 每页行数
	
	@JSONField(serialize=false)
	private int page = 1;// 当前页数
	
	@JSONField(serialize=false)
	private int totalPage;// 总页数
	
	@JSONField(name="total")
	private int totalSize;// 总条数
	
	@JSONField(name="rows")
	private List<?> itemList;// 数据
	
	@JSONField(serialize=false)
	private Map<String, Object> params = new HashMap<String, Object>(); //条件查询参数
	
	@JSONField(serialize=false)
	private String sort;//排序字段
	
	@JSONField(serialize=false)
	private String order;//asc/desc
	
	public PageBean() {
		super();
	}
	
	public PageBean(int pageSize, int pageNum) {
		super();
		this.rows = pageSize;
		this.page = pageNum;
	}
	
	public void addParam(String key, Object val) {
		params.put(key, val);
	}


	public int getTotalPage() {
		if (this.totalSize % this.rows != 0) {
			this.totalPage = (int) (this.totalSize / this.rows + 1);
		} else {
			this.totalPage = (int) (this.totalSize / this.rows);
		}
		if (totalPage == 0)
			totalPage = 1;
		return totalPage;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public List<?> getItemList() {
		return itemList;
	}

	public void setItemList(List<?> itemList) {
		this.itemList = itemList;
	}

	public int getLimitEnd() {
		return rows;
	}

	public int getLimitStart() {
		return rows * (page - 1);
	}


	public Map<String, Object> getParams() {
		return params;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sort);
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "PageBean [rows=" + rows + ", page=" + page + ", totalPage=" + getTotalPage()
				+ ", totalSize=" + totalSize + ", itemList=" + itemList + ", params=" + params
				+ ", sort=" + sort + ", order=" + order + "]";
	}

}
