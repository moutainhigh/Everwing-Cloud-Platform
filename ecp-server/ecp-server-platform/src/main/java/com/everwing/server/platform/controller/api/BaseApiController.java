package com.everwing.server.platform.controller.api;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.server.platform.constant.ResponseCode;
import com.everwing.server.platform.exception.ApiException;
import com.everwing.server.platform.pojo.ResponseResult;
import com.everwing.server.platform.util.Resources;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @description 抽取APIController共用方法
 * @author MonKong
 * @date 2017年3月27日
 */
public class BaseApiController extends Resources{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private final String REQUEST_BODY_ATTR = "request_body_string";
	
	@Value("${platform.download.baseurl}")
	private String downloadBaseUrl;
	
	protected Integer parseTypeFromBody(){
		return (Integer) parseFromRequestBody("type");
	}
	
	protected String parseAccountNameFromBody(){
		return (String) parseFromRequestBody("accountName");
	}
	
	protected <T> T parseObjFromBody(Class<T> clazz){
		return (T) parseFromRequestBody(clazz);
	}
	
	private Object parseFromRequestBody(Object obj) {
		HttpServletRequest request = getCurrRequest();
		String body = (String) request.getAttribute(REQUEST_BODY_ATTR);

		// put request body string into request attribute
		if (StringUtils.isBlank(body)) {
			// get current request body string
			try {
				body = IOUtils.toString(request.getInputStream());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (StringUtils.isBlank(body)) {
				throw new ApiException(ResponseCode.PARAMS_MISSING);
			}
			request.setAttribute(REQUEST_BODY_ATTR, body);
		}

		
		if(obj instanceof String){
			Object val = JSON.parseObject(body).get(obj);
			if(val == null){
				throw new ApiException(ResponseCode.PARAMS_MISSING);
			}
			return val;
		}else{
			Object val = JSON.parseObject(body, (Class)obj);
			if(val == null){
				throw new ApiException(ResponseCode.PARAMS_MISSING);
			}
			return val;
		}
	}
	
	protected HttpServletRequest getCurrRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
	}
	
	protected void checkSuccess(RemoteModelResult remoteResult) throws ApiException{
		getModel(remoteResult);
	}
	
	protected <T> T getModel(RemoteModelResult<T> remoteResult) throws ApiException {
		if (remoteResult.isSuccess()) {
			return remoteResult.getModel();
		} else {
			throw new ApiException(remoteResult);
		}
	}
	
	protected <T> T getFirstModel(RemoteModelResult<List<T>> remoteResult) throws ApiException {
		if (remoteResult.isSuccess()) {
			List<T> list = remoteResult.getModel();
			return list.size() > 0 ? list.get(0) : null;
		} else {
			throw new ApiException(remoteResult);
		}
	}

	protected String renderJsonWithImgPath(RemoteModelResult<?> remoteResult) throws ApiException {
		return renderJson(remoteResult,true);
	}
	
	protected String renderJson(RemoteModelResult<?> remoteResult) throws ApiException {
		return renderJson(remoteResult,false);
	}
	
	private String renderJson(RemoteModelResult<?> remoteResult,boolean hasImgPath) throws ApiException {
		if (remoteResult.isSuccess()) {
			ResponseResult responseResult = new ResponseResult();
			Object obj = remoteResult.getModel();
			if(obj instanceof Account){ //不对外显示账号密码和id
				Account account = (Account) obj;
				account.setPassword(null);
				account.setAccountId(null);
			}
			if(obj instanceof Company){
				Company company = (Company) obj;
				company.setJdbcUrl(null);
				company.setJdbcPassword(null);
				company.setJdbcUsername(null);
			}
			if(hasImgPath){
//				responseResult.setImgBasePath(fastDFSApi.getControllerBaseUri());
				responseResult.setImgBasePath(CommonUtils.getRequestBasePath(getCurrRequest())+"/file/");
			}
			responseResult.setData(obj);
			return JSON.toJSONString(responseResult);
		} else {
			throw new ApiException(remoteResult);
		}
	}
	
	protected String renderJson(ResponseCode rc) {
		return JSON.toJSONString(new ResponseResult(rc));
	}
	
	protected String renderSuccess() {
		return renderJson(new RemoteModelResult<Void>());
	}
}
