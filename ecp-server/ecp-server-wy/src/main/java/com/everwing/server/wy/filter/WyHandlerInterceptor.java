package com.everwing.server.wy.filter;/**
 * Created by wust on 2018/11/30.
 */

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.wy.api.sys.TSysOperationLogApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * Function:认证、授权
 * Reason:先检查用户是否登录，登录后再进行授权验证
 * Date:2018/11/30
 * @author wusongti@lii.com.cn
 */
@Component
public class WyHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private SpringRedisTools springRedisTools;

    @Autowired
    private TSysOperationLogApi tSysOperationLogApi;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return WyAuthorizingRealmHandlerInterceptor.preHandle(httpServletRequest,httpServletResponse,o,springRedisTools);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        WyOperationLogHandlerInterceptor.postHandle(httpServletRequest,httpServletResponse,o,tSysOperationLogApi);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
