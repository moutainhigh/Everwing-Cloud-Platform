package com.everwing.server.admin.controller;

import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.AdminInfo;
import com.everwing.coreservice.common.platform.entity.generated.RoleAndAccount;
import com.everwing.coreservice.common.platform.entity.generated.RoleAndAccountExample;
import com.everwing.coreservice.common.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {
	@GetMapping("/main")
	public String index() {
		return toAdminView("account");
	}
	
	@PostMapping("/list")
	public @ResponseBody String list(PageBean pageBean) {
		addParamToPageBean(pageBean, "queryContent");
		//query accounts
		RemoteModelResult<PageBean> rmr = commonAdminApi.listAdminAccountByPage(pageBean);
		List<Map<String,Object>> accountList = (List<Map<String, Object>>) getModel(rmr).getItemList();
		if (accountList.size() < 1) {
			return renderAdminJson(rmr);
		}
		//query roles of every account
		ArrayList<String> idsList = new ArrayList<String>();
		for(Map<String,Object> accountMap:accountList){
			idsList.add((String)accountMap.get("accountId"));
		}
		List<Map<String, Object>> rolesList = getModel(commonAdminApi.queryRoleByAccountIds(idsList.toArray(new String[]{})));
		
		//add roles into accounts Map
		for(Map<String,Object> accountMap:accountList){
			for(Map<String,Object> roleMap:rolesList){
				if(roleMap.get("accountId").equals(accountMap.get("accountId"))){//find roles of every account
					List<String> roleList = (List<String>) accountMap.get("roleList");
					if (roleList == null) {// add roles into corresponding account
						ArrayList<String> newRoleList = new ArrayList<String>();
						newRoleList.add((String) roleMap.get("roleName"));
						accountMap.put("roleList", newRoleList);
					}else{
						roleList.add((String) roleMap.get("roleName"));
					}
				}
			}
		}
		
		return renderAdminJson(rmr);
	}


	@PostMapping("/save")
	public @ResponseBody String save(Account account, AdminInfo adminInfo, String roleIds) {
		// save account
		String accountId = randomUUID();
		account.setAccountId(accountId);
		account.setCreateTime(new Date());
		account.setUpdateTime(new Date());
		account.setType(Dict.ACCOUNT_TYPE_ADMIN.getIntValue());
		account.setState(Dict.ACCOUNT_STATE_NORMAL.getIntValue());
		commonQueryApi.insertSelective(account);

		adminInfo.setAccountId(account.getAccountId());
		adminInfo.setAdminInfoId(randomUUID());
		adminInfo.setCreaterAccountId(getCurrUser().getAccountId());
		commonQueryApi.insertSelective(adminInfo);

		// save corresponding RoleAndAccount
		if(StringUtils.isNoneBlank(roleIds)){
			for(String roleId:roleIds.split(",")){
				RoleAndAccount raa = new RoleAndAccount();
				raa.setRoleId(roleId);
				raa.setAccountId(accountId);
				raa.setRoleAndAccountId(randomUUID());
				raa.setUpdateTime(new Date());
				commonQueryApi.insertSelective(raa);
			}
		}
		
		return renderSuccess();
	}
	
	@PostMapping("/updateOnlyAccount")
	public @ResponseBody String updateOnlyAccount(Account account) {
		commonQueryApi.updateByPrimaryKeySelective(account);
		return renderSuccess();
	}
	
	@PostMapping("/update")
	public @ResponseBody String update(Account account, AdminInfo adminInfo, String roleIds) {
		String accountId = StringUtils.isBlank(adminInfo.getAccountId()) ? account.getAccountId()
				: adminInfo.getAccountId();
		CommonUtils.isAnyNull(accountId,adminInfo.getAdminInfoId());
		// update account
		account.setAccountId(accountId);
		account.setUpdateTime(new Date());
		commonQueryApi.updateByPrimaryKeySelective(account);
		
		if(StringUtils.isNotBlank(adminInfo.getAdminInfoId())){
			commonQueryApi.updateByPrimaryKeySelective(adminInfo);
		}
		
		//update RoleAndAccount
		RoleAndAccountExample example = new RoleAndAccountExample();
		example.createCriteria().andAccountIdEqualTo(accountId);
		commonQueryApi.deleteByExample(RoleAndAccount.class, example);
		
		// save corresponding RoleAndAccount
		if(StringUtils.isNoneBlank(roleIds)){
			
			for(String roleId:roleIds.split(",")){
				RoleAndAccount raa = new RoleAndAccount();
				raa.setRoleId(roleId);
				raa.setAccountId(accountId);
				raa.setRoleAndAccountId(randomUUID());
				raa.setUpdateTime(new Date());
				commonQueryApi.insertSelective(raa);
			}
		}
		
		return renderSuccess();
	}
}