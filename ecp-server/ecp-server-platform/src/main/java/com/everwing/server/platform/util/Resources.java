package com.everwing.server.platform.util;

import com.everwing.coreservice.platform.api.*;
import com.everwing.coreservice.platform.api.other.SmsApi;
import com.everwing.server.platform.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 注入所有Api和Service对象，供子类直接调用
 * @author MonKong
 * @date 2017年3月29日
 */
@Component
public class Resources {
	
	/*---- Service ----*/
	@Autowired
	protected AccountApi accountApi;
	@Autowired
	protected CommonQueryApi commonQueryApi;
	@Autowired
	protected LogApi logApi;
	@Autowired
	protected SmsApi smsApi;
	@Autowired
	protected AccountAndHouseApi accountAndHouseApi;
	@Autowired
	protected FileService fileService;
	@Autowired
	protected IdGenApi idGenApi;
	@Autowired
	protected FastDFSApi fastDFSApi;
	@Autowired
	protected CompanyApi companyApi;
	@Autowired
	protected IdentityApi identityApi;
}
