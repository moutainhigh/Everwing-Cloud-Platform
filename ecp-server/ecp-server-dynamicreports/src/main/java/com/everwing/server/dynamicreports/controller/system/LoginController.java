package com.everwing.server.dynamicreports.controller.system;/**
 * Created by wust on 2018/2/6.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.dynamicreports.DynamicreportsEnum;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.dynamicreports.api.system.CommonApi;
import com.everwing.coreservice.dynamicreports.api.system.RoleApi;
import com.everwing.coreservice.dynamicreports.api.system.UserApi;
import com.everwing.server.dynamicreports.utils.CookieUtils;
import com.everwing.server.dynamicreports.utils.DynamicreportsJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 *
 * Function:
 * Reason:
 * Date:2018/2/6
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/LoginController")
public class LoginController {
    @Autowired
    private CommonApi commonApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private RoleApi roleApi;

    @Autowired
    private SpringRedisTools springRedisTools;

    @RequestMapping(value = "/login/{loginName}/{password}",method = RequestMethod.POST)
    public @ResponseBody MessageMap login(HttpServletResponse response,@PathVariable String loginName, @PathVariable String password){
        MessageMap mm = new MessageMap();

        if(StringUtils.isBlank(CommonUtils.null2String(loginName))){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("请输入登录账号");
            return mm;
        }

        if(StringUtils.isBlank(CommonUtils.null2String(password))){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("请输入登录密码");
            return mm;
        }

        RemoteModelResult<MessageMap> remoteModelResult = commonApi.login(loginName,password);
        if(remoteModelResult.isSuccess()){
            mm = remoteModelResult.getModel();
            if(MessageMap.INFOR_SUCCESS.equalsIgnoreCase(mm.getFlag())){
                Object obj = springRedisTools.getByKey(String.format(ApplicationConstant.REPORT_WEB_LOGIN_KEY.getStringValue(),loginName));
                if(obj != null){
                    JSONObject jsonObject = (JSONObject) obj;
                    String token = "";
                    try {
                        token = setJsonWebToken(mm,jsonObject.getJSONObject(DynamicreportsEnum.USERCONTEXT_userInfo.getStringValue()));
                    } catch (Exception e) {
                        mm.setFlag(MessageMap.INFOR_ERROR);
                        mm.setMessage("登录失败：" +e.getMessage());
                        return mm;
                    }

                    try {
                        CookieUtils.addCookie(response,ApplicationConstant.COOKIE_TOKEN_REPORT_WEB_LOGIN.getStringValue(),token,0);
                    } catch (UnsupportedEncodingException e) {
                        mm.setFlag(MessageMap.INFOR_ERROR);
                        mm.setMessage("登录失败：" +e.getMessage());
                        return mm;
                    }
                }
            }else{
                System.out.println("##################"+mm.getMessage());
            }
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
            return mm;
        }
        return mm;
    }

    private String setJsonWebToken(MessageMap mm, JSONObject jsonObject) throws Exception {
        return DynamicreportsJwtUtil.getInstance().createToken(jsonObject,0);
    }
}
