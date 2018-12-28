package com.everwing.coreservice.common.wy.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置类 
 * @author zhugeruifei
 */
@Component
public class SysConfig {

	@Value("${bill.ftl.path}")
	private String billFtlPath;				//ftl存放路径
	
	@Value("${bill.ftl.fileName}")
	private String billFtlFileName;			//ftl名	bill.ftl
	
	@Value("${bill.location.unix}")
	private String unixFilePath;			//linux下bill的html存放路径
	
	@Value("${bill.location.win}")
	private String winFilePath;				//windows下bill的html存放路径

	@Value("${bill.img.location.win}")
	private String winBillImgPath;			//windows下  bill引用的img存放路径
	
	@Value("${bill.img.location.unit}")
	private String unixBillImgPath;			//linux下bill引用的img存放路径
	
	@Value("${bill.zip.location.win}")		//windows下 bill打成的压缩包存放路径
	private String winBillZipPath;
	
	@Value("${bill.zip.location.unix}")		//linux下 bill打成的zip包存放路径
	private String unixBillZipPath;
	
	@Value("${font.path}")
	private String fontPath;				//本项目的字体文件存放路径
	
	@Value("${coll.location.unix}")
	private String collTxtUnixPath;			//linux下托收文件txt存放路径
	
	@Value("${coll.location.win}")
	private String collTxtWinPath;				//windows下托收文件txt存放路径
	
	@Value("${coll.zip.location.unix}")
	private String collZipUnixPath;			//托收zip文件unix存放路径
	
	@Value("${coll.zip.location.win}")
	private String collZipWinPath;			//托收zip文件windows存放路径
	
	private String billFilePath;			//根据系统判断返回的bill的html存放路径
	
	private String billImgPath;				//根据系统判断返回的bill的img的存放路径
	
	private String billZipPath;				//根据系统判断返回的bill的zip存放路径
	
	private String collZipPath;
	
	private String collTxtPath;
	
	public String getBillFtlPath() {
		return billFtlPath;
	}

	public String getBillFtlFileName() {
		return billFtlFileName;
	}

	public String getBillFilePath() {
		if(System.getProperty("os.name").toUpperCase().startsWith("WIN")){
			return winFilePath;
		}else{
			return unixFilePath;
		}
	}
	public String getFontPath() {
		return fontPath;
	}

	public String getBillImgPath() {
		if(System.getProperty("os.name").toUpperCase().startsWith("WIN")){
			return winBillImgPath;
		}else{
			return unixBillImgPath;
		}
	}
	
	public String getBillZipPath() {
		if(System.getProperty("os.name").toUpperCase().startsWith("WIN")){
			return winBillZipPath;
		}else{
			return unixBillZipPath;
		}
	}
	
	public String getCollZipPath() {
		if(System.getProperty("os.name").toUpperCase().startsWith("WIN")){
			return collZipWinPath;
		}else{
			return collZipUnixPath;
		}
	}

	public String getCollTxtPath() {
		if(System.getProperty("os.name").toUpperCase().startsWith("WIN")){
			return collTxtWinPath;
		}else{
			return collTxtUnixPath;
		}
	}

	public String getCollTxtUnixPath() {
		return collTxtUnixPath;
	}

	public void setCollTxtUnixPath(String collTxtUnixPath) {
		this.collTxtUnixPath = collTxtUnixPath;
	}

	public String getCollTxtWinPath() {
		return collTxtWinPath;
	}

	public void setCollTxtWinPath(String collTxtWinPath) {
		this.collTxtWinPath = collTxtWinPath;
	}

	public String getCollZipUnixPath() {
		return collZipUnixPath;
	}

	public void setCollZipUnixPath(String collZipUnixPath) {
		this.collZipUnixPath = collZipUnixPath;
	}

	public String getCollZipWinPath() {
		return collZipWinPath;
	}

	public void setCollZipWinPath(String collZipWinPath) {
		this.collZipWinPath = collZipWinPath;
	}

	public void setBillFtlPath(String billFtlPath) {
		this.billFtlPath = billFtlPath;
	}

	public void setBillFtlFileName(String billFtlFileName) {
		this.billFtlFileName = billFtlFileName;
	}

	public void setUnixFilePath(String unixFilePath) {
		this.unixFilePath = unixFilePath;
	}

	public void setWinFilePath(String winFilePath) {
		this.winFilePath = winFilePath;
	}

	public void setWinBillImgPath(String winBillImgPath) {
		this.winBillImgPath = winBillImgPath;
	}

	public void setUnixBillImgPath(String unixBillImgPath) {
		this.unixBillImgPath = unixBillImgPath;
	}

	public void setWinBillZipPath(String winBillZipPath) {
		this.winBillZipPath = winBillZipPath;
	}

	public void setUnixBillZipPath(String unixBillZipPath) {
		this.unixBillZipPath = unixBillZipPath;
	}

	public void setFontPath(String fontPath) {
		this.fontPath = fontPath;
	}

	public void setBillFilePath(String billFilePath) {
		this.billFilePath = billFilePath;
	}

	public void setBillImgPath(String billImgPath) {
		this.billImgPath = billImgPath;
	}

	public void setBillZipPath(String billZipPath) {
		this.billZipPath = billZipPath;
	}

	public void setCollZipPath(String collZipPath) {
		this.collZipPath = collZipPath;
	}

	public void setCollTxtPath(String collTxtPath) {
		this.collTxtPath = collTxtPath;
	}

	public String getUnixFilePath() {
		return unixFilePath;
	}

	public String getWinFilePath() {
		return winFilePath;
	}

	public String getWinBillImgPath() {
		return winBillImgPath;
	}

	public String getUnixBillImgPath() {
		return unixBillImgPath;
	}

	public String getWinBillZipPath() {
		return winBillZipPath;
	}

	public String getUnixBillZipPath() {
		return unixBillZipPath;
	}
}
