package com.everwing.server.wy.filter;/**
 * Created by wust on 2018/11/9.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLog;
import com.everwing.coreservice.wy.api.sys.TSysOperationLogApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *
 * Function:业务日志拦截器
 * Reason:
 * Date:2018/11/9
 * @author wusongti@lii.com.cn
 */
public class WyOperationLogHandlerInterceptor {

    public static void postHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o,final TSysOperationLogApi tSysOperationLogApi) throws Exception {
        if(o != null){
            if(o instanceof HandlerMethod){
                HandlerMethod handlerMethod = (HandlerMethod)o;
                Object annotationObj = handlerMethod.getMethodAnnotation(WyOperationLogAnnotation.class);
                if(annotationObj != null){
                    WyOperationLogAnnotation wyOperationLogAnnotation = (WyOperationLogAnnotation)annotationObj;
                    WyBusinessContext ctx = WyBusinessContext.getContext();
                    if(StringUtils.isNotBlank(CommonUtils.null2String(ctx.getCompanyId()))){
                        TSysOperationLog entity = new TSysOperationLog();
                        entity.setCompanyId(ctx.getCompanyId());
                        entity.setProjectId(ctx.getProjectId());
                        entity.setModuleName(wyOperationLogAnnotation.moduleName().getValue());
                        entity.setBusinessName(wyOperationLogAnnotation.businessName());
                        entity.setOperationType(wyOperationLogAnnotation.operationType().getValue());
                        entity.setOperationDate(new Date());
                        entity.setOperationUser(ctx.getLoginName());
                        tSysOperationLogApi.insert(ctx.getCompanyId(),entity);
                    }
                }
            }
        }
    }

}
