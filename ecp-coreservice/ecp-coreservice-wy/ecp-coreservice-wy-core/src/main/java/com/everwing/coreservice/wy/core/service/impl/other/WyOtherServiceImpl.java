package com.everwing.coreservice.wy.core.service.impl.other;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCode;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCodeAndSysUser;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCodeAndSysUserExample;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCodeExample;
import com.everwing.coreservice.common.wy.service.other.WyOtherService;
import com.everwing.coreservice.wy.dao.mapper.other.OtherMapper;
import com.everwing.coreservice.wy.dao.mapper.other.TcAgentCodeAndSysUserMapper;
import com.everwing.coreservice.wy.dao.mapper.other.TcAgentCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("wyOtherServiceImpl")
public class WyOtherServiceImpl implements WyOtherService {
	@Autowired
	private TcAgentCodeMapper tcAgentCodeMapper;
	@Autowired
	private TcAgentCodeAndSysUserMapper tcAgentCodeAndSysUserMapper;
	@Autowired
	private OtherMapper otherMapper;
	
	@Override
	public BaseDto getAgentCodeByUserId(String companyId, String userId) {
//		if(StringUtils.isBlank(userId)){
//			return new BaseDto();
//		}
//		// 删除该用户的绑定
//		TcAgentCodeAndSysUserExample example = new TcAgentCodeAndSysUserExample();
//		example.createCriteria().andSysUserIdEqualTo("2d058236-c6c0-4325-956f-62e1d1beea77");
//		List<TcAgentCodeAndSysUser> obj = tcAgentCodeAndSysUserMapper.selectByExample(example);
		
		BaseDto baseDto = new BaseDto();
		baseDto.setObj(otherMapper.getAgentCodeByUserId(userId));
		return baseDto;
	}

	@Override
	public BaseDto bindAgentCode(String companyId, TcAgentCodeAndSysUser tcAgentCodeAndSysUser) {
		// 删除该用户的绑定
		TcAgentCodeAndSysUserExample example = new TcAgentCodeAndSysUserExample();
		example.createCriteria().andSysUserIdEqualTo(tcAgentCodeAndSysUser.getSysUserId());
		tcAgentCodeAndSysUserMapper.deleteByExample(example);

		// 绑定
		tcAgentCodeAndSysUser.setId(UUID.randomUUID().toString());
		tcAgentCodeAndSysUser.setCreateTime(new Date());
		tcAgentCodeAndSysUserMapper.insert(tcAgentCodeAndSysUser);
		return new BaseDto();
	}

	@Override
	public BaseDto newAgentCode(String companyId, TcAgentCode tcAgentCode) {
		tcAgentCode.setAgentCodeId(UUID.randomUUID().toString());
		tcAgentCode.setCreateTime(new Date());
		tcAgentCodeMapper.insert(tcAgentCode);
		return new BaseDto();
	}
	
	@Override
	public BaseDto updateAgentCode(String companyId, TcAgentCode tcAgentCode) {
		tcAgentCodeMapper.updateByPrimaryKeySelective(tcAgentCode);
		return new BaseDto();
	}

	@Override
	public BaseDto delAgentCode(String companyId, List<String> agentCodeIds) {
		// 删除该坐席号的绑定关系
		TcAgentCodeAndSysUserExample example = new TcAgentCodeAndSysUserExample();
		example.createCriteria().andAgentCodeIdIn(agentCodeIds);
		tcAgentCodeAndSysUserMapper.deleteByExample(example);

		// 删除坐席号
		TcAgentCodeExample example2 = new TcAgentCodeExample();
		example2.createCriteria().andAgentCodeIdIn(agentCodeIds);
		tcAgentCodeMapper.deleteByExample(example2);
		return new BaseDto();
	}
}