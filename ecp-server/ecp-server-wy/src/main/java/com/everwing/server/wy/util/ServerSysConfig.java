package com.everwing.server.wy.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 * @author xiao
 */
@Component
public class ServerSysConfig {
	
	@Value("${photo.upload.templocation.win}")
	private String winPhotoPath;
	
	@Value("${photo.upload.templocation.unix}")
	private String unixPhotoPath;
	
	@Value("${report.upload.templocation.win}")
	private String winReportPath;
	
	@Value("${report.upload.templocation.unix}")
	private String unixReportPath;

	
	private String photoPath; //精拍仪临时上传的路径

	private String reportPath;
	
	public String getReportPath(){
		if(System.getProperty("os.name").toUpperCase().startsWith("WIN")){
			return winReportPath;
		}else{
			return unixReportPath;
		}
	}
	

	public String getPhotoPath() {
		if(System.getProperty("os.name").toUpperCase().startsWith("WIN")){
			return winPhotoPath;
		}else{
			return unixPhotoPath;
		}
	}
	
}
