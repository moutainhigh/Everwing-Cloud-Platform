package com.everwing.server.platform.controller.api;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.server.platform.constant.ResponseCode;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountApiController extends BaseApiController {
	
	@GetMapping("/test")
	public String test() throws Exception {
		return "finish";
	}

	
	
	/**
	 * @description 物业用户常用按钮数据的更新/添加
	 */
	@PostMapping("/update/wyFavoriteIcon")
	public String wyFavoriteIcon(String accountName,String wyFavoriteIcon) {
		HashMap<String, String> dataMap = new HashMap<String,String>();
		dataMap.put("wyFavoriteIcon", wyFavoriteIcon);
		
		return renderJson(accountApi.exDataUpdate(accountName,
				Dict.ACCOUNT_TYPE_STAFF.getIntValue(),  JSON.toJSONString(dataMap)));
	}
	

	/**
	 * @description 账号注册
	 */
	@PostMapping("")
	public String register(@RequestBody Map<String, Object> paramsMap) throws Exception {
		Account account = new Account();
		BeanUtils.populate(account, paramsMap);
		String smsCode = (String) paramsMap.get("smsCode");
		String phoneNum = (String) paramsMap.get("phoneNum");

		return renderJson(accountApi.register(account, smsCode, phoneNum));
	}

	/**
	 * @description 账号注销
	 */
	@PostMapping("/cancel")
	public String cancel(@RequestBody Account account) {
		return renderJson(accountApi.cancelAccount(account.getAccountName(), account.getType(),
				account.getPassword()));
	}

	/**
	 * @description 账号登录
	 */
	@PostMapping("/login")
	public String login(@RequestBody Account account) {
		return renderJson(accountApi.queryByAccountNameAndPsw(account.getAccountName(),
				account.getType(), account.getPassword()));
	}

	/**
	 * @description 账号查询
	 */
	@GetMapping("/accountname/{accountName}")
	public String queryByAccountName(@PathVariable String accountName, Integer type) {
		return renderJson(accountApi.queryByAccountName(accountName, type));
	}

	/**
	 * @description 根据来源ID查询
	 */
	@GetMapping("/sourceid/{sourceId}")
	public String queryBySourceId(@PathVariable("sourceId") String sourceId, Integer type) {
		// 验证数据
		if (sourceId == null || type == null) {
			return renderJson(ResponseCode.PARAMS_MISSING);
		}
		return renderJson(accountApi.queryBySourceId(sourceId, type));
	}
	
	/**
	 * @description 账号额外信息更新/添加
	 */
	@PostMapping("/update/exdata")
	public String exDataUpdate(@RequestBody Account accountData) {
		return renderJson(accountApi.exDataUpdate(accountData.getAccountName(),
				accountData.getType(), accountData.getExData()));
	}

	/**
	 * @description 根据内部账号查询
	 */
	@GetMapping("/accountCode/{accountCode}")
	public String queryByAccountCode(@PathVariable("accountCode") String accountCode,
			HttpServletRequest req) {
		// 验证数据
		if (StringUtils.isBlank(accountCode)) {
			return renderJson(ResponseCode.PARAMS_MISSING);
		}
		return renderJson(accountApi.queryByAccountCode(accountCode));
	}
}