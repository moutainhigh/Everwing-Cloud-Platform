package com.everwing.server.admin.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.admin.entity.extra.UITree;
import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.*;
import com.everwing.coreservice.common.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
	
	@GetMapping("/main")
	public String main() {
		return toAdminView("role");
	}
	
	@PostMapping("/list")
	public @ResponseBody String list(PageBean pageBean) {
		addParamToPageBean(pageBean, "queryContent");
		return renderAdminJson(commonAdminApi.listRoleByPage(pageBean));
	}
	
	@GetMapping("/treeView/account/{accountId}")
	public @ResponseBody String permissionsOfAccount(@PathVariable("accountId") String accountId) {
		//query all role of the account
		RoleAndAccountExample example = new RoleAndAccountExample();
		example.createCriteria().andAccountIdEqualTo(accountId);
		List<RoleAndAccount> rnaList = getModel(commonQueryApi.selectListByExample(RoleAndAccount.class, example));
		
		List<Role> roleList = new ArrayList<Role>();
		if(rnaList.size() > 0){
			ArrayList<String> idsList = new ArrayList<String>();
			for(RoleAndAccount rna:rnaList){
				idsList.add(rna.getRoleId());
			}
			
			RoleExample example2 = new RoleExample();
			example2.createCriteria().andRoleIdIn(idsList);
			roleList = getModel(commonQueryApi.selectListByExample(Role.class, example2));
		}
		
		
		ArrayList<UITree> treeList = makeCheckedTreeList(roleList);

		return JSON.toJSONString(containNodes(treeList,"管理平台角色"));
	}
	
	private ArrayList<UITree> makeCheckedTreeList(List<Role> accountPermissionlist) {
		// query all available role
		RoleExample example = new RoleExample();
		example.createCriteria().andStatusEqualTo(1);
		List<Role> allRolelist = getModel(
				commonQueryApi.selectListByExample(Role.class, example));
		
		ArrayList<UITree> treeList = new ArrayList<UITree>();
		for (Role r1 : allRolelist) {
			UITree tree = new UITree();
			tree.setId(r1.getRoleId());
			tree.setText(r1.getRoleName());
			
			for (Role r2 : accountPermissionlist) {
				if (r2.getRoleId().equals(r1.getRoleId())) {
					tree.setChecked(true);
				}
			}
			treeList.add(tree);
		}
		return treeList;
	}
	
	@GetMapping("/treeView/allRole")
	public @ResponseBody String allRole() {
		RoleExample example = new RoleExample();
		example.createCriteria().andStatusEqualTo(Dict.ROLE_STATE_ABLE.getIntValue());
		List<Role> allRolelist = getModel(
				commonQueryApi.selectListByExample(Role.class, example));

		ArrayList<UITree> treeList = new ArrayList<UITree>();
		for (Role r : allRolelist) {
			UITree tree = new UITree();
			tree.setId(r.getRoleId());
			tree.setText(r.getRoleName());
			treeList.add(tree);
		}
		return JSON.toJSONString(containNodes(treeList,"管理平台角色"));
	}
	
	@PostMapping("/update")
	public @ResponseBody String update(Role role,String permissionIds) {
		CommonUtils.isAnyNull(role.getRoleId());
		
		role.setUpdateTime(new Date());
		
		//更新角色
		handleResult(commonQueryApi.updateByPrimaryKeySelective(role));
		
		if(StringUtils.isNotBlank(permissionIds)){
			//删除所有权限
			PermissionAndRoleExample example = new PermissionAndRoleExample();
			example.createCriteria().andRoleIdEqualTo(role.getRoleId());
			handleResult(commonQueryApi.deleteByExample(PermissionAndRole.class, example));
			
			//插入权限
			addPermissions(role.getRoleId(), permissionIds);
			
		}
		return renderSuccess();
	}
	
	@PostMapping("/save")
	public @ResponseBody String save(Role role,String permissionIds) {
		role.setCreateAccountId(getCurrUser().getAccountId());
		role.setCreateTime(new Date());
		role.setUpdateTime(new Date());
		role.setRoleId(UUID.randomUUID().toString());
		role.setStatus(Dict.ROLE_STATE_ABLE.getIntValue());
		commonQueryApi.insertSelective(role);
		
		if(StringUtils.isNotBlank(permissionIds)){
			addPermissions(role.getRoleId(), permissionIds);
		}
		
		return renderSuccess();
	}

	private void addPermissions(String roleId, String permissionIds) {
		String[] pids = permissionIds.split(",");
		if(pids!=null && pids.length>0){
			for(String pid:pids){
				PermissionAndRole pnr = new PermissionAndRole();
				pnr.setPermissionAndRoleId(UUID.randomUUID().toString());
				pnr.setPermissionId(pid);
				pnr.setRoleId(roleId);
				pnr.setUpdateTime(new Date());
				commonQueryApi.insertSelective(pnr);
			}
		}
	}
	
	
	
}