package com.everwing.server.dynamicreports.filter;/**
 * Created by wust on 2017/8/1.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.context.DynamicreportsBusinessContext;
import com.everwing.coreservice.common.dynamicreports.DynamicreportsEnum;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsResourceVO;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUserVO;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.PropertiesHelper;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.server.dynamicreports.utils.CookieUtils;
import com.everwing.server.dynamicreports.utils.DynamicreportsJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Function:认证、授权
 * Reason:先检查用户是否登录，登录后再进行授权验证
 * Date:2017/8/1
 * @author wusongti@lii.com.cn
 */
public class DynamicreportsAuthorizingRealm implements Filter {

    static Logger logger = LogManager.getLogger(DynamicreportsAuthorizingRealm.class);

    private SpringRedisTools springRedisTools;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        springRedisTools = SpringContextHolder.getBean("redisDataOperator");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,token,origin,content-type,accept");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        logger.info("###########################请求地址{}，{}",request.getRequestURL(),request.getServletPath());

        String loginName = "";
        String key = "";
        String reqUrl = request.getServletPath().replace("//","/");

        // 不需要过滤的地址，系统工具类
        if(reqUrl.contains("utils/UtilController")){
            logger.info("###########################请求的地址在[UtilController]里面，请求通过，请求地址{}，{}",request.getRequestURL(),request.getServletPath());
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 不需要过滤的地址，白名单配置
        String noFilterUrlList = PropertiesHelper.getInstance("config/server-dynamicreports.properties").getValue("noFilterUrlList");
        String[] noFilterUrlLists = noFilterUrlList.split(",");
        for (String filterUrlList : noFilterUrlLists) {
            if(isEqual(reqUrl,filterUrlList)){
                logger.info("###########################请求的地址在[noFilterUrlList]里面，请求通过，请求地址{}，{}",request.getRequestURL(),request.getServletPath());
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        /**
         * 从cookie获取token
         */
        String token = CookieUtils.findCookie(request, ApplicationConstant.COOKIE_TOKEN_REPORT_WEB_LOGIN.getStringValue());
        if(StringUtils.isBlank(CommonUtils.null2String(token))){
            logger.info("###########################token丢失。");
            response.sendError(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        if(token.split("\\.").length != 3){
            logger.info("###########################这是一个非法的token[{}]。",token);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        /**
         * 解析token
         */
        try {
            JSONObject jsonObject = DynamicreportsJwtUtil.getInstance().parseToken(token);
            loginName = jsonObject.getString("loginName");
            key = String.format(ApplicationConstant.REPORT_WEB_LOGIN_KEY.getStringValue(),loginName);
        } catch (Exception e) {
            logger.error("解析token出错：",e);
            response.sendError(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        // 从缓存获取登录信息
        JSONObject jsonObjectRedis = springRedisTools.getByKey(key) == null ? null : (JSONObject)springRedisTools.getByKey(key);
        if(jsonObjectRedis != null){
            // 设置登录用户的基本信息到上下文环境
            setDynamicreportsBusinessContext(jsonObjectRedis,request.getLocale().toString());

            // 是否开启地址过滤
            String openAuthorization = PropertiesHelper.getInstance("config/server-dynamicreports.properties").getValue("openAuthorization");
            if(StringUtils.isBlank(CommonUtils.null2String(openAuthorization)) || "false".equalsIgnoreCase(openAuthorization)){
                logger.info("###########################系统没有开启认证授权模式，请求通过。");
                filterChain.doFilter(servletRequest, servletResponse);
            }else{
                logger.info("###########################认证成功，开始授权...");
                boolean prmissionFlag = false;

                // 可否在白名单里面找到
                List<TRightsResourceVO> whiteResource = jsonObjectRedis.get(DynamicreportsEnum.USERCONTEXT_whiteResource.getStringValue()) == null ? null : (List<TRightsResourceVO>)jsonObjectRedis.get(DynamicreportsEnum.USERCONTEXT_whiteResource.getStringValue());
                for (TRightsResourceVO tRightsResourceVO : whiteResource) {
                    if(isEqual(reqUrl,tRightsResourceVO.getSrcUrl())){
                        prmissionFlag = true;
                        break;
                    }
                }

                // 可否在非白名单里面找到
                List<TRightsResourceVO> isNotWhiteResource = jsonObjectRedis.get(DynamicreportsEnum.USERCONTEXT_isNotWhiteResource.getStringValue()) == null ? null : (List<TRightsResourceVO>)jsonObjectRedis.get(DynamicreportsEnum.USERCONTEXT_whiteResource.getStringValue());
                for (TRightsResourceVO tRightsResourceVO : isNotWhiteResource) {
                    if(isEqual(reqUrl,tRightsResourceVO.getSrcUrl())){
                        prmissionFlag = true;
                        break;
                    }
                }


                if(!prmissionFlag){
                    // 拒绝访问
                    logger.info("###########################授权失败，拒绝访问，请求地址{}，{}，登录账号{}",request.getRequestURL(),request.getServletPath(),loginName);

                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }else{
                    // 可以访问
                    logger.info("###########################授权成功，请求地址{}，{}，登录账号{}",request.getRequestURL(),request.getServletPath(),loginName);

                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }
        }else {
            // 缓存没有登录信息，登录信息失效了，需要重新登录
            logger.info("###########################认证失败，登录信息已经失效，请重新登录，请求地址{}，{}，登录账号{}",request.getRequestURL(),request.getServletPath(),loginName);

            response.sendError(HttpServletResponse.SC_NO_CONTENT);
        }
    }


    /**
     * 判断请求的地址和持有的地址是否匹配
     * @param reqUrl    页面请求的地址
     * @param hasUrl    当前登录用户持有的地址
     * @return
     */
    private boolean isEqual(String reqUrl , String hasUrl){
        if(StringUtils.isBlank(CommonUtils.null2String(hasUrl))){
            return false;
        }

        String[] hasUrlCharsByBackslash = hasUrl.split("/");

        String[] reqUrlCharsByBackslash = reqUrl.split("/");

        // 格式是否正确
        if(hasUrlCharsByBackslash.length != reqUrlCharsByBackslash.length){

            //return false;
        }

        String hasUrlRegx = hasUrl.replaceAll("\\*", "[\\\\w\\\\W]+");

        Pattern p = Pattern.compile(hasUrlRegx);
        Matcher m = p.matcher(reqUrl);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 设置用户信息到线程上下文
     * @param jsonObject
     * @param lan
     */
    private void setDynamicreportsBusinessContext(JSONObject jsonObject,String lan){
        TRightsUserVO tRightsUserVO = JSONObject.parseObject(jsonObject.getString(DynamicreportsEnum.USERCONTEXT_userInfo.getStringValue()),TRightsUserVO.class);
        DynamicreportsBusinessContext.getContext().setUserId(tRightsUserVO.getUserId());
        DynamicreportsBusinessContext.getContext().setLoginName(tRightsUserVO.getLoginName());
        DynamicreportsBusinessContext.getContext().setStaffNumber(tRightsUserVO.getStaffNumber());
        DynamicreportsBusinessContext.getContext().setStaffName(tRightsUserVO.getStaffName());
        DynamicreportsBusinessContext.getContext().setUserType(tRightsUserVO.getType());
        DynamicreportsBusinessContext.getContext().setLan(lan);
    }

    @Override
    public void destroy() {

    }
}
