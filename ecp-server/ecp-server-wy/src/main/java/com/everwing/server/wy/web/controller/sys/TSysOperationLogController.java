package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2017/6/2.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLogSearch;
import com.everwing.coreservice.wy.api.sys.TSysOperationLogApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/2
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/system/TSysOperationLogController")
public class TSysOperationLogController {
    @Autowired
    private TSysOperationLogApi tSysOperationLogApi;

    @RequestMapping(value = "/listPage",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPage(HttpServletRequest request, @RequestBody TSysOperationLogSearch condition){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        condition.setCompanyId(ctx.getCompanyId());
        RemoteModelResult<BaseDto> result = tSysOperationLogApi.listPage(ctx.getCompanyId(),condition);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }
}
