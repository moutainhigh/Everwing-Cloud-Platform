package com.everwing.server.admin.util;

import com.everwing.coreservice.admin.api.CommonAdminApi;
import com.everwing.coreservice.admin.api.TestApi;
import com.everwing.coreservice.admin.api.UploadFileApi;
import com.everwing.coreservice.platform.api.CommonQueryApi;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.server.admin.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 注入所有dao和service对象，供子类直接调用
 * @author MonKong
 * @date 2017年3月29日
 */
@Component
public class Resources {
	
	/*---- Service ----*/
	@Autowired
	protected FileService fileService;
	
	
	/*---- Api ----*/
	@Autowired
	protected TestApi testApi;
	@Autowired
	protected CommonAdminApi commonAdminApi;
	@Autowired
	protected UploadFileApi uploadFileApi;
	@Autowired
	protected CompanyApi companyApi;
	@Autowired
	protected CommonQueryApi commonQueryApi;
	@Autowired
	protected FastDFSApi fastDFSApi;
	


}
