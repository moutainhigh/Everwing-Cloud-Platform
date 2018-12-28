package com.everwing.server.wy.filter;/**
 * Created by wust on 2018/12/5.
 */

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.PropertiesHelper;
import com.everwing.coreservice.common.wy.common.UserContextModel;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import com.everwing.coreservice.common.wy.entity.system.resource.TSysResource;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import com.everwing.server.wy.util.WyJwtHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Function:认证授权拦截器
 * Reason:
 * Date:2018/12/5
 * @author wusongti@lii.com.cn
 */
public class WyAuthorizingRealmHandlerInterceptor {
    static Logger logger = LogManager.getLogger(WyAuthorizingRealmHandlerInterceptor.class);

    public static boolean preHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o,SpringRedisTools springRedisTools) throws Exception {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,jsonWebToken,origin,content-type,accept");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials","true");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletRequest.setCharacterEncoding("UTF-8");

        if(logger.isInfoEnabled()){
            logger.info("###########################请求地址{}，{}",httpServletRequest.getPathInfo(),httpServletRequest.getServletPath());
        }

        String loginName = "";

        String contextPath = httpServletRequest.getContextPath();

        String reqUrl = httpServletRequest.getPathInfo().replace(contextPath,"");


        // 不需要过滤的地址，其他外部接口
        if(reqUrl.contains("nologin")){
            if(logger.isInfoEnabled()){
                logger.info("###########################请求的地址包含[nologin]，该请求由程序员申请绕过token校验，请求通过，请求地址{}，{}",reqUrl,contextPath);
            }
            return true;
        }

        // 不需要过滤的地址，白名单配置
        String noFilterUrlList = PropertiesHelper.getInstance("config/conf.properties").getValue("noFilterUrlList");
        String[] noFilterUrlLists = noFilterUrlList.split(",");
        for (String filterUrlList : noFilterUrlLists) {
            String filterUrl = filterUrlList.substring(filterUrlList.lastIndexOf("/") + 1);
            if("**".equals(filterUrl)){ // 只匹配前缀
                String filterUrlP = filterUrlList.substring(0,filterUrlList.lastIndexOf("/"));
                if(reqUrl.contains(filterUrlP)){
                    if(logger.isInfoEnabled()){
                        logger.info("###########################请求的地址在[noFilterUrlList]里面，请求通过，请求地址{}，{}",reqUrl,contextPath);
                    }

                    return true;
                }
            }else{ // 全匹配
                if(isEqual(reqUrl,filterUrlList)){
                    if(logger.isInfoEnabled()){
                        logger.info("###########################请求的地址在[noFilterUrlList]里面，请求通过，请求地址{}，{}",reqUrl,contextPath);
                    }

                    return true;
                }
            }
        }

        /**
         * 获取token
         */
        String token = httpServletRequest.getHeader("jsonWebToken");
        if(StringUtils.isBlank(CommonUtils.null2String(token))){
            token = httpServletRequest.getParameter("jsonWebToken");
            if(StringUtils.isBlank(CommonUtils.null2String(token))){
                if(logger.isInfoEnabled()){
                    logger.info("###########################token丢失。");
                }

                httpServletResponse.sendError(HttpServletResponse.SC_NO_CONTENT);
                return false;
            }
        }

        if(token.split("\\.").length != 3){
            if(logger.isInfoEnabled()){
                logger.info("###########################这是一个非法的token[{}]。",token);
            }

            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        /**
         * 解析token
         */
        try {
            loginName = WyJwtHelper.getInstance().parseWyLoginToken(token);
        } catch (Exception e) {
            logger.error("解析token出错：",e);
            httpServletResponse.sendError(HttpServletResponse.SC_NO_CONTENT);
            return false;
        }



        String key = String.format(ApplicationConstant.WY_WEB_LOGIN_KEY.getStringValue(),loginName);

        // 从缓存获取登录信息
        UserContextModel redisUserContext = springRedisTools.getByKey(key) == null ? null : (UserContextModel)springRedisTools.getByKey(key);
        if(redisUserContext != null){
            // 设置登录用户的基本信息到上下文环境
            setBusinessContext(redisUserContext,httpServletRequest.getLocale().toString(),token,contextPath);

            // 是否开启地址过滤
            String openAuthorization = PropertiesHelper.getInstance("config/conf.properties").getValue("openAuthorization");
            if(StringUtils.isBlank(CommonUtils.null2String(openAuthorization)) || "false".equalsIgnoreCase(openAuthorization)){
                if(logger.isInfoEnabled()){
                    logger.info("###########################系统没有开启认证授权模式，请求通过。");
                }
                return true;
            }else{
                if(logger.isInfoEnabled()){
                    logger.info("###########################认证成功，开始授权...");
                }

                boolean prmissionFlag = false;

                // 可否在白名单里面找到
                List<TSysResource> anonResources = redisUserContext.getAnonResources();
                for (TSysResource tSysResource : anonResources) {
                    if(isEqual(reqUrl,tSysResource.getSrcUrl())){
                        prmissionFlag = true;
                        break;
                    }
                }

                // 可否在非白名单里面找到
                List<TSysResource> resources = redisUserContext.getAnonResources();
                for (TSysResource tSysResource : resources) {
                    if(isEqual(reqUrl,tSysResource.getSrcUrl())){
                        prmissionFlag = true;
                        break;
                    }
                }


                if(!prmissionFlag){
                    // 拒绝访问
                    if(logger.isInfoEnabled()){
                        logger.info("###########################授权失败，拒绝访问，请求地址{}，{}，登录账号{}",reqUrl,contextPath,loginName);
                    }

                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }else{
                    // 可以访问
                    if(logger.isInfoEnabled()){
                        logger.info("###########################授权成功，请求地址{}，{}，登录账号{}",reqUrl,contextPath,loginName);
                    }

                    return true;
                }
            }
        }else {
            // 缓存没有登录信息，登录信息失效了，需要重新登录
            if(logger.isInfoEnabled()){
                logger.info("###########################认证失败，登录信息已经失效，请重新登录，请求地址{}，{}，登录账号{}",reqUrl,contextPath,loginName);
            }

            httpServletResponse.sendError(HttpServletResponse.SC_NO_CONTENT);

            return false;
        }
    }


    /**
     * 判断请求的地址和持有的地址是否匹配
     * @param reqUrl    页面请求的地址
     * @param hasUrl    当前登录用户持有的地址
     * @return
     */
    private static boolean isEqual(String reqUrl , String hasUrl){
        if(StringUtils.isBlank(CommonUtils.null2String(hasUrl))){
            return false;
        }

        String[] hasUrlCharsBybackslash = hasUrl.split("/");

        String[] reqUrlCharsBybackslash = reqUrl.split("/");

        // 格式是否正确
        if(hasUrlCharsBybackslash.length != reqUrlCharsBybackslash.length){
            return false;
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


    private static void setBusinessContext(UserContextModel redisUserContext, String lan, String token,String contextPath){
        TSysUser user = redisUserContext.getLoginUser();
        TSysCompany rootCompany = redisUserContext.getRootCompany();

        WyBusinessContext.getContext().setUserId(user.getUserId());
        WyBusinessContext.getContext().setLoginName(user.getLoginName());
        WyBusinessContext.getContext().setStaffNumber(user.getStaffNumber());
        WyBusinessContext.getContext().setStaffName(user.getStaffName());
        WyBusinessContext.getContext().setCompanyId(rootCompany.getCompanyId());
        WyBusinessContext.getContext().setCompanyCode(rootCompany.getCode());
        WyBusinessContext.getContext().setCompanyName(rootCompany.getName());
        WyBusinessContext.getContext().setLan(lan);
        WyBusinessContext.getContext().setSuAdmin(user.getType().equalsIgnoreCase(LookupItemEnum.staffType_systemAdmin.getStringValue()));
        WyBusinessContext.getContext().setJsonWebToken(token);
        WyBusinessContext.getContext().setOrganizationComponent(redisUserContext.getOrganizationComponent());
        WyBusinessContext.getContext().setContextPath(contextPath);
    }
}
