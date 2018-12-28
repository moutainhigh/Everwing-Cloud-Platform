package com.everwing.coreservice.platform.util;

import com.everwing.coreservice.platform.dao.mapper.cust.PersonCustExtraMapper;
import com.everwing.coreservice.platform.dao.mapper.extra.*;
import com.everwing.coreservice.platform.dao.mapper.generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 注入所有dao对象，供子类直接调用
 * @author MonKong
 * @date 2017年3月29日
 */
@Component
public class MapperResources {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/*---- Extra Mapper ----*/
	@Autowired
	protected AccountAndHouseExtraMapper accountAndHouseExtraMapper;
	@Autowired
	protected AccountExtraMapper accountExtraMapper;
	@Autowired
	protected CommonMapper commonMapper;
	@Autowired
	protected CommonAdminExtraMapper commonAdminExtraMapper;
	@Autowired
	protected CompanyExtraMapper companyExtraMapper;
	@Autowired
	protected IdentityExtraMapper identityExtraMapper;
	@Autowired
	protected TcBuildingExtraMapper tcBuildingExtraMapper;
	@Autowired
	protected AppPkgExtraMapper appPkgExtraMapper;

	/*---- Generated Mapper ----*/
	@Autowired
	protected TcBuildingMapper tcBuildingMapper;
	@Autowired
	protected RoleMapper roleMapper;
	@Autowired
	protected AppPkgMapper appPkgMapper;
	@Autowired
	protected AdminInfoMapper adminInfoMapper;
	@Autowired
	protected PersonBuildingMapper personBuildingMapper;
	@Autowired
	protected AccountAndHouseMapper accountAndHouseMapper;
	@Autowired
	protected AnnouncementMapper announcementMapper;
	@Autowired
	protected PermissionMapper permissionMapper;
	@Autowired
	protected BuildingGateMapper buildingGateMapper;
	@Autowired
	protected OperationLogMapper operationLogMapper;
	@Autowired
	protected CompanyDatabaseMapper companyDatabaseMapper;
	@Autowired
	protected GatingMapper gatingMapper;
	@Autowired
	protected PersonCustMapper personCustMapper;
	@Autowired
	protected AccountIdentityMapper accountIdentityMapper;
	@Autowired
	protected RoleAndAccountMapper roleAndAccountMapper;
	@Autowired
	protected AccountMapper accountMapper;
	@Autowired
	protected UploadFileMapper uploadFileMapper;
	@Autowired
	protected IdGenMapper idGenMapper;
	@Autowired
	protected CompanyMapper companyMapper;
	@Autowired
	protected TProExeInfoMapper tProExeInfoMapper;
	@Autowired
	protected PermissionAndRoleMapper permissionAndRoleMapper;
	@Autowired
	protected MkjLogMapper mkjLogMapper;
	@Autowired
	protected CompanyApprovalMapper companyApprovalMapper;
	@Autowired
	protected EnterpriseMapper enterpriseMapper;
	@Autowired
	protected LyAuthorizationAccountMapper lyAuthorizationAccountMapper;
	@Autowired
	protected PersonCustExtraMapper personCustExtraMapper;
	@Autowired
	protected InformationMapper informationMapper;

}
