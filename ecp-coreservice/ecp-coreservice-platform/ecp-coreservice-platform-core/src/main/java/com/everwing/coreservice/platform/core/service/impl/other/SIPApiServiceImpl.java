package com.everwing.coreservice.platform.core.service.impl.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseSipData;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SIPApiServiceImpl{

	private Logger logger= LoggerFactory.getLogger(getClass());

	@Value("${sys.sipAPIBasePath}")
	public String basePath;


	public boolean register(List<Account> accountList)  {
		String result = HttpUtils.doPost(basePath + "/user", createUserJson(accountList));
		return handleSipResult(result);
	}

	public boolean cancel(List<Account> accountList)  {
		String result = HttpUtils.doPost(basePath + "/deluser", createUserJson(accountList));
		return handleSipResult(result);
	}

	public boolean bind(List<AccountAndHouseSipData> dataList)  {
		if(dataList!=null && dataList.size()>0){
			System.err.println("开始调用SIP/OPENFIRE绑定接口...");
			String result = HttpUtils.doPost(basePath.concat("/user/bind"), createAccountAndHouseJson(dataList));
			return handleSipResult(result);
		}else{
			return false;
		}
	}

	public boolean unbind(List<AccountAndHouseSipData> dataList)  {
		if(dataList!=null && dataList.size()>0){
			String result = HttpUtils.doPost(basePath.concat("/user/delbind"), createAccountAndHouseJson(dataList));
			return handleSipResult(result);
		}else{
			return false;
		}
	}

	//修改用户密码
	public boolean modifyPwd(List<Account> accountList){
		if(!accountList.isEmpty()){
			String result=HttpUtils.doPost(basePath.concat("/resetpwd"),createUserJson(accountList));
			return handleSipResult(result);
		}
		return false;
	}

	/**
	 * format:{"binduser":[{"username":"房间内部账号","bindusername":"内部账号"}]}
	 */
	private static String createAccountAndHouseJson(List<AccountAndHouseSipData> dataList) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		JSONArray jsonArray = new JSONArray();
		map.put("binduser", jsonArray);
		jsonArray.addAll(dataList);
		return JSON.toJSONString(map);
	}

	/**
	 * format:{"user":[{"username":"","password":""}]}
	 */
	private static String createUserJson(List<Account> accountList) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Map> dataList = new ArrayList<Map>();
		map.put("user", dataList);

		for(Account account:accountList){
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("username", account.getAccountCode());
			data.put("password", account.getPassword());
			dataList.add(data);
		}

		return JSON.toJSONString(map);
	}

	private boolean handleSipResult(String result) {
		logger.info("调用SIP返回结果："+result);
		//返回结果居然是html... <html><body>operation succeeded.</body></html>
		boolean isSuccess = result.contains("succe");
		if(!isSuccess){
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		return isSuccess;
	}
}
