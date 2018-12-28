package com.everwing.coreservice.wy.core.request;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name="ExcelInfo")
public class ExcelInfo {
	private String[] titles;
	private String[] fields;
	private String sheetName;
	private List list;
	public String[] getTitles() {
		return titles;
	}
	public void setTitles(String[] titles) {
		this.titles = titles;
	}
	public String[] getFields() {
		return fields;
	}
	public void setFields(String[] fields) {
		this.fields = fields;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	
	
}
