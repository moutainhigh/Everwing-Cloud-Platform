package com.everwing.server.admin.controller;

import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.server.admin.contants.ResponseCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController extends BaseController {
	
	@GetMapping("/admin_login")
	public String loginPage() {
		Subject subject = SecurityUtils.getSubject();
    	subject.logout();
		return "admin/admin_login";
	}
	
	@PostMapping("/login")
	public String login(String username,String password) {
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(new UsernamePasswordToken(username, password));
		} catch (AuthenticationException e) {
			if(e.getCause() instanceof ECPBusinessException){
				ECPBusinessException ex = (ECPBusinessException) e.getCause();
				setRequestAttr("errorMsg", ResponseCode.getByMapCode(ex.getErrorCode()).getMsg());
				return "admin/admin_login";
			}
		}
		return "redirect:/index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "redirect:/index";
	}
}