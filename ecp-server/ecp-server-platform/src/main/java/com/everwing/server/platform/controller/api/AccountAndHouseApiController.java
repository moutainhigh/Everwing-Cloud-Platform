package com.everwing.server.platform.controller.api;

import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/accountBind")
public class AccountAndHouseApiController extends BaseApiController {

	/**
	 * @throws Exception
	 * @description 资产绑定
	 */
	@PostMapping("")
	public String add(@RequestBody Map<String, Object> body) throws Exception {
		String objAccount = (String) body.get("objAccount");
		String houseAccount = (String) body.get("houseAccount");
		Integer type = (Integer) body.get("type");

		ArrayList<AccountAndHouseData> dataList = new ArrayList<AccountAndHouseData>();
		dataList.add(new AccountAndHouseData(objAccount, type, houseAccount));
		return renderJson(accountAndHouseApi.batchAdd(dataList));
	}

	/**
	 * @description 资产解绑
	 */
	@PostMapping("/cancel")
	public String delete(@RequestBody Map<String, Object> body) throws Exception {
		String objAccount = (String) body.get("objAccount");
		String houseAccount = (String) body.get("houseAccount");
		Integer type = (Integer) body.get("type");
		
		ArrayList<AccountAndHouseData> dataList = new ArrayList<AccountAndHouseData>();
		dataList.add(new AccountAndHouseData(objAccount, type, houseAccount));
		return renderJson(accountAndHouseApi.batchDelete(dataList));
	}

}