package com.everwing.server.admin.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.admin.entity.extra.UITree;
import com.everwing.coreservice.common.platform.entity.generated.Permission;
import com.everwing.coreservice.common.platform.entity.generated.PermissionExample;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {

	@GetMapping("treeView/role/{roleId}")
	public @ResponseBody String permissionsOfRole(@PathVariable("roleId") String roleId) {
		// query all permission of the account
		List<Permission> rolePermissionlist = getModel(
				commonAdminApi.listPermissionByRoleId(roleId));

		ArrayList<UITree> treeList = makeCheckedTreeList(rolePermissionlist);

		return JSON.toJSONString(containNodes(treeList, "管理平台权限"));
	}

	@GetMapping("treeView/account/{accountId}")
	public @ResponseBody String permissionsOfAccount(@PathVariable("accountId") String accountId) {
		// query all permission of the account
		List<Permission> accountPermissionlist = getModel(
				commonAdminApi.listPermissionByAccountId(accountId));
		ArrayList<UITree> treeList = makeCheckedTreeList(accountPermissionlist);

		return JSON.toJSONString(containNodes(treeList, "管理平台权限"));
	}

	@GetMapping("treeView/allPermissions")
	public @ResponseBody String allPermissions() {
		List<Permission> allPermissionlist = getModel(
				commonQueryApi.selectListByExample(Permission.class, null));

		ArrayList<UITree> treeList = new ArrayList<UITree>();
		for (Permission p : allPermissionlist) {
			UITree tree = new UITree();
			tree.setId(p.getPermissionId());
			tree.setText(p.getPermissionDescription());
			treeList.add(tree);
		}
		return JSON.toJSONString(containNodes(treeList, "管理平台权限"));
	}

	@GetMapping("/currUserPermissioin")
	public @ResponseBody String currUserPermissioin() {
		return renderAdminJson(
				commonAdminApi.listPermissionByAccountId(getCurrUser().getAccountId()));
	}

	private ArrayList<UITree> makeCheckedTreeList(List<Permission> accountPermissionlist) {
		// query all permission
		List<Permission> allPermissionlist = getModel(
				commonQueryApi.selectListByExample(Permission.class, new PermissionExample()));
		ArrayList<UITree> treeList = new ArrayList<UITree>();
		for (Permission p1 : allPermissionlist) {
			UITree tree = new UITree();
			tree.setId(p1.getPermissionId());
			tree.setText(p1.getPermissionDescription());

			for (Permission p2 : accountPermissionlist) {
				if (p2.getPermissionId().equals(p1.getPermissionId())) {
					tree.setChecked(true);
				}
			}
			treeList.add(tree);
		}
		return treeList;
	}

	public static void main(String[] args) throws Exception {
		// IOUtils test
		System.out.println(
				IOUtils.toString(new FileInputStream("/Users/MonKong/Downloads/test.txt")));
	}
}
