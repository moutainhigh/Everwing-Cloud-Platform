package com.everwing.coreservice.platform.api.util;

import com.everwing.coreservice.common.admin.service.CompanyService;
import com.everwing.coreservice.common.platform.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 注入所有dao和service对象，供子类直接调用
 * @author MonKong
 * @date 2017年3月29日
 */
@Component
public class ServiceResources {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/*---- Service ----*/
	@Autowired
	protected AccountAndHouseService accountAndHouseService;
	@Autowired
	protected AccountService accountService;
	@Autowired
	protected CommonService commonService;
	@Autowired
	protected IdGenService idGenService;
	@Autowired
	protected CompanyService companyService;
	@Autowired
	protected FastDFSService fastDFSService;
	@Autowired
	protected IdentityService identityService;

	@Autowired
	protected InformationService informationService;
}
