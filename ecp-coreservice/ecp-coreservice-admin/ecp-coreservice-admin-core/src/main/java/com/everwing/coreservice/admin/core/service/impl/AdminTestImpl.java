package com.everwing.coreservice.admin.core.service.impl;

import com.everwing.coreservice.common.admin.service.AdminTest;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.platform.dao.mapper.extra.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminTestImpl")
public class AdminTestImpl implements AdminTest {

	@Autowired
	TestMapper testMapper;

	@Override
	public RemoteModelResult<String> test(String ha) {
		System.out.println("ecp-coreservice-admin-core-AdminTestImpl.test()");
		return new RemoteModelResult<String>(testMapper.test());
	}

}
