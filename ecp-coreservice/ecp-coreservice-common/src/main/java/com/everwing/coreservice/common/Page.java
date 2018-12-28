package com.everwing.coreservice.common;
public class Page implements java.io.Serializable{
	private static final long serialVersionUID = -5319064537975959840L;
	private int showCount = 10; //每页显示记录数
	private int totalPage;		//总页数
	private int totalResult;	//总记录数
	private int currentPage;	//当前页
	private int currentResult;	//当前记录起始索引
	public int getTotalPage() {
		if(totalResult % showCount == 0)
			totalPage = totalResult / showCount;
		else
			totalPage = totalResult / showCount+1;
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	public int getCurrentPage() {
		if(getTotalPage() > 0){
			if(currentPage > getTotalPage()) {
				currentPage = getTotalPage();
			}
		}

		if(currentPage <= 0) {
			currentPage = 1;
		}
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getShowCount() {
		return showCount;
	}
	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}
	public int getCurrentResult() {
		currentResult = (getCurrentPage()-1)*getShowCount();
		if(currentResult<0)
			currentResult = 0;
		return currentResult;
	}
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}

	@Override
	public String toString() {
		return "Page{" +
				"showCount=" + showCount +
				", totalPage=" + totalPage +
				", totalResult=" + totalResult +
				", currentPage=" + currentPage +
				", currentResult=" + currentResult +
				'}';
	}
}
