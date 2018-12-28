package com.everwing.server.wy.util;/**
 * Created by wust on 2017/7/7.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.wy.api.sys.TSysRoleApi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * Function:工具
 * Reason:该控制器不走权限过滤器
 * Date:2017/7/7
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/UtilController")
public class UtilController {
    private static final Logger logger = LogManager.getLogger(UtilController.class);

    @Autowired
    private TSysRoleApi tSysRoleApi;

    @Autowired
    private CompanyApi companyApi;

    /**
     * 初始化权限
     * @param request
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Common,businessName="初始化资源权限",operationType= OperationEnum.Save)
    @RequestMapping(value = "/nologin/initResources",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap initResources(HttpServletRequest request){
        MessageMap mm = new MessageMap();
        StringBuffer msg = new StringBuffer();

        RemoteModelResult<List<Company>> remoteModelResult = companyApi.queryAllCompany();
        if(remoteModelResult.isSuccess()){
            List<Company> companies = remoteModelResult.getModel();
            if(CollectionUtils.isNotEmpty(companies)){
                for (Company company : companies) {
                    RemoteModelResult<MessageMap> result = tSysRoleApi.initResources(company.getCompanyId());
                    if(!result.isSuccess()){
                        msg.append("公司["+company.getCompanyName()+"]的菜单初始化失败；");
                    }else{
                        logger.info("公司["+company.getCompanyName()+"]的菜单初始化成功");
                    }
                }
            }
        }else{
            logger.error("*******从平台查询公司列表异常{}*********",remoteModelResult.getMsg());
        }

        if(StringUtils.isNotBlank(msg)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(msg.toString());
        }
        return mm;
    }



    /**
     * 根据公司初始化菜单资源
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Common,businessName="根据公司初始化菜单资源",operationType= OperationEnum.Save)
    @RequestMapping(value = "/nologin/initResourcesByCompanyId",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap initResourcesByCompanyId(){
        MessageMap mm = new MessageMap();
        StringBuffer msg = new StringBuffer();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<MessageMap> result = tSysRoleApi.initResources(ctx.getCompanyId());
        if(!result.isSuccess()){
            msg.append("公司["+ctx.getCompanyName()+"]的菜单初始化失败；");
        }else{
            logger.info("公司["+ctx.getCompanyName()+"]的菜单初始化成功");
        }



        if(StringUtils.isNotBlank(msg)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(msg.toString());
        }
        return mm;
    }


    /**
     * 获取所有Controller的URL映射地址
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/nologin/getUrlMapping",method = RequestMethod.POST)
    public MessageMap getUrlMapping(HttpServletRequest request) {
        MessageMap mm = new MessageMap();
        String operation = "<operation id=\"%s\" name=\"%s\" desc=\"%s\" url=\"%s\"/>";

        WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        RequestMappingHandlerMapping rmhp = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for(RequestMappingInfo info : map.keySet()){
            String patternsStr = info.getPatternsCondition().toString().replace("[","").replace("]","").replaceAll("(\\{\\w+\\})","*");
            System.out.println(String.format(operation,UUID.randomUUID(),"","",patternsStr));
        }
        return mm;
    }
}
