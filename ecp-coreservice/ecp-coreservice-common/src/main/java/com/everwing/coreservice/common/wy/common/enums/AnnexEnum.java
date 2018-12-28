package com.everwing.coreservice.common.wy.common.enums;

/**
 * @TODO 附件Enum
 * @author DELL
 *
 */
public enum AnnexEnum {

	
	annex_type_img(1),	//图片
	annex_type_zip(2),	//压缩包
	annex_type_doc(3),	//文档
	annex_type_txt(4),	//文本
	annex_type_excel(5),	//excel
	annex_type_pdf(6),		//pdf
	
	annex_is_used_yes(0),	//附件可用
	annex_is_used_no(1),	//附件停用
	
	//文件类型
	annex_file_type_img("img"),
	annex_file_type_zip("zip"),
	annex_file_type_doc("doc"),
	annex_file_type_txt("txt"),
	annex_file_type_xls("xls"),
	annex_file_type_xlsx("xlsx"),
	annex_file_type_pdf("pdf");
	
	
	private Integer value;
	private String strValue;
	AnnexEnum(){}
	AnnexEnum(Integer v){this.value = v;}
	AnnexEnum(String v){this.strValue = v;}
	
	public Integer getIntV(){
		return value;
	}
	
	public String getStringV(){
		return strValue;
	}
	
}
