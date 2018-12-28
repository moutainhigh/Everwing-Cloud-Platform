package com.everwing.server.admin.controller;

import com.everwing.coreservice.admin.api.TestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController extends BaseController {

	@Autowired
	private TestApi testApi;

	@GetMapping("/test")
	public @ResponseBody String test() {
		System.out.println("TestController.test()");
		return renderApiJson(testApi.test("123123"));
		// return "Monkong";
	}

}