package com.everwing.server.admin.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.admin.entity.extra.UITree;
import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.server.admin.contants.ResponseCode;
import com.everwing.server.admin.exception.ApiException;
import com.everwing.server.admin.pojo.ResponseResult;
import com.everwing.server.admin.util.MyRealm.ShiroUser;
import com.everwing.server.admin.util.Resources;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @description 抽取APIController共用方法
 * @author MonKong
 * @date 2017年3月27日
 */
public class BaseController extends Resources {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected <T> T getModel(RemoteModelResult<T> remoteResult) throws ApiException {
		handleResult(remoteResult);
		
		return remoteResult.getModel();
	}

	protected String renderApiJson(ResponseResult rr) throws ApiException {
		return JSON.toJSONString(rr);
	}

	protected String renderAdminJson(RemoteModelResult<?> remoteResult) throws ApiException {
		handleResult(remoteResult);
		
		return JSON.toJSONString(remoteResult.getModel());
	}

	protected String renderApiJson(RemoteModelResult<?> remoteResult) throws ApiException {
		handleResult(remoteResult);
		
		ResponseResult responseResult = new ResponseResult();
		Object obj = remoteResult.getModel();
		responseResult.setData(obj);
		return JSON.toJSONString(responseResult);
	}
	
	protected void handleResult(RemoteModelResult<?> remoteResult){
		if (remoteResult.isSuccess()) {
			return;
		} else {
			throw new ApiException(remoteResult);
		}
	}

	protected String renderApiJson(ResponseCode rc) {
		return JSON.toJSONString(new ResponseResult(rc));
	}

	protected String renderSuccess() {
		return renderApiJson(new RemoteModelResult<Void>());
	}

	protected String toAdminView(String subJsp) {
		getCurrRequest().setAttribute("subJsp", subJsp);
		return "admin/base_page";
	}

	protected HttpServletRequest getCurrRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
	}
	
	protected void setRequestAttr(String key, Object val) {
		 getCurrRequest().setAttribute(key, val);
	}
	
	protected Account getCurrUser() {
		return ((ShiroUser)SecurityUtils.getSubject().getPrincipal()).getUser();
	}
	
	protected String randomUUID() {
		return UUID.randomUUID().toString();
	}
	
	protected ArrayList<UITree> containNodes(ArrayList<UITree> sunTreeList,String title) {
		UITree main = new UITree();
		main.setText(title);
		main.setChildren(sunTreeList);

		ArrayList<UITree> mainList = new ArrayList<UITree>();
		mainList.add(main);
		return mainList;
	}

	protected void addParamToPageBean(PageBean pageBean, String... paramKey) {
		if (paramKey != null) {
			for (String key : paramKey) {
				String val = getCurrRequest().getParameter(key);
				if (StringUtils.isNotBlank(val)) {
					logger.info("params  {} : {}",key,val);
					pageBean.addParam(key, val);
				}
			}
		}
	}

}
