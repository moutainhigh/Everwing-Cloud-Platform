package com.everwing.server.platform.controller;

import com.everwing.coreservice.common.dto.LoginRslt;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.AccountIdentity;
import com.everwing.coreservice.common.utils.HttpUtils;
import com.everwing.coreservice.platform.api.TestApi;
import com.everwing.server.platform.constant.ResponseCode;
import com.everwing.server.platform.controller.api.BaseApiController;
import com.everwing.server.platform.pojo.ResponseResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController extends BaseApiController{
	 Logger logger2 = LogManager.getLogger(getClass());
	@Autowired
	private TestApi testApi;
	
	@RequestMapping("/test2")
	@ResponseBody
	public String test2() throws Exception {
		AccountIdentity model = getModel(identityApi.queryByAccountName("17000600000", 0));
		return "asd";
	}

	@RequestMapping("/test")
	@ResponseBody
	public ResponseResult test() throws Exception {
		testApi.test("123", 123);
//		testApi.test2("test");
		ResponseResult result = new ResponseResult();
		result.setResponseCode(ResponseCode.ACCOUNT_CANCELED);
		return result;
	}

	@RequestMapping("/test/login")
	public @ResponseBody <T> RemoteModelResult<?> testLogin() throws Exception {
		RemoteModelResult<LoginRslt> result = testApi.test2("");
		
		LoginRslt model = getModel(result);
		if (result.isSuccess()) {
			System.out.println("TestController.testLogin()");
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		String url="http://114.55.42.151:8860";
		String content = URLEncoder.encode("【桃源居物业公司】您的验证码为5566，请于5分钟内输入验证码");
		String mobibes = "18924269015,17727958997";
		String password = "U8ZZ7G7FH7";
		String customer_code = "950142";
		
		Map<String, String> params=new HashMap<String, String>();
		params.put("cust_code", customer_code);
		params.put("content", content);
		params.put("destMobiles", mobibes);
		params.put("sign", DigestUtils.md5Hex(content+password));
		String result = HttpUtils.doPost(url,HttpUtils.convertToQueryStr(params).substring(1));
		System.out.println(result);
	}
}