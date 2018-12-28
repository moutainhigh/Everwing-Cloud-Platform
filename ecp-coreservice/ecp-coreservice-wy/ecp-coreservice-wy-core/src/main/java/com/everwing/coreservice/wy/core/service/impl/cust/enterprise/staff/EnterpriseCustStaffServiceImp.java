package com.everwing.coreservice.wy.core.service.impl.cust.enterprise.staff;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.staff.EnterpriseCustStaffNew;
import com.everwing.coreservice.common.wy.service.cust.enterprise.staff.EnterpriseCustStaffService;
import com.everwing.coreservice.wy.dao.mapper.cust.enterprisecust.staff.EnterpriseCustStaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("enterpriseCustStaffService")
public class EnterpriseCustStaffServiceImp implements EnterpriseCustStaffService {

	@Autowired
	private EnterpriseCustStaffMapper enterpriseCustStaffMapper;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listEnterpriseCustStaffPage(String companyId, EnterpriseCustStaffNew enterpreiseCustStaff) {
		List<EnterpriseCustStaffNew> enterpriseList=this.enterpriseCustStaffMapper.listPageEnterpriseCustStaff(enterpreiseCustStaff);
		//查询无数据直接返回
		if(CommonUtils.isEmpty(enterpriseList))
			return new BaseDto(null,new Page());
		//证件号码处理
		for(EnterpriseCustStaffNew s : enterpriseList){
			if(18 == s.getCertNum().length())
				s.setCertNum(s.getCertNum()+"X");
		}
		return new BaseDto(enterpriseList,enterpreiseCustStaff.getPage());
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public MessageMap addEnterpriseCustStaff(String companyId, EnterpriseCustStaffNew enterpreiseCustStaff) {
		if(null == enterpreiseCustStaff)
			return new MessageMap(MessageMap.INFOR_WARNING, "数据为空,无法新增");
		
		int num= this.enterpriseCustStaffMapper.addEnterpriseCustStaff(enterpreiseCustStaff);
		if(num >0) return new MessageMap(null, "添加成功");
		else  return new MessageMap(null, "添加失败，请检查参数信息");
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public MessageMap updateEnterpriseCustStaff(String companyId, EnterpriseCustStaffNew enterpreiseCustStaff) {
		if(null == enterpreiseCustStaff)
			return new MessageMap(MessageMap.INFOR_WARNING, "数据为空,无法修改");
		int num=this.enterpriseCustStaffMapper.updateEnterpriseCustStaff(enterpreiseCustStaff);
		if(num>0) return new MessageMap(null, "修改成功");
		else return new MessageMap(null, "未找到匹配数据，请检查参数信息");
	}

	
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public MessageMap deleteEnterpriseCustStaff(String companyId, String ids) {
		if(CommonUtils.isEmpty(ids))
			return new MessageMap(MessageMap.INFOR_WARNING, "数据为空,无法删除");
		int num= this.enterpriseCustStaffMapper.deleteEnterpriseCustStaff(CommonUtils.str2List(ids, Constants.STR_COMMA));
		if(num >0) return new MessageMap(null, "删除成功");
		else return new MessageMap(null, "未找到匹配数据，请检查参数信息");
	}

	
}
