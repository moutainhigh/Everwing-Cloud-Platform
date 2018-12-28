package com.everwing.coreservice.platform.core.util;

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.utils.OpenFireUtils;
import com.everwing.coreservice.common.utils.SipUtils;
import com.everwing.coreservice.platform.core.service.impl.AccountServiceImpl;
import com.everwing.coreservice.platform.core.service.impl.other.OpenfireApiServiceImpl;
import com.everwing.coreservice.platform.core.service.impl.other.SIPApiServiceImpl;
import com.everwing.coreservice.platform.core.service.impl.other.SmsServiceImpl;
import com.everwing.coreservice.platform.dao.mapper.extra.FastDFSExtraMapper;
import com.everwing.coreservice.platform.dao.mapper.extra.IdGenExtraMapper;
import com.everwing.coreservice.platform.dao.mapper.generated.LyAuthorizationAccountMapper;
import com.everwing.coreservice.platform.util.MapperResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description 注入所有dao和service对象，供子类直接调用
 * @author MonKong
 * @date 2017年3月29日
 */
@Component
public class Resources extends MapperResources{
	
	/*---- Others ----*/
	@Resource(name="accountServiceImpl")
	protected AccountServiceImpl accountService;
	
	/*---- Service ----*/
	@Autowired
	protected SIPApiServiceImpl sipApiService;
	@Autowired
	protected OpenfireApiServiceImpl openfireApiService;

	@Autowired
	protected SmsServiceImpl smsService;

	@Autowired
	protected IdGenExtraMapper idGenExtraMapper;

	@Autowired
	protected FastDFSExtraMapper fastDFSExtraMapper;

	@Autowired
	protected LyAuthorizationAccountMapper lyAuthorizationAccountMapper;

	@Resource
	protected SpringRedisTools springRedisTools;

	@Resource
	protected SipUtils sipUtils;

	@Resource
	protected OpenFireUtils openFireUtils;
}
