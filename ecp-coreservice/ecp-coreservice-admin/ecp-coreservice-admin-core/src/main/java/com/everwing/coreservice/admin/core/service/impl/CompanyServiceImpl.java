package com.everwing.coreservice.admin.core.service.impl;

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.admin.core.util.PinYin4jUtil;
import com.everwing.coreservice.common.admin.service.CompanyService;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.platform.entity.generated.CompanyApproval;
import com.everwing.coreservice.common.platform.entity.generated.CompanyDatabase;
import com.everwing.coreservice.common.utils.generator.WyCodeGenerator;
import com.everwing.coreservice.common.wy.initialization.AbstractInitializeData;
import com.everwing.coreservice.platform.util.MapperResources;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class CompanyServiceImpl extends MapperResources implements CompanyService {
	private String jdbcHost;
	private int jdbcPort;
	@Value("${jdbc.platform.url}")
	private String jdbcUrl;


	@Autowired
	private SpringRedisTools springRedisTools;

	@Autowired
	private AbstractInitializeData initializeDataSource;

	@Autowired
	private AbstractInitializeData initializeMenusResources;

	@Override
	@Transactional(rollbackFor=Exception.class)
	public RemoteModelResult<Company> createCompany(CompanyApproval companyApproval) {
		Account _account = accountMapper.selectByPrimaryKey(companyApproval.getAccountId());
		// 如果账号已关联公司，则不创建
		if (_account == null || StringUtils.isNotBlank(_account.getCompanyId())) {
			logger.info("....账号已关联公司，直接返回");
			return new RemoteModelResult<Company>();
		}

		String companyCode = WyCodeGenerator.genCompanyCode();
		String companyName = companyApproval.getCompanyName();
		String pinYinName = PinYin4jUtil.converterToFirstSpell(companyName);
		String dbUserName = PinYin4jUtil.converterToFirstSpell(companyName);
		String dbPsw = UUID.randomUUID().toString().substring(0, 5);

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String dbName = "wypt_" +  sf.format(new Date()) + companyCode + "_" + pinYinName;
		

		logger.info("创建公司信息："+dbName + "|" + dbUserName + "|" + dbPsw);
		
		// 执行脚本
		String companyId = UUID.randomUUID().toString();
		String ddlTableSql = readSqlFileToString("classpath:sql/wy_ddl_table.sql");
		String ddlFunctionSql = readSqlFileToString("classpath:sql/wy_ddl_function.sql");
		String dmlSql = readSqlFileToString("classpath:sql/wy_dml.sql");
		companyExtraMapper.wy_ddl(dbName, dbUserName, dbPsw,ddlTableSql,ddlFunctionSql);
		companyExtraMapper.wy_dml(dbName,dmlSql);
		companyExtraMapper.init_user(dbName, _account.getAccountName(),companyCode, companyId,companyName, WyCodeGenerator.genUserCode());

		// 插入公司数据
		Company company = new Company();
		company.setCompanyName(companyApproval.getCompanyName());
		company.setCompanyLocation(companyApproval.getCompanyLocation());
		company.setCompanyAddress(companyApproval.getCompanyAddress());
		company.setBizRegistryLicenseNum(companyApproval.getBizRegistryLicenseNum());
		company.setLogoFileId(companyApproval.getLogoFileId());
		company.setBizSaleLicenseFileId(companyApproval.getBizSaleLicenseFileId());
		company.setOrgCodeFileId(companyApproval.getOrgCodeFileId());
		company.setTaxLicenseFileId(companyApproval.getTaxLicenseFileId());
		company.setPropertyCertFileId(companyApproval.getPropertyCertFileId());
		company.setCompanyId(companyId);
		company.setJdbcUsername(dbUserName);
		company.setJdbcPassword(dbPsw);
		company.setJdbcUrl("jdbc:mysql://" + jdbcHost + ":" + jdbcPort + "/" + dbName + "?useSSL=false&allowPublicKeyRetrieval=true&allowMultiQueries=true");
		companyMapper.insert(company);

		// 更新账号公司字段
		Account account = new Account();
		account.setAccountId(companyApproval.getAccountId());
		account.setCompanyId(company.getCompanyId());
		accountMapper.updateByPrimaryKeySelective(account);
		
		//插入公司名
		CompanyDatabase companyDatabase = new CompanyDatabase();
		companyDatabase.setDbName(dbName);
		companyDatabase.setCompanyId(companyId);
		companyDatabaseMapper.insertSelective(companyDatabase);

		/**
		 * 放入缓存
		 */
		initializeDataSource.init(companyId);


		/**
		 * 初始化菜单
		 */
		initializeMenusResources.init(companyId);
		return new RemoteModelResult<>(company);
	}
	
	@PostConstruct
	public void init() throws Exception{
		URL url = new URL(jdbcUrl.replace("jdbc:mysql", "http"));
		jdbcHost = url.getHost();
		jdbcPort = url.getPort();
	}

	private String readSqlFileToString(String path) {
		FileInputStream is = null;
		StringBuilder stringBuilder = null;
		try {
			File file = ResourceUtils.getFile(path);
			if (file != null && file.length() != 0) {
				is = new FileInputStream(file);
				InputStreamReader streamReader = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(streamReader);
				String line;
				stringBuilder = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					if(line.startsWith("--")
							|| line.startsWith("/*")
							|| "".equals(line)){
						continue;
					}

					if(path.contains("function") && line.contains("--")){
						line = line.substring(0,line.indexOf("--"));
					}
					stringBuilder.append(line).append(" ");
				}
				reader.close();
				is.close();
			}
		} catch (Exception e) {
			throw new ECPBusinessException("读取sql文件【"+path+"】失败。");
		}
		return String.valueOf(stringBuilder);
	}
}
